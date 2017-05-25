package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import android.view.MenuItem;
import org.nbp.common.OutgoingMessage;

public class Send extends EditorAction {
  public Send () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    ContentHandle contentHandle = editor.getEditArea().getContentHandle();

    if (contentHandle != null) {
      OutgoingMessage message = new OutgoingMessage();
      message.addAttachment(contentHandle.getUri());

      if (message.send()) {
      }
    } else {
      editor.showMessage(R.string.message_send_new);
    }
  }
}
