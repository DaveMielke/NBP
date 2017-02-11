package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import java.io.File;

import org.nbp.common.FileMaker;
import org.nbp.common.AttachmentMaker;

public class SendScreen extends Action {
  private File makeLogFile () {
    FileScreenLogger logger = new FileScreenLogger();
    logger.logScreen();
    return logger.getFile();
  }

  @Override
  public boolean performAction () {
    DeveloperMessage message = new DeveloperMessage() {
      @Override
      protected final boolean containsSensitiveData () {
        return true;
      }

      @Override
      protected final String getSubject () {
        return "Screen content sent by user";
      }
    };

    if (message.isSendable()) {
      {
        File file = makeLogFile();
        if (file == null) return false;
        message.addAttachment(file, "a copy of your screen content");
      }

      if (!message.send()) return false;
    }

    return true;
  }

  public SendScreen (Endpoint endpoint) {
    super(endpoint, true);
  }
}
