package org.nbp.editor;

import android.text.style.CharacterStyle;
import java.util.Date;

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

  public final String getName () {
    return authorName;
  }

  public final void setName (String name) {
    authorName = name;
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

  @Override
  protected final Integer getForegroundColor () {
    return AuthorColors.get(getName());
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    super.finishDialog(helper);
    helper.setText(R.id.author_name, getName());
    helper.setText(R.id.author_initials, getInitials());

    {
      Date timestamp = getTimestamp();
      if (timestamp != null) helper.setText(R.id.author_timestamp, timestamp.toString());
    }
  }
}
