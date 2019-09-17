package org.nbp.common.dictionary;

public class DescriptionEntry {
  private final String itemName;
  private final String itemText;

  public DescriptionEntry (String name, String text) {
    itemName = name;
    itemText = text;
  }

  public final String getName () {
    return itemName;
  }

  public final String getText () {
    return itemText;
  }
}
