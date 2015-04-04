package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.ClipboardManager;
import android.content.ClipData;

import android.view.inputmethod.InputConnection;

public class CutToClipboard extends InputAction {
  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        String text = BrailleDevice.getSelectedText();

        if (text != null) {
          InputConnection connection = getInputConnection();

          if (connection != null) {
            if (copyToClipboard(text)) {
              if (deleteSelectedText(connection)) {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  public CutToClipboard () {
    super(false);
  }
}
