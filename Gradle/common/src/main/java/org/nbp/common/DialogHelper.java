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

  public final void setText (int view, CharSequence... strings) {
    if (strings != null) {
      for (CharSequence string : strings) {
        if (string == null) continue;

        int from = 0;
        int to = string.length();

        while (to > from) {
          int last = to - 1;
          if (!Character.isWhitespace(string.charAt(last))) break;
          to = last;
        }

        for (int index=from; index<to; index+=1) {
          char character = string.charAt(index);

          if (character == '\n') {
            from = index + 1;
          } else if (!Character.isWhitespace(character)) {
            break;
          }
        }

        if (from < to) {
          if ((from > 0) || (to < string.length())) {
            string = string.subSequence(from, to);
          }

          ((TextView)findViewById(view)).setText(string);
          break;
        }
      }
    }
  }

  public final void setText (int view, int resource) {
    setText(view, dialog.getContext().getString(resource));
  }

  public final void setTextFromAsset (int view, String name) {
    final StringBuilder text = new StringBuilder();

    new InputProcessor() {
      @Override
      protected boolean handleLine (CharSequence line, int number) {
        if (text.length() > 0) text.append('\n');
        text.append(line);
        return true;
      }
    }.processInput(name);

    setText(view, text.toString());
  }

  public final void setValue (int view, boolean value) {
    setText(view, Boolean.toString(value));
  }

  public final void setValue (int view, int value) {
    setText(view, Integer.toString(value));
  }

  public final void setValue (int view, long value) {
    setText(view, Long.toString(value));
  }

  public final void setValue (int view, float value) {
    setText(view, Float.toString(value));
  }

  public final void setValue (int view, double value) {
    setText(view, Double.toString(value));
  }
}
