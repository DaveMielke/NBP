package org.nbp.b2g.ui;

import android.util.Log;

public abstract class DirectionalAction extends Action {
  private final static String LOG_TAG = DirectionalAction.class.getName();

  protected enum ActionResult {
    NEXT,
    DONE,
    WRITE,
    FAILED
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

    synchronized (endpoint) {
      for (ActionPerformer actionPerformer : actionPerformers) {
        ActionResult result = actionPerformer.performAction(endpoint);

        switch (result) {
          case NEXT:
            continue;

          case DONE:
            return true;

          case WRITE:
            return endpoint.write();

          default:
            Log.w(LOG_TAG, "unsupported action result: " + result.name());
          case FAILED:
            return false;
        }
      }
    }

    Class<? extends Action> action = getExternalAction();
    if (action == null) return false;
    return endpoint.performAction(action);
  }

  protected int getBrailleLength () {
    return Devices.braille.get().getLength();
  }

  protected DirectionalAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
