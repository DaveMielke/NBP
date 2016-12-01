package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;

import android.text.SpannableStringBuilder;

public class ByteOperations extends ContentOperations {
  protected void beginBytes (SpannableStringBuilder content) {
  }

  protected int processBytes (SpannableStringBuilder content, byte[] buffer, int count) {
    return count;
  }

  protected void endBytes (SpannableStringBuilder content) {
  }

  @Override
  public final void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    beginBytes(content);

    byte[] buffer = new byte[0X1000];
    int from = 0;

    while (true) {
      int count = stream.read(buffer, from, (buffer.length - from));
      if (count == -1) break;
      count += from;

      if ((from = processBytes(content, buffer, count)) == count) {
        from = 0;
      } else {
        count -= from;
        System.arraycopy(buffer, from, buffer, 0, count);
        from = count;
      }
    }

    endBytes(content);
  }

  public ByteOperations () {
    super();
  }
}
