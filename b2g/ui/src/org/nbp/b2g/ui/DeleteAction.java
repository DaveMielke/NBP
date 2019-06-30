package org.nbp.b2g.ui;

import android.util.Log;

import android.text.SpannableStringBuilder;

public abstract class DeleteAction extends Action {
  private final static String LOG_TAG = DeleteAction.class.getName();

  protected abstract int getDeleteOffset ();

  private final boolean deleteCharacter (Endpoint endpoint, int offset) {
    int start = endpoint.getSelectionStart();
    int end = endpoint.getSelectionEnd();

    if (!Endpoint.isSelection(start, end)) {
      if (!Endpoint.isSelected(end)) return false;
      start = end + offset;
      end = start + 1;

      if (start < 0) return false;
      if (end > endpoint.getTextLength()) return false;
      if (!endpoint.isSelectable(start)) return false;
    }

    if (start == end) return true;

    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format(
        "deleting text: %s", endpoint.getText().subSequence(start, end)
      ));
    }

    return endpoint.deleteText(start, end);
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int offset = endpoint.getSelectionStart() + getDeleteOffset();
        endpoint.adjustScroll(offset);
        offset -= endpoint.getLineStart();

        int start = endpoint.findFirstBrailleOffset(offset);
        int end = endpoint.findFirstBrailleOffset(offset+1);

        CharSequence braille = endpoint.isHintText()? "": endpoint.getBrailleCharacters();
        SpannableStringBuilder sb = new SpannableStringBuilder(braille);
        sb.delete(start, end);
        return endpoint.setBrailleCharacters(sb.subSequence(0, sb.length()), start);
      }
    }

    return false;
  }

  protected DeleteAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
