package org.nbp.ipaws;

import android.util.Log;

public class Base64 extends ApplicationComponent {
  private final static String LOG_TAG = Base64.class.getName();

  private final String encodingAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  private final char padCharacter = '=';

  private final byte[] indexMap = new byte[0X80];
  private final int INVALID_CHARACTER;

  public Base64 () {
    super();

    INVALID_CHARACTER = encodingAlphabet.length();

    {
      int count = indexMap.length;

      for (int index=0; index<count; index+=1) {
        indexMap[index] = (byte)INVALID_CHARACTER;
      }
    }

    {
      int count = encodingAlphabet.length();

      for (int index=0; index<count; index+=1) {
        indexMap[encodingAlphabet.charAt(index)] = (byte)index;
      }
    }
  }

  public final byte[] decode (String encoded) {
    encoded = encoded.replaceAll("\\s", "");
    int length = encoded.length();

    while (length > 0) {
      int end = length - 1;
      if (encoded.charAt(end) != padCharacter) break;
      length = end;
    }

    int count = (length / 4) * 3;
    {
      int extra = length % 4;

      if (extra != 0) {
        if ((extra -= 1) < 1) {
          Log.w(LOG_TAG, ("invalid encoding length: " + length));
          return null;
        }

        count += extra;
      }
    }

    byte[] decoded = new byte[count];
    int offset = 0;

    int index = 0;
    int state = 0;

    while (index < length) {
      char character = encoded.charAt(index++);

      int bits = INVALID_CHARACTER;
      if (character < indexMap.length) bits = indexMap[character];

      if (bits == INVALID_CHARACTER) {
        Log.w(LOG_TAG,
          String.format(
            "invalid encoding character: U+%04X", character
          )
        );

        return null;
      }

      bits <<= (state + 1) * 2;

      if (state == 0) {
        decoded[offset] = (byte)bits;
      } else {
        decoded[offset++] |= bits >> 8;
        if (index == length) break;
        if (state < 3) decoded[offset] = (byte)(bits & 0XFF);
      }

      state = (state + 1) % 4;
    }

    if (offset != count) {
      Log.w(LOG_TAG,
        String.format(
          "unexpected decoded size: %d != %d", offset, count
        )
      );

      return null;
    }

    return decoded;
  }
}
