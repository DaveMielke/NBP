package org.nbp.editor;
import org.nbp.editor.controls.*;

import org.nbp.common.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static ProtectTextControl protectText = new ProtectTextControl();
  public final static ReviewerNameControl reviewerName = new ReviewerNameControl();

  public final static Control[] ALL = new Control[] {
    protectText,
    reviewerName
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
