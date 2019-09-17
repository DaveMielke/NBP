package org.nbp.common.dictionary;

import java.util.ArrayList;

public class DescriptionList extends ArrayList<DescriptionEntry> {
  public DescriptionList () {
    super();
  }

  public final void add (String name, String text) {
    add(new DescriptionEntry(name, text));
  }
}
