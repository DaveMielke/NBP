package org.nbp.editor;
import org.nbp.editor.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static ProtectTextControl protectText = new ProtectTextControl();
  public final static BrailleModeControl brailleMode = new BrailleModeControl();
  public final static AuthorNameControl authorName = new AuthorNameControl();

  public final static Control[] ALL = new Control[] {
    protectText,
    brailleMode,
    authorName
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
