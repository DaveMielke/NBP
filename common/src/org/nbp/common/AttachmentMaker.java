package org.nbp.common;

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
