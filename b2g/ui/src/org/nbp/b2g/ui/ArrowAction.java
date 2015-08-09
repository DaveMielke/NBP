package org.nbp.b2g.ui;

public abstract class ArrowAction extends Action {
  protected boolean performSliderAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performInputAction (Endpoint endpoint) {
    return false;
  }

  protected Class<? extends Action> getMoveAction () {
    return null;
  }

  protected abstract Class<? extends Action> getNavigationAction ();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isSlider()) {
        return performSliderAction(endpoint);
      }

      if (endpoint.isInputArea()) {
        return performInputAction(endpoint);
      }

      if ((endpoint.getLineStart() > 0) || (endpoint.getLineLength() < endpoint.getTextLength())) {
        Class<? extends Action> action = getMoveAction();

        if (action != null) {
          if (endpoint.performAction(action)) {
            return true;
          }
        }
      }
    }

    return endpoint.performAction(getNavigationAction());
  }

  protected ArrowAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
