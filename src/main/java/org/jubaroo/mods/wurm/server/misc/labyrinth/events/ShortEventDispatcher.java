package org.jubaroo.mods.wurm.server.misc.labyrinth.events;

import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShortEventDispatcher {
    private static ArrayList<EventOnce> events;
    private static Timer timer;
    private static boolean running;

    static {
        ShortEventDispatcher.events = new ArrayList<EventOnce>();
        ShortEventDispatcher.timer = null;
        ShortEventDispatcher.running = false;
    }

    private static void startPolling() {
        if (ShortEventDispatcher.timer != null) {
            RequiemLogging.logInfo("startPolling(): Poller is already running");
            return;
        }
        (ShortEventDispatcher.timer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                poll();
            }
        }, 50L, 1L);
        RequiemLogging.logInfo("stopPolling(): started");
    }

    private static void stopPolling() {
        if (ShortEventDispatcher.timer != null) {
            RequiemLogging.logInfo("stopPolling(): stopped");
            ShortEventDispatcher.timer.cancel();
            ShortEventDispatcher.timer = null;
            return;
        }
        RequiemLogging.logInfo("stopPolling(): Poller was not running. Why call this, eh?");
    }

    public static void add(final EventOnce event) {
        if (event.getOriginalMilliSecondDelay() > 10000L) {
            throw new RuntimeException("Short events cannot exceed 10 seconds");
        }
        ShortEventDispatcher.events.add(event);
        if (ShortEventDispatcher.timer == null) {
            startPolling();
        }
    }

    private static void poll() {
        if (ShortEventDispatcher.running) {
            return;
        }
        if (ShortEventDispatcher.events.size() == 0) {
            stopPolling();
            return;
        }
        ShortEventDispatcher.running = true;
        final ArrayList<EventOnce> executed = new ArrayList<EventOnce>();
        final long ts = System.currentTimeMillis();
        final EventOnce[] eventsCopy = ShortEventDispatcher.events.toArray(new EventOnce[0]);
        EventOnce[] array;
        for (int length = (array = eventsCopy).length, i = 0; i < length; ++i) {
            final EventOnce event = array[i];
            if (event.getInvokeAt() < ts && event.invoke()) {
                executed.add(event);
            }
        }
        if (executed.size() > 0) {
            ShortEventDispatcher.events.removeAll(executed);
        }
        ShortEventDispatcher.running = false;
    }
}
