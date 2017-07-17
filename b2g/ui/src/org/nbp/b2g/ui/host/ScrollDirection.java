package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import org.nbp.common.Braille;

import android.view.accessibility.AccessibilityNodeInfo;

public enum ScrollDirection {
  FORWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD,

    new byte[] {
      Braille.CELL_DOT_1 | Braille.CELL_DOT_5,
      Braille.CELL_DOT_3 | Braille.CELL_DOT_8,
      Braille.CELL_DOT_7 | Braille.CELL_DOT_6,
      Braille.CELL_DOT_2 | Braille.CELL_DOT_4
    },

    R.string.message_scroll_forward
  ),

  BACKWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD,

    new byte[] {
      Braille.CELL_DOT_7 | Braille.CELL_DOT_6,
      Braille.CELL_DOT_2 | Braille.CELL_DOT_4,
      Braille.CELL_DOT_1 | Braille.CELL_DOT_5,
      Braille.CELL_DOT_3 | Braille.CELL_DOT_8
    },

    R.string.message_scroll_backward
  );

  private final int nodeAction;
  private final CharSequence actionText;

  public final int getNodeAction () {
    return nodeAction;
  }

  public boolean writeActionText () {
    return Devices.braille.get().write(actionText);
  }

  private ScrollDirection (int action, byte[] symbol, int text) {
    nodeAction = action;

    {
      StringBuilder sb = new StringBuilder();
      sb.append(Braille.toString(symbol));
      sb.append(' ');
      sb.append(ApplicationContext.getString(text));
      actionText = sb.subSequence(0, sb.length());
    }
  }
}
