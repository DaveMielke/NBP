package org.nbp.editor;
import org.nbp.editor.controls.*;

import org.nbp.common.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static ProtectTextControl protectText = new ProtectTextControl();
  public final static OwnerNameControl ownerName = new OwnerNameControl();

  public final static Control[] ALL = new Control[] {
    protectText,
    ownerName
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
