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
}
