package org.nbp.editor;

import java.io.File;
import android.text.SpannableStringBuilder;

public interface ContentOperations {
  public void read (File file, SpannableStringBuilder input);
  public void write (File file, CharSequence output);
}
