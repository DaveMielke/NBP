package org.nbp.editor;
import org.nbp.editor.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  // general settings
  public final static ProtectTextControl protectText = new ProtectTextControl();
  public final static SizeLimitControl sizeLimit = new SizeLimitControl();

  // braille settings
  public final static BrailleModeControl brailleMode = new BrailleModeControl();
  public final static BrailleCodeControl brailleCode = new BrailleCodeControl();

  // document settings
  public final static AuthorNameControl authorName = new AuthorNameControl();

  public final static Control[] inCreationOrder = Control.getControlsInCreationOrder();
  public final static Control[] inRestoreOrder = Control.getControlsInRestoreOrder();

  public static void restore () {
    Control.restoreCurrentValues(inRestoreOrder);
  }
}
