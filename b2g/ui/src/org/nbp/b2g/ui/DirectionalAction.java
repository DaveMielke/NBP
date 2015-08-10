package org.nbp.b2g.ui;

public abstract class DirectionalAction extends Action {
  protected boolean performSliderAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performCursorAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performInternalAction () {
    return false;
  }

  protected abstract Class<? extends Action> getExternalAction ();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isSlider()) {
        return performSliderAction(endpoint);
      }

      if (endpoint.isInputArea()) {
        return performCursorAction(endpoint);
      }

      if ((endpoint.getLineStart() > 0) || (endpoint.getLineLength() < endpoint.getTextLength())) {
        if (performInternalAction()) {
          return true;
        }
      }
    }

    return endpoint.performAction(getExternalAction());
  }

  protected DirectionalAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
