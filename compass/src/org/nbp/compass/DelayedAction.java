package org.nbp.compass;

import org.nbp.common.Timeout;

public class DelayedAction extends Timeout {
  public DelayedAction (long delay, String name) {
    super(delay, name);
  }

  private Runnable latestAction = null;
  private Runnable currentAction = null;

  @Override
  public void run () {
    currentAction = latestAction;
    latestAction = null;

    if (currentAction != null) {
      currentAction.run();
      start();
    }
  }

  public final void setAction (Runnable action) {
    synchronized (this) {
      latestAction = action;
      if (currentAction == null) run();
    }
  }
}
