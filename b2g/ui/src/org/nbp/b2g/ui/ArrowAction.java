package org.nbp.b2g.ui;

public abstract class ArrowAction extends ScanCodeAction {
  protected boolean performEditAction (Endpoint endpoint) {
    return false;
  }

  protected boolean performSeekAction (Endpoint endpoint) {
    return false;
  }

  protected String getNavigationAction () {
    return null;
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        return performEditAction(endpoint);
      }

      if (endpoint.isSeekable()) {
        return performSeekAction(endpoint);
      }
    }

    {
      String name = getNavigationAction();

      if (name != null) {
        Action action = endpoint.getKeyBindings().getAction(name);
        if (action == null) return false;
        return KeyEvents.performAction(action, false);
      }
    }

    return super.performAction();
  }

  protected ArrowAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
