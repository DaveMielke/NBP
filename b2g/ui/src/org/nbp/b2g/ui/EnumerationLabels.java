package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

public abstract class EnumerationLabels {
  private final static Map<Enum, Integer> labels = new HashMap<Enum, Integer>();

  public static String getLabel (Enum value) {
    Integer label = labels.get(value);
    if (label == null) return null;
    return ApplicationContext.getString(label);
  }

  public static void setLabel (Enum value, int label) {
    labels.put(value, label);
  }

  private EnumerationLabels () {
  }
}
