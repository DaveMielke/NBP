package org.nbp.common;

import java.io.File;

import android.text.SpannableStringBuilder;

public class DefaultFileHandler extends FileHandler {
  @Override
  protected final void read (File file, SpannableStringBuilder sb) {
  }

  @Override
  protected final void write (File file, SpannableStringBuilder sb) {
  }

  public DefaultFileHandler () {
    super();
  }
}
