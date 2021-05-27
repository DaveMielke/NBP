package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;
import android.text.Editable;

public abstract class ContentOperations extends DocumentComponent {
  private final boolean contentCanContainMarkup;

  protected ContentOperations (boolean canContainMarkup) {
    super();
    contentCanContainMarkup = canContainMarkup;
  }

  public final boolean canContainMarkup () {
    return contentCanContainMarkup;
  }

  protected final void readingNotSupported () throws IOException {
    throw new IOException("reading not supported");
  }

  public void read (InputStream stream, Editable content) throws IOException {
    readingNotSupported();
  }

  protected final void writingNotSupported () throws IOException {
    throw new IOException("writing not supported");
  }

  public void write (OutputStream stream, CharSequence content) throws IOException {
    writingNotSupported();
  }
}
