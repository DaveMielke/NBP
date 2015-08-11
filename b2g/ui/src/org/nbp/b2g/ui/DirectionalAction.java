package org.nbp.b2g.ui;

import android.util.Log;

public abstract class DirectionalAction extends Action {
  private final static String LOG_TAG = DirectionalAction.class.getName();

  protected enum ActionResult {
    DONE,
    WRITE,
    FAILED,
    NEXT
  }

  private interface ActionPerformer {
    public abstract ActionResult performAction (Endpoint endpoint);
  }

  protected ActionResult performSliderAction (Endpoint endpoint) {
    return ActionResult.FAILED;
  }

  protected ActionResult performCursorAction (Endpoint endpoint) {
    return ActionResult.FAILED;
  }

  protected ActionResult performInternalAction (Endpoint endpoint) {
    return ActionResult.FAILED;
  }

  protected Class<? extends Action> getExternalAction () {
    return null;
  }

  private final ActionPerformer[] actionPerformers = new ActionPerformer[] {
    new ActionPerformer() {
      @Override
      public final ActionResult performAction (Endpoint endpoint) {
        if (!endpoint.isSlider()) return ActionResult.NEXT;
        return performSliderAction(endpoint);
      }
    },

    new ActionPerformer() {
      @Override
      public final ActionResult performAction (Endpoint endpoint) {
        if (!endpoint.isInputArea()) return ActionResult.NEXT;
        return performCursorAction(endpoint);
      }
    },

    new ActionPerformer() {
      @Override
      public final ActionResult performAction (Endpoint endpoint) {
        ActionResult result = performInternalAction(endpoint);
        if (result == ActionResult.FAILED) result = ActionResult.NEXT;
        return result;
      }
    }
  };

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();
    boolean write = false;

    synchronized (endpoint) {
    ACTION_PERFORMER_LOOP:
      for (ActionPerformer actionPerformer : actionPerformers) {
        ActionResult result = actionPerformer.performAction(endpoint);

        switch (result) {
          case DONE:
            return true;

          case WRITE:
            write = true;
            break ACTION_PERFORMER_LOOP;

          default:
            Log.w(LOG_TAG, "unsupported action result: " + result.name());
          case FAILED:
            return false;

          case NEXT:
            continue ACTION_PERFORMER_LOOP;
        }
      }
    }

    if (write) return endpoint.write();
    Class<? extends Action> action = getExternalAction();
    if (action == null) return false;
    return endpoint.performAction(action);
  }

  protected int getBrailleLength () {
    return Devices.braille.get().getLength();
  }

  protected DirectionalAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
