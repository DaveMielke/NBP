package org.nbp.b2g.input;

public class TabBackwardAction extends TabForwardAction {
  private static final int[] scanCodeModifiers = new int[] {
    SCAN_CODE_SHIFT
  };

  @Override
  protected int[] getScanCodeModifiers () {
    return scanCodeModifiers;
  }

  private static final int[] keyCodeModifiers = new int[] {
    KEY_CODE_SHIFT
  };

  @Override
  protected int[] getKeyCodeModifiers () {
    return keyCodeModifiers;
  }

  public TabBackwardAction () {
    super();
  }
}
