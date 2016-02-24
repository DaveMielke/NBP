package org.nbp.editor;

import java.io.InputStream;
import java.io.OutputStream;

import android.text.SpannableStringBuilder;

public interface ContentOperations {
  public boolean read (InputStream stream, SpannableStringBuilder content);
  public boolean write (OutputStream stream, CharSequence content);
}
