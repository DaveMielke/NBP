package org.nbp.editor;

import java.io.IOException;

import android.text.Editable;

public class BrailleKeywordOperations extends ASCIIBrailleOperations {
  private int bytesProcessed;
  private boolean done;

  @Override
  protected void beginBytes (Editable content) throws IOException {
    super.beginBytes(content);
    bytesProcessed = 0;
    done = false;
  }

  @Override
  protected int processBytes (Editable content, byte[] buffer, int count) {
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
      byte brf = buffer[index];

      switch (brf) {
        case BrailleNoteKeyword.END_OF_FILE:
          count = index;
          done = true;
          continue;

        case BrailleNoteKeyword.END_OF_LINE:
          brf = '\n';
          break;

        default:
          continue;
      }

      buffer[index] = brf;
    }

    return super.processBytes(content, buffer, count);
  }

  public BrailleKeywordOperations () {
    super();
  }
}
