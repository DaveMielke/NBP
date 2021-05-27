package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

public abstract class ReviewerColors {
  private ReviewerColors () {
  }

  private final static Map<String, Integer> reviewerColors =
               new HashMap<String, Integer>();

  public final static void reset () {
    reviewerColors.clear();
  }

  public final static Integer get (String reviewer) {
    if (reviewer == null) return null;
    if (reviewer.isEmpty()) return null;
    Integer color = reviewerColors.get(reviewer);

    if (color == null) {
      int[] colors = ApplicationParameters.REVIEWER_COLORS;
      int index = Math.min(reviewerColors.size(), colors.length-1);
      color = colors[index];
      reviewerColors.put(reviewer, color);
    }

    return color;
  }
}
