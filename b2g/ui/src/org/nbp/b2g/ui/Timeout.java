package org.nbp.b2g.ui;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Timeout implements Runnable {
  private final long delay;
  private Timer timer = null;

  public Timeout (long delay) {
    this.delay = delay;
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

  public void start () {
    if (delay > 0) {
      synchronized (this) {
        cancel();

        TimerTask task = new TimerTask() {
          @Override
          public void run () {
            Timeout timeout = Timeout.this;

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
}
