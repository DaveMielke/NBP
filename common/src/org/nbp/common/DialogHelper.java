package org.nbp.common;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

public class DialogHelper {
  private final Dialog dialog;

  public DialogHelper (Dialog dialog) {
    this.dialog = dialog;
  }

  public final Dialog getDialog () {
    return dialog;
  }

  public final View findViewById (int view) {
    return getDialog().findViewById(view);
  }

  public final void setText (int view, CharSequence text) {
    if (text != null) ((TextView)findViewById(view)).setText(text);
  }

  public final void setText (int view, int text) {
    setText(view, dialog.getContext().getString(text));
  }

  public final void setValue (int view, int value) {
    setText(view, Integer.toString(value));
  }
}
