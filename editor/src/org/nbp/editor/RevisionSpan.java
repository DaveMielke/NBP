package org.nbp.editor;

import android.text.style.CharacterStyle;

import android.app.Dialog;
import android.widget.TextView;
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

  private final static void setText (Dialog dialog, int id, CharSequence text) {
    if (text != null) ((TextView)dialog.findViewById(id)).setText(text);
  }

  private final static void setText (Dialog dialog, int id, int text) {
    setText(dialog, id, dialog.getContext().getString(text));
  }

  @Override
  public final void finishDialog (Dialog dialog) {
    setText(dialog, R.id.revision_text, getActualText());
    setText(dialog, R.id.revision_type, getRevisionType());
    setText(dialog, R.id.revision_author, getAuthor());

    {
      Date timestamp = getTimestamp();
      if (timestamp != null) setText(dialog, R.id.revision_timestamp, timestamp.toString());
    }
  }
}
