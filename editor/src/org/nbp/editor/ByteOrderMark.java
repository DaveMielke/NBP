package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;

public enum ByteOrderMark {
  UTF8("UTF-8", new byte[] {(byte)0XEF, (byte)0XBB, (byte)0XBF}),
  UTF16BE("UTF-16BE", new byte[] {(byte)0XFE, (byte)0XFF}),
  UTF16LE("UTF-16LE", new byte[] {(byte)0XFF, (byte)0XFE}),
  ; // end of enumeration

  private final String encodingName;
  private final byte[] byteSequence;

  ByteOrderMark (String name, byte[] bytes) {
    encodingName = name;
    byteSequence = bytes;
  }

  public final String getEncodingName () {
    return encodingName;
  }

  public final int getLength () {
    return byteSequence.length;
  }

  public final byte getByte (int offset) {
    return byteSequence[offset];
  }

  private static int maximumLength = 0;

  static {
    for (ByteOrderMark bom : values()) {
      maximumLength = Math.max(maximumLength, bom.getLength());
    }
  }

  public static int getMaximumLength () {
    return maximumLength;
  }

  public static InputStreamReader getInputStreamReader (InputStream stream) throws IOException {
    byte[] buffer = new byte[getMaximumLength()];

    if (!stream.markSupported()) {
      stream = new BufferedInputStream(stream, buffer.length);
    }

    stream.mark(buffer.length);
    int count = 0;
    boolean end = false;

  NEXT_BOM:
    for (ByteOrderMark bom : values()) {
      int length = bom.getLength();

      for (int offset=0; offset<length; offset+=1) {
        if (offset == count) {
          if (end) continue NEXT_BOM;
          int next = stream.read();

          if (next < 0) {
            end = true;
            continue NEXT_BOM;
          }

          buffer[count++] = (byte)next;
        }

        if (bom.getByte(offset) != buffer[offset]) continue NEXT_BOM;
      }

      return new InputStreamReader(stream, bom.getEncodingName());
    }

    stream.reset();
    return new InputStreamReader(stream, UTF8.getEncodingName());
  }
}
