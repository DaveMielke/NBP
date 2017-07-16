package org.nbp.common;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Timeout implements Runnable {
  private final long defaultDelay;
  private final String timeoutName;

  public Timeout (long delay, String name) {
    defaultDelay = delay;
    timeoutName = name;
  }

  public String getName () {
    return timeoutName;
  }

  private Timer timerObject = null;
  private static int timerIdentifier = 0;

  public boolean isActive () {
    synchronized (this) {
      return timerObject != null;
    }
  }

  public boolean cancel () {
    synchronized (this) {
      if (!isActive()) return false;

      timerObject.cancel();
      timerObject = null;
      return true;
    }
  }

  public void start (long delay) {
    if (delay > 0) {
      synchronized (this) {
        cancel();

        final Timeout timeout = this;
        final int identifier = ++timerIdentifier;

        TimerTask task = new TimerTask() {
          @Override
          public void run () {
            synchronized (timeout) {
              if (timeout.isActive()) {
                if (identifier == timeout.timerIdentifier) {
                  timeout.cancel();
                  timeout.run();
                }
              }
            }
          }
        };

        timerObject = new Timer();
        timerObject.schedule(task, delay);
      }
    }
  }

  public void start () {
    start(defaultDelay);
  }
}
