package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

public class TabBackward extends TabForward {
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

  public TabBackward () {
    super();
  }
}
