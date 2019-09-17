package org.nbp.common.dictionary;

import java.util.ArrayList;

public class ItemList extends ArrayList<ItemEntry> {
  public ItemList () {
    super();
  }

  public final void add (String name, String description) {
    add(new ItemEntry(name, description));
  }
}
