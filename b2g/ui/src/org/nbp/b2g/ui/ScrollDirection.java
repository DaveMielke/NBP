package org.nbp.b2g.ui;

import android.view.accessibility.AccessibilityNodeInfo;

public enum ScrollDirection {
  FORWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD,
    new byte[] {
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8,
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4
    }
  ),

  BACKWARD(
    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD,
    new byte[] {
      BrailleDevice.DOT_7 | BrailleDevice.DOT_6,
      BrailleDevice.DOT_2 | BrailleDevice.DOT_4,
      BrailleDevice.DOT_1 | BrailleDevice.DOT_5,
      BrailleDevice.DOT_3 | BrailleDevice.DOT_8
    }
  );

  private final int nodeAction;
  private final byte[] brailleSymbol;

  public final int getNodeAction () {
    return nodeAction;
  }

  public final byte[] getBrailleSymbol () {
    return brailleSymbol;
  }

  private ScrollDirection (int nodeAction, byte[] brailleSymbol) {
    this.nodeAction = nodeAction;
    this.brailleSymbol = brailleSymbol;
  }
}
