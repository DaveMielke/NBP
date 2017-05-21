package org.nbp.editor;

import android.app.Dialog;
import android.widget.TextView;

public class DialogHelper {
  private final Dialog dialog;

  public DialogHelper (Dialog dialog) {
    this.dialog = dialog;
  }

  public final Dialog getDialog () {
    return dialog;
  }

  public final void setText (int view, CharSequence text) {
    if (text != null) ((TextView)dialog.findViewById(view)).setText(text);
  }

  public final void setText (int view, int text) {
    setText(view, dialog.getContext().getString(text));
  }

  public final void setValue (int view, int value) {
    setText(view, Integer.toString(value));
  }

  public final void setValue (int view, boolean value) {
    setText(view, (value? R.string.value_boolean_true: R.string.value_boolean_false));
  }
}
