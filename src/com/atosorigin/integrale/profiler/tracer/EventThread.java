/*
 * Copyright (c) 2001, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package com.atosorigin.integrale.profiler.tracer;

import com.sun.jdi.*;
import com.sun.jdi.request.*;
import com.sun.jdi.event.*;

import java.util.*;
import java.io.PrintWriter;

/**
 * This class processes incoming JDI events and displays them
 *
 * @author Robert Field
 */
public class EventThread extends Thread {

	private final VirtualMachine vm; // Running VM
	private final String[] excludes; // Packages to exclude
	private final PrintWriter writer; // Where output goes
	
	private StringBuffer indent = new StringBuffer();
	
	private Stack<Long> startTimes = new Stack<Long>();

	private boolean connected = true; // Connected to VM
	private boolean vmDied = true; // VMDeath occurred

	// Maps ThreadReference to ThreadTrace instances
	private Map<ThreadReference, ThreadTrace> traceMap = new HashMap<ThreadReference, ThreadTrace>();

	EventThread(VirtualMachine vm, String[] excludes, PrintWriter writer) {
		super("event-handler");
		this.vm = vm;
		this.excludes = excludes;
		this.writer = writer;
	}

	/**
	 * Run the event handling thread. As long as we are connected, get event
	 * sets off the queue and dispatch the events within them.
	 */
	@Override
	public void run() {
		EventQueue queue = vm.eventQueue();
		while (connected) {
			try {
				EventSet eventSet = queue.remove();
				EventIterator it = eventSet.eventIterator();
				while (it.hasNext()) {
					handleEvent(it.nextEvent());
				}
				eventSet.resume();
			} catch (InterruptedException exc) {
				// Ignore
			} catch (VMDisconnectedException discExc) {
				handleDisconnectedException();
				break;
			}
		}
	}

	/**
	 * Create the desired event requests, and enable them so that we will get
	 * events.
	 * 
	 * @param excludes
	 *            Class patterns for which we don't want events
	 * @param watchFields
	 *            Do we want to watch assignments to fields
	 */
	void setEventRequests(boolean watchFields) {
		EventRequestManager mgr = vm.eventRequestManager();

		// want all exceptions
		ExceptionRequest excReq = mgr.createExceptionRequest(null, true, true);
		// suspend so we can step
		excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		excReq.enable();

		MethodEntryRequest menr = mgr.createMethodEntryRequest();
		for (int i = 0; i < excludes.length; ++i) {
			menr.addClassExclusionFilter(excludes[i]);
		}
		menr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		menr.enable();

		MethodExitRequest mexr = mgr.createMethodExitRequest();
		for (int i = 0; i < excludes.length; ++i) {
			mexr.addClassExclusionFilter(excludes[i]);
		}
		mexr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		mexr.enable();

		ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
		// Make sure we sync on thread death
		tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		tdr.enable();

		if (watchFields) {
			ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
			for (int i = 0; i < excludes.length; ++i) {
				cpr.addClassExclusionFilter(excludes[i]);
			}
			cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
			cpr.enable();
		}
	}

	/**
	 * This class keeps context on events in one thread. In this implementation,
	 * context is the indentation prefix.
	 */
	class ThreadTrace {

		final ThreadReference thread;

		ThreadTrace(ThreadReference thread) {
			this.thread = thread;
			System.out.println("====== " + thread.name() + " thread ======");
		}

//		private void println(String str) {
//			writer.println(str);
//		}

		void methodEntryEvent(MethodEntryEvent event) {

			Long start = System.nanoTime();
			startTimes.push(start);
			
			List<String> arguments = event.method().argumentTypeNames();
			String sourceName = event.method().declaringType().name();
			
			try {
				sourceName = event.method().declaringType().sourceName();
			} catch (AbsentInformationException aie) {
				System.out.println("Unable to shorten Class name " + aie);
			}
			sourceName = sourceName.substring(0, sourceName.length() - 5);

//			System.out.println(lineNo++ + ": " + indent + "enter;" + sourceName + "." + event.method().name() + ";" + arguments.toString() + ";" + start);
			indent.append("| ");
			
			writer.println("enter;" + sourceName + "." + event.method().name() + ";" + arguments.toString() + ";" + start);
		}

		void methodExitEvent(MethodExitEvent event) {

			startTimes.pop();
			
			List<String> arguments = event.method().argumentTypeNames();
			String sourceName = event.method().declaringType().name();
			
			try {
				sourceName = event.method().declaringType().sourceName();
			} catch (AbsentInformationException aie) {
				System.out.println("Unable to shorten Class name " + aie);
			}
			sourceName = sourceName.substring(0, sourceName.length() - 5);
			
//			System.out.println(lineNo++ + ": " + indent + "exit;" + sourceName + "." + event.method().name() + ";" + arguments.toString() + ";" + System.nanoTime());
			indent.delete(indent.length()-2, indent.length());
			writer.println("exit;" + sourceName + "." + event.method().name() + ";" + arguments.toString() + ";" + System.nanoTime());
		}

		void fieldWatchEvent(ModificationWatchpointEvent event) {
			Field field = event.field();
			Value value = event.valueToBe();
//			System.out.println("    " + field.name() + " = " + value);
			writer.println("    " + field.name() + " = " + value);
		}

		void exceptionEvent(ExceptionEvent event) {

			if (event != null && event.catchLocation() != null) {
				if (!((event.catchLocation().toString().startsWith("java.lang.ClassLoader")) ||
						(event.catchLocation().toString().startsWith("sun")))) {
					System.err.println("exception,E," + event.exception() + ",catch:" + event.catchLocation() + "," + System.nanoTime());
				}
			}
			// Step to the catch
			EventRequestManager mgr = vm.eventRequestManager();
			StepRequest req = mgr.createStepRequest(thread,
					StepRequest.STEP_MIN, StepRequest.STEP_INTO);
			req.addCountFilter(1); // next step only
			req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
			req.enable();
		}

