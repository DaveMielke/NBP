package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

public class TabBackward extends TabForward {
  private final static int[] scanCodeModifiers = new int[] {
    SCAN_CODE_SHIFT
  };

  @Override
  protected int[] getScanCodeModifiers () {
    return scanCodeModifiers;
  }

  private final static int[] keyCodeModifiers = new int[] {
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
