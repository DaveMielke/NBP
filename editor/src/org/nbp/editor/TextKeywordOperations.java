package org.nbp.editor;

import java.io.IOException;

import android.text.Editable;

public class TextKeywordOperations extends ByteOperations {
  private int bytesProcessed;
  private boolean done;
  private boolean ignore;

  @Override
  protected void beginBytes (Editable content) throws IOException {
    super.beginBytes(content);
    bytesProcessed = 0;
    done = false;
    ignore = false;
  }

  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) throws IOException {
    int from = BrailleNoteKeyword.HEADER_SIZE - bytesProcessed;
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
          case BrailleNoteKeyword.END_OF_FILE:
            count = index;
            done = true;
            continue;

          case BrailleNoteKeyword.END_OF_LINE:
            character = '\n';
          case '\n':
            break;

          case 0X02:
            ignore = true;
            continue;

          default:
            if (Character.isISOControl(character)) character = '?';
            break;
        }

        content.append(character);
      }
    }

    return super.processBytes(content, buffer, count);
  }

  public TextKeywordOperations () {
    super();
  }
}
