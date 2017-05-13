package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

public abstract class AuthorColors {
  private AuthorColors () {
  }

  private final static Map<String, Integer> authorColors =
               new HashMap<String, Integer>();

  public final static void reset () {
    authorColors.clear();
  }

  public final static Integer get (String author) {
    if (author == null) return null;
    if (author.isEmpty()) return null;
    Integer color = authorColors.get(author);

    if (color == null) {
      int[] colors = ApplicationParameters.REVISION_AUTHOR_COLORS;
      int index = Math.min(authorColors.size(), colors.length-1);
      color = colors[index];
      authorColors.put(author, color);
    }

    return color;
  }
}
