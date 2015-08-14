package org.nbp.b2g.ui;

import android.view.accessibility.AccessibilityNodeInfo;

import android.text.SpannableStringBuilder;
import android.graphics.Typeface;

public enum ScrollDirection {
  FORWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD,

    new byte[] {
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8,
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4
    },

    R.string.message_scrolling_forward
  ),

  BACKWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD,

    new byte[] {
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4,
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8
    },

    R.string.message_scrolling_backward
  );

  private final int nodeAction;
  private final byte[] brailleSymbol;
  private final CharSequence monitorText;

  public final int getNodeAction () {
    return nodeAction;
  }

  public final byte[] getBrailleSymbol () {
    return brailleSymbol;
  }

  public boolean writeBrailleSymbol () {
    return Devices.braille.get().write(getBrailleSymbol(), monitorText);
  }

  private ScrollDirection (int nodeAction, byte[] braille, int text) {
    this.nodeAction = nodeAction;
    brailleSymbol = braille;

    {
      SpannableStringBuilder sb = new SpannableStringBuilder(ApplicationContext.getString(text));
      sb.setSpan(Typeface.ITALIC, 0, sb.length(), 0);
      monitorText = sb;
    }
  }
}
