package org.nbp.b2g.ui;

public class Context {
  private static Context currentContext = new HostContext();
  private final static Object LOCK = new Object();

  public static Context getCurrentContext () {
    synchronized (LOCK) {
      return currentContext;
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
        keyBindings = new KeyBindings();
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

  public Context () {
  }
}
