package org.nbp.editor;

import android.text.SpannableStringBuilder;

public class BrailleKeysoftOperations extends ASCIIBrailleOperations {
  private final static int HEADER_SIZE = 0X26E;
  private int bytesProcessed;
  private boolean done;

  @Override
  protected void beginBytes (SpannableStringBuilder content) {
    super.beginBytes(content);
    bytesProcessed = 0;
    done = false;
  }

  @Override
  protected int processBytes (SpannableStringBuilder content, byte[] buffer, int count) {
    int from = HEADER_SIZE - bytesProcessed;
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
        case 0X1A:
          count = index;
          done = true;
          continue;

        case 0X0D:
          brf = '\n';
          break;

        default:
          continue;
      }

      buffer[index] = brf;
    }

    return super.processBytes(content, buffer, count);
  }

  public BrailleKeysoftOperations () {
    super();
  }
}
