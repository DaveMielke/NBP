package org.nbp.b2g.ui;

public abstract class InputAction extends ScanCodeAction {
  protected int getOffset (int cursorKey) {
    return BrailleDevice.getIndent() + cursorKey;
  }

  protected boolean isCharacterOffset (int offset) {
    return ((offset >= 0) && (offset < BrailleDevice.getLength()));
  }

  protected boolean isCursorOffset (int offset) {
    return ((offset >= 0) && (offset <= BrailleDevice.getLength()));
  }

  protected InputAction () {
    super();
  }
}
