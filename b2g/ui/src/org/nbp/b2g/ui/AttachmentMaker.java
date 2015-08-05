package org.nbp.b2g.ui;

import java.io.File;

public abstract class AttachmentMaker extends FileMaker {
  @Override
  protected final boolean setAttributes (File file) {
    return file.setReadable(true, false);
  }

  public AttachmentMaker () {
    super();
  }
}
