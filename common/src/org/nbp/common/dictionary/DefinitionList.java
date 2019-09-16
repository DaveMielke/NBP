package org.nbp.common.dictionary;

import java.util.ArrayList;

public class DefinitionList extends ArrayList<DefinitionEntry> {
  public DefinitionList () {
    super();
  }

  public final void add (String word, String text, String name, String description) {
    add(new DefinitionEntry(word, text, name, description));
  }
}
