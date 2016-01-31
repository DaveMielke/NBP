package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

public class Wordify {
  private final static Map<String, String> map = new HashMap<String, String>();

  public static String get (String identifier) {
    synchronized (map) {
      String text = map.get(identifier);
      if (text != null) return text;

      text = identifier.replaceAll("(?<=\\p{Lower})()(?=\\p{Upper})", " ");
      map.put(identifier, text);
      return text;
    }
  }
}
