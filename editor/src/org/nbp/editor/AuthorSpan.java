package org.nbp.editor;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import android.text.style.CharacterStyle;

public abstract class AuthorSpan extends RegionSpan {
  protected AuthorSpan (String prefix, String suffix, CharacterStyle style) {
    super(prefix, suffix, style);
  }

  protected AuthorSpan (String prefix, String suffix) {
    super(prefix, suffix);
  }

  protected AuthorSpan (CharacterStyle style) {
    super(style);
  }

  private String authorName = null;
  private String authorInitials = null;
  private Date modificationTimestamp = null;

  public final String getAuthor () {
    return authorName;
  }

  public final void setAuthor (String author) {
    authorName = author;
  }

  public final String getInitials () {
    return authorInitials;
  }

  public final void setInitials (String initials) {
    authorInitials = initials;
  }

  public final Date getTimestamp () {
    return modificationTimestamp;
  }

  public final void setTimestamp (Date timestamp) {
    modificationTimestamp = timestamp;
  }

  public final void setTimestamp () {
    setTimestamp(new Date());
  }

  private final static Map<String, Integer> authorColors =
               new HashMap<String, Integer>();

  public final static void reset () {
    authorColors.clear();
  }

  @Override
  protected final Integer getForegroundColor () {
    String author = getAuthor();
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
