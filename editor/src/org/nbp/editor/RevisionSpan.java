package org.nbp.editor;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.nbp.common.AlertDialogBuilder;
import android.app.AlertDialog;

import android.widget.TextView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

public abstract class RevisionSpan extends EditorSpan {
  private final String decorationPrefix;
  private final String decorationSuffix;
  private final CharacterStyle characterStyle;

  protected RevisionSpan (String prefix, String suffix, CharacterStyle style) {
    super();
    decorationPrefix = prefix;
    decorationSuffix = suffix;
    characterStyle = style;
  }

  protected RevisionSpan (String prefix, String suffix) {
    this(prefix, suffix, null);
  }

  protected RevisionSpan (CharacterStyle style) {
    this(null, null, style);
  }

  public final String getDecorationPrefix () {
    return decorationPrefix;
  }

  public final String getDecorationSuffix () {
    return decorationSuffix;
  }

  public final CharacterStyle getCharacterStyle () {
    return characterStyle;
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

  private final static int[] authorColorArray = new int[] {
    Color.RED,
    Color.BLUE
  };

  private final static Map<String, Integer> authorColorMap =
               new HashMap<String, Integer>();

  public final static void reset () {
    authorColorMap.clear();
  }

  private final void setColor (SpannableStringBuilder content, int start, int end) {
    String author = getAuthor();
    Integer color = authorColorMap.get(author);

    if (color == null) {
      int index = Math.min(authorColorMap.size(), authorColorArray.length-1);
      color = authorColorArray[index];
      authorColorMap.put(author, color);
    }

    content.setSpan(new ForegroundColorSpan(color), start, end, content.SPAN_INCLUSIVE_EXCLUSIVE);
  }

  private CharSequence actualText;
  private CharSequence decoratedText;

  public final CharSequence getActualText () {
    return actualText;
  }

  public final CharSequence getDecoratedText () {
    return decoratedText;
  }

  private final void appendDecoration (SpannableStringBuilder sb, String decoration) {
    if (decoration != null) {
      if (!decoration.isEmpty()) {
        int start = sb.length();
        sb.append(decoration);
        int end = sb.length();
        sb.setSpan(new DecorationSpan(), start, end, sb.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
  }

  public final void decorateText (SpannableStringBuilder content) {
    int spanStart = content.getSpanStart(this);
    int spanEnd = content.getSpanEnd(this);
    actualText = content.subSequence(spanStart, spanEnd);

    SpannableStringBuilder sb = new SpannableStringBuilder();
    appendDecoration(sb, decorationPrefix);
    int textStart = sb.length();
    sb.append(actualText);
    int textEnd = sb.length();
    appendDecoration(sb, decorationSuffix);

    if (characterStyle != null) sb.setSpan(characterStyle, textStart, textEnd, sb.SPAN_INCLUSIVE_EXCLUSIVE);
    setColor(sb, textStart, textEnd);

    decoratedText = sb.subSequence(0, sb.length());
    content.replace(spanStart, spanEnd, decoratedText);
  }

  public final void restoreData (Spannable text) {
    int start = text.getSpanStart(this);
    int end = text.getSpanEnd(this);
    decoratedText = text.subSequence(start, end);

    if (decorationPrefix != null) start += decorationPrefix.length();
    if (decorationSuffix != null) end -= decorationSuffix.length();
    actualText = text.subSequence(start, end);
  }

  public abstract int getAction ();

  private final static void setText (AlertDialog dialog, int id, CharSequence text) {
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
