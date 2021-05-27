package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import org.nbp.common.OutgoingMessage;

public class SendCurrent extends EditorAction {
  public SendCurrent (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    ContentHandle contentHandle = editor.getEditArea().getContentHandle();

    if (contentHandle != null) {
      OutgoingMessage message = new OutgoingMessage();
      message.addAttachment(contentHandle.getUri());

      if (message.send()) {
      }
    } else {
      showMessage(R.string.message_send_new);
    }
  }
}
