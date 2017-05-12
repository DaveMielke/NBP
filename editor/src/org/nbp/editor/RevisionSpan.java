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

public abstract class RevisionSpan extends RegionSpan {
  protected RevisionSpan (String prefix, String suffix, CharacterStyle style) {
    super(prefix, suffix, style);
  }

  protected RevisionSpan (String prefix, String suffix) {
    super(prefix, suffix);
  }

  protected RevisionSpan (CharacterStyle style) {
    super(style);
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

  private final static Map<String, Integer> authorColors =
               new HashMap<String, Integer>();

  public final static void reset () {
    authorColors.clear();
  }

  @Override
  protected final Integer getColor () {
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
