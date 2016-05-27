package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public enum ScrollDirection {
  FORWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD,

    new byte[] {
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8,
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4
    },

    R.string.message_scroll_forward
  ),

  BACKWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD,

    new byte[] {
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4,
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8
    },

    R.string.message_scroll_backward
  );

  private final int nodeAction;
  private final CharSequence brailleText;

  public final int getNodeAction () {
    return nodeAction;
  }

  public boolean writeBrailleSymbol () {
    return Devices.braille.get().write(brailleText);
  }

  private ScrollDirection (int action, byte[] braille, int text) {
    nodeAction = action;

    StringBuilder sb = new StringBuilder();
    sb.append(Braille.toString(braille));

    sb.append(' ');
    sb.append(ApplicationContext.getString(text));

    brailleText = sb.subSequence(0, sb.length());
  }
}