		// Step to exception catch
		void stepEvent(StepEvent event) {
			// Adjust call depth
			int cnt = 0;
			// sb = new StringBuffer(baseIndent);
			try {
				cnt = thread.frameCount();
			} catch (IncompatibleThreadStateException exc) {
			}
			while (cnt-- > 0) {
				// sb.append("| ");
			}

			EventRequestManager mgr = vm.eventRequestManager();
			mgr.deleteEventRequest(event.request());
		}

		void threadDeathEvent(ThreadDeathEvent event) {
			// sb = new StringBuffer(baseIndent);
			System.out.println("====== " + thread.name() + "thread end ======");
		}
	}

	/**
	 * Returns the ThreadTrace instance for the specified thread, creating one
	 * if needed.
	 */
	ThreadTrace threadTrace(ThreadReference thread) {
		ThreadTrace trace = traceMap.get(thread);
		if (trace == null) {
			trace = new ThreadTrace(thread);
			traceMap.put(thread, trace);
		}
		return trace;
	}

	/**
	 * Dispatch incoming events
	 */
	private void handleEvent(Event event) {
		if (event instanceof ExceptionEvent) {
			exceptionEvent((ExceptionEvent) event);
		} else if (event instanceof ModificationWatchpointEvent) {
			fieldWatchEvent((ModificationWatchpointEvent) event);
		} else if (event instanceof MethodEntryEvent) {
			methodEntryEvent((MethodEntryEvent) event);
		} else if (event instanceof MethodExitEvent) {
			methodExitEvent((MethodExitEvent) event);
		} else if (event instanceof StepEvent) {
			stepEvent((StepEvent) event);
		} else if (event instanceof ThreadDeathEvent) {
			threadDeathEvent((ThreadDeathEvent) event);
		} else if (event instanceof ClassPrepareEvent) {
			classPrepareEvent((ClassPrepareEvent) event);
		} else if (event instanceof VMStartEvent) {
			vmStartEvent((VMStartEvent) event);
		} else if (event instanceof VMDeathEvent) {
			vmDeathEvent((VMDeathEvent) event);
		} else if (event instanceof VMDisconnectEvent) {
			vmDisconnectEvent((VMDisconnectEvent) event);
		} else {
			throw new Error("Unexpected event type");
		}
	}

	/***
	 * A VMDisconnectedException has happened while dealing with another event.
	 * We need to flush the event queue, dealing only with exit events (VMDeath,
	 * VMDisconnect) so that we terminate correctly.
	 */
	synchronized void handleDisconnectedException() {
		EventQueue queue = vm.eventQueue();
		while (connected) {
			try {
				EventSet eventSet = queue.remove();
				EventIterator iter = eventSet.eventIterator();
				while (iter.hasNext()) {
					Event event = iter.nextEvent();
					if (event instanceof VMDeathEvent) {
						vmDeathEvent((VMDeathEvent) event);
					} else if (event instanceof VMDisconnectEvent) {
						vmDisconnectEvent((VMDisconnectEvent) event);
					}
				}
				eventSet.resume(); // Resume the VM
			} catch (InterruptedException exc) {
				// ignore
			}
		}
	}

	private void vmStartEvent(VMStartEvent event) {
		// writer.println("-- VM Started --");
		System.out.println("-- VM Started --");
	}

	// Forward event for thread specific processing
	private void methodEntryEvent(MethodEntryEvent event) {
		threadTrace(event.thread()).methodEntryEvent(event);
	}

	// Forward event for thread specific processing
	private void methodExitEvent(MethodExitEvent event) {
		threadTrace(event.thread()).methodExitEvent(event);
	}

	// Forward event for thread specific processing
	private void stepEvent(StepEvent event) {
		threadTrace(event.thread()).stepEvent(event);
	}

	// Forward event for thread specific processing
	private void fieldWatchEvent(ModificationWatchpointEvent event) {
		threadTrace(event.thread()).fieldWatchEvent(event);
	}

	void threadDeathEvent(ThreadDeathEvent event) {
		ThreadTrace trace = traceMap.get(event.thread());
		if (trace != null) { // only want threads we care about
			trace.threadDeathEvent(event); // Forward event
		}
	}

	/**
	 * A new class has been loaded. Set watchpoints on each of its fields
	 */
	private void classPrepareEvent(ClassPrepareEvent event) {
		EventRequestManager mgr = vm.eventRequestManager();
		List<Field> fields = event.referenceType().visibleFields();
		for (Field field : fields) {
			ModificationWatchpointRequest req = mgr
					.createModificationWatchpointRequest(field);
			for (int i = 0; i < excludes.length; ++i) {
				req.addClassExclusionFilter(excludes[i]);
			}
			req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			req.enable();
		}
	}

	private void exceptionEvent(ExceptionEvent event) {
		ThreadTrace trace = traceMap.get(event.thread());
		if (trace != null) { // only want threads we care about
			trace.exceptionEvent(event); // Forward event
		}
	}

	public void vmDeathEvent(VMDeathEvent event) {
		vmDied = true;
		// writer.println("-- The application exited --");
		System.out.println("-- The application exited --");
		System.out.println();
		System.out.println("-- Traced program output: --");
	}

	public void vmDisconnectEvent(VMDisconnectEvent event) {
		connected = false;
		if (!vmDied) {
			// writer.println("-- The application has been disconnected --");
			System.out.println("-- The application has been disconnected --");
		}
	}
}