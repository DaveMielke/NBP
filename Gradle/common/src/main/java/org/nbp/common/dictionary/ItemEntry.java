package org.nbp.common.dictionary;

public class ItemEntry {
  private final String itemName;
  private final String itemDescription;

  public ItemEntry (String name, String description) {
    itemName = name;
    itemDescription = description;
  }

  public final String getName () {
    return itemName;
  }

  public final String getDescription () {
    return itemDescription;
  }
}
