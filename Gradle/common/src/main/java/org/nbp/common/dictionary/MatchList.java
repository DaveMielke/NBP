package org.nbp.common.dictionary;

import java.util.ArrayList;

public class MatchList extends ArrayList<MatchEntry> {
  public MatchList () {
    super();
  }

  public final void add (String word, String database) {
    add(new MatchEntry(word, database));
  }
}
