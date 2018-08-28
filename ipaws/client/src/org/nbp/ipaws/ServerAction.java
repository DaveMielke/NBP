package org.nbp.ipaws;

public abstract class ServerAction extends ApplicationComponent {
  public ServerAction () {
    super();
  }

  public abstract boolean perform (ServerSession session);

  public final boolean perform () {
    final ServerSession session = AlertService.getServerSession();
    if (session == null) return false;

    new Thread() {
      @Override
      public void run () {
        perform(session);
      }
    }.start();

    return true;
  }
}
