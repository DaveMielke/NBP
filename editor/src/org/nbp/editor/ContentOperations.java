package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.text.SpannableStringBuilder;

public interface ContentOperations {
  public void read (InputStream stream, SpannableStringBuilder content) throws IOException;
  public void write (OutputStream stream, CharSequence content) throws IOException;
}
