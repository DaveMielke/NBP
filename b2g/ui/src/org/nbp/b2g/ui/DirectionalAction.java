package org.nbp.b2g.ui;

public abstract class DirectionalAction extends Action {
  protected boolean performSliderAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performCursorAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performInternalAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performExternalAction (Endpoint endpoint) {
    return false;
  }

  protected Class<? extends Action> getExternalAction () {
    return null;
  }

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
        if (performInternalAction(endpoint)) {
          return true;
        }
      }

      if (performExternalAction(endpoint)) return true;
    }

    {
      Class<? extends Action> action = getExternalAction();
      if (action != null) return endpoint.performAction(action);
    }

    return false;
  }

  protected int getBrailleLength () {
    return Devices.braille.get().getLength();
  }

  protected DirectionalAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
