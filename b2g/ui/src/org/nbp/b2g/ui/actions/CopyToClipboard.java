package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.ClipboardManager;
import android.content.ClipData;

public class CopyToClipboard extends InputAction {
  @Override
  public boolean performAction () {
    String text = BrailleDevice.getSelectedText();

    if (text != null) {
      if (copyToClipboard(text)) {
        return true;
      }
    }

    return false;
  }

  public CopyToClipboard () {
    super();
  }
}
