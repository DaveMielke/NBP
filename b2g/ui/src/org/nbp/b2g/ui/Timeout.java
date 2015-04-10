package org.nbp.b2g.ui;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Timeout {
  private final long delay;
  private Timer timer = null;

  protected abstract void run (Object argument);

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

  public void start (final Object argument) {
    synchronized (this) {
      cancel();

      TimerTask task = new TimerTask() {
        @Override
        public void run () {
          Timeout timeout = Timeout.this;

          synchronized (timeout) {
            timeout.cancel();
            timeout.run(argument);
          }
        }
      };

      timer = new Timer();
      timer.schedule(task, delay);
    }
  }

  public void start () {
    start(null);
  }
}
