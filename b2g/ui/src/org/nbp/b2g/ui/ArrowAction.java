package org.nbp.b2g.ui;

public abstract class ArrowAction extends Action {
  protected boolean performInputAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performSliderAction (Endpoint endpoint) {
    return false;
  }

  protected abstract Class<? extends Action> getNavigationAction ();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        return performInputAction(endpoint);
      }

      if (endpoint.isSlider()) {
        return performSliderAction(endpoint);
      }
    }

    return getEndpoint().performAction(getNavigationAction());
  }

  protected ArrowAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
