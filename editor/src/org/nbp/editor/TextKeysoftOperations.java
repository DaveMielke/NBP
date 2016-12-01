package org.nbp.editor;

import android.text.SpannableStringBuilder;

public class TextKeysoftOperations extends ByteOperations {
  private int bytesProcessed;
  private boolean done;
  private boolean ignore;

  @Override
  protected void beginBytes (SpannableStringBuilder content) {
    super.beginBytes(content);
    bytesProcessed = 0;
    done = false;
    ignore = false;
  }

  @Override
  protected int processBytes (SpannableStringBuilder content, byte[] buffer, int count) {
    int from = KeysoftDefinitions.HEADER_SIZE - bytesProcessed;
    bytesProcessed += count;
    if (done || (from >= count)) return count;

    if (from > 0) {
      int newCount = count - from;
      byte[] newBuffer = new byte[newCount];
      System.arraycopy(buffer, from, newBuffer, 0, newCount);

      buffer = newBuffer;
      count = newCount;
    }

    for (int index=0; index<count; index+=1) {
      if (ignore) {
        ignore = false;
      } else {
        char character = (char)buffer[index];

        switch (character) {
          case KeysoftDefinitions.END_OF_FILE:
            count = index;
            done = true;
            continue;

          case KeysoftDefinitions.END_OF_LINE:
            character = '\n';
            break;

          case 0X02:
            ignore = true;
            continue;

          default:
            break;
        }

        content.append(character);
      }
    }

    return super.processBytes(content, buffer, count);
  }

  public TextKeysoftOperations () {
    super();
  }
}
