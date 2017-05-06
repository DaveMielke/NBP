package org.nbp.editor;

import java.util.Date;

import org.nbp.common.AlertDialogBuilder;
import android.app.AlertDialog;

import android.widget.TextView;

import android.text.SpannableStringBuilder;
import android.text.Spannable;
import android.text.style.CharacterStyle;

public abstract class RevisionSpan extends EditorSpan {
  private final CharSequence actualText;
  private final String decorationPrefix;
  private final String decorationSuffix;
  private final CharacterStyle revisionStyle;

  private final CharSequence decoratedText;

  protected RevisionSpan (CharSequence text, String prefix, String suffix, CharacterStyle style) {
    super();
    actualText = text;
    decorationPrefix = prefix;
    decorationSuffix = suffix;
    revisionStyle = style;

    SpannableStringBuilder sb = new SpannableStringBuilder();
    if (decorationPrefix != null) sb.append(decorationPrefix);
    int start = sb.length();
    sb.append(actualText);
    int end = sb.length();
    if (decorationSuffix != null) sb.append(decorationSuffix);
    if (revisionStyle != null) sb.setSpan(revisionStyle, start, end, sb.SPAN_INCLUSIVE_EXCLUSIVE);
    decoratedText = sb.subSequence(0, sb.length());
  }

  protected RevisionSpan (CharSequence text, String prefix, String suffix) {
    this(text, prefix, suffix, null);
  }

  protected RevisionSpan (CharSequence text, CharacterStyle style) {
    this(text, null, null, style);
  }

  public final CharSequence getActualText () {
    return actualText;
  }

  public final CharSequence getDecoratedText () {
    return decoratedText;
  }

  public final CharacterStyle getStyle () {
    return revisionStyle;
  }

  public final void decorateText (SpannableStringBuilder content) {
    content.replace(
      content.getSpanStart(this),
      content.getSpanEnd(this),
      decoratedText
    );
  }

  private String revisionAuthor = null;
  private Date revisionTimestamp = null;

  public final String getAuthor () {
    return revisionAuthor;
  }

  public final RevisionSpan setAuthor (String author) {
    revisionAuthor = author;
    return this;
  }

  public final Date getTimestamp () {
    return revisionTimestamp;
  }

  public final RevisionSpan setTimestamp (Date timestamp) {
    revisionTimestamp = timestamp;
    return this;
  }

  public final RevisionSpan setTimestamp () {
    return setTimestamp(new Date());
  }

  public abstract int getAction ();

  private final void setText (AlertDialog dialog, int id, CharSequence text) {
    if (text != null) ((TextView)dialog.findViewById(id)).setText(text);
  }

  public final void show (EditorActivity activity) {
    AlertDialog.Builder builder = activity.newAlertDialogBuilder(
      R.string.menu_revisions_showRevision
    );

    builder.setView(
      activity.getLayoutInflater().inflate(R.layout.revision, null)
    );

    builder.setNeutralButton(R.string.action_ok, null);
    AlertDialog dialog = builder.create();
    dialog.show();

    setText(dialog, R.id.revision_text, getActualText());
    setText(dialog, R.id.revision_action, activity.getString(getAction()));
    setText(dialog, R.id.revision_author, getAuthor());

    {
      Date timestamp = getTimestamp();
      if (timestamp != null) setText(dialog, R.id.revision_timestamp, timestamp.toString());
    }
  }
}
