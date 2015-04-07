package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;

public class Endpoint {
  private static Endpoint currentEndpoint = new HostEndpoint();
  private final static Object LOCK = new Object();

  public static Endpoint getCurrentEndpoint () {
    synchronized (LOCK) {
      return currentEndpoint;
    }
  }

  private KeyBindings keyBindings = null;
  private final Characters characters = new Characters();

  protected String[] getKeysFileNames () {
    return null;
  }

  private void ensureKeyBindings () {
    synchronized (this) {
      if (keyBindings == null) {
        keyBindings = new KeyBindings(this);
        keyBindings.addKeyBindings(getKeysFileNames());
      }
    }
  }

  public KeyBindings getKeyBindings () {
    ensureKeyBindings();
    return keyBindings;
  }

  public Characters getCharacters () {
    ensureKeyBindings();
    return characters;
  }

  public Endpoint () {
  }
}
