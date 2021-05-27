package org.nbp.editor.operations;
import org.nbp.editor.*;

import java.io.IOException;
import java.io.InputStream;

import android.text.Editable;

public class ByteOperations extends ContentOperations {
  protected void beginBytes (Editable content) throws IOException {
  }

  protected int processBytes (Editable content, byte[] buffer, int count) throws IOException {
    return count;
  }

  protected void endBytes (Editable content) throws IOException {
  }

  @Override
  public final void read (InputStream stream, Editable content) throws IOException {
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
    super(false);
  }
}
