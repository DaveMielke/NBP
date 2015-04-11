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

  public static String getClassName (String name) {
    int index = name.lastIndexOf('.');
    if (index == -1) return name;
    return name.substring(index+1);
  }

  public static String getClassName (CharSequence name) {
    return getClassName(name.toString());
  }

  public static String getClassName (Class type) {
    return getClassName(type.getName());
  }

  private LanguageUtilities () {
  }
}
