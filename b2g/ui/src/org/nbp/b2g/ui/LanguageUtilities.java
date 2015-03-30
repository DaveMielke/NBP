package org.nbp.b2g.ui;

public class LanguageUtilities {
  public static boolean canAssign (Class to, Class from) {
    return to.isAssignableFrom(from);
  }

  public static boolean canAssign (Class to, String from) {
    try {
      return canAssign(to, Class.forName(from));
    } catch (ClassNotFoundException exception) {
    }

    return false;
  }

  public static boolean canAssign (Class to, CharSequence from) {
    return canAssign(to, from.toString());
  }

  private LanguageUtilities () {
  }
}
