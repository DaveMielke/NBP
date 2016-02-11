package org.nbp.common;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Timeout implements Runnable {
  private final long defaultDelay;
  private final String timeoutName;

  private Timer timer = null;

  public String getName () {
    return timeoutName;
  }

  public Timeout (long delay, String name) {
    defaultDelay = delay;
    timeoutName = name;
  }

  public boolean isActive () {
    synchronized (this) {
      return timer != null;
    }
  }

  public boolean cancel () {
    synchronized (this) {
      if (!isActive()) return false;
      timer.cancel();
      timer = null;
      return true;
    }
  }

  public void start (long delay) {
    if (delay > 0) {
      synchronized (this) {
        cancel();

        final Timeout timeout = this;
        TimerTask task = new TimerTask() {
          @Override
          public void run () {
            final Timeout timeout = Timeout.this;

            synchronized (timeout) {
              timeout.cancel();
              timeout.run();
            }
          }
        };

        timer = new Timer();
        timer.schedule(task, delay);
      }
    }
  }

  public void start () {
    start(defaultDelay);
  }
}
