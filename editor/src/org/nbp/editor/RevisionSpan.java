package org.nbp.editor;

import android.text.style.CharacterStyle;
import java.util.Date;

public abstract class RevisionSpan extends AuthorSpan implements DialogFinisher {
  protected RevisionSpan (String prefix, String suffix, CharacterStyle style) {
    super(prefix, suffix, style);
  }

  protected RevisionSpan (String prefix, String suffix) {
    super(prefix, suffix);
  }

  protected RevisionSpan (CharacterStyle style) {
    super(style);
  }

  public abstract int getRevisionType ();

  @Override
  public final void finishDialog (DialogHelper helper) {
    helper.setText(R.id.revision_text, getActualText());
    helper.setText(R.id.revision_type, getRevisionType());
    helper.setText(R.id.revision_author, getAuthor());

    {
      Date timestamp = getTimestamp();
      if (timestamp != null) helper.setText(R.id.revision_timestamp, timestamp.toString());
    }
  }
}
