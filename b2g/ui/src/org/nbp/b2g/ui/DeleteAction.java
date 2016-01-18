package org.nbp.b2g.ui;

public abstract class DeleteAction extends Action {
  protected abstract int getDeleteOffset ();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        return endpoint.deleteText(getDeleteOffset());
      }
    }

    return false;
  }

  protected DeleteAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
