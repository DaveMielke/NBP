package org.nbp.b2g.ui;

public abstract class InputAction extends Action {
  protected abstract boolean performInputAction (Endpoint endpoint);

  protected boolean performNonInputAction (Endpoint endpoint) {
    return false;
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      return endpoint.isInputArea()? 
             performInputAction(endpoint):
             performNonInputAction(endpoint);
    }
  }

  protected InputAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
