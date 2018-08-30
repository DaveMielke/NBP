package org.nbp.ipaws;

public class Base64 extends ApplicationComponent {
  private final int ALPHABET_SIZE = 64;
  private final int ENCODING_BLOCK_SIZE = 4;
  private final int DECODING_BLOCK_SIZE = 3;

  private final String encodingAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  private final char padCharacter = '=';

  private final byte[] indexMap = new byte[0X80];
  private final int INVALID_CHARACTER;

  public Base64 () {
    super();

    {
      int count = encodingAlphabet.length();

      if (count != ALPHABET_SIZE) {
        throw new AssertionError(
          String.format(
            "illegal alphabet size: %d != %d",
            count, ALPHABET_SIZE
          )
        );
      }

      INVALID_CHARACTER = count;
    }

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

    int count = (length / ENCODING_BLOCK_SIZE) * DECODING_BLOCK_SIZE;
    {
      int extra = length % ENCODING_BLOCK_SIZE;

      if (extra != 0) {
        if ((extra -= 1) < 1) {
          throw new IllegalArgumentException(
            String.format(
              "illegal encoded string length: %d",
              length
            )
          );
        }

        count += extra;
      }
    }

    byte[] decoded = new byte[count];
    int offset = 0;

    int index = 0;
    int state = 0;

    while (index < length) {
      char character = encoded.charAt(index);

      int bits = INVALID_CHARACTER;
      if (character < indexMap.length) bits = indexMap[character];

      if (bits == INVALID_CHARACTER) {
        throw new IllegalArgumentException(
          String.format(
            "invalid encoding character: U+%04X@[%d]",
            (int)character, index
          )
        );
      }

      index += 1;
      bits <<= (state + 1) * 2;

      if (state == 0) {
        decoded[offset] = (byte)bits;
      } else {
        decoded[offset++] |= bits >> 8;
        if (index == length) break;
        if (state < 3) decoded[offset] = (byte)(bits & 0XFF);
      }

      state = (state + 1) % ENCODING_BLOCK_SIZE;
    }

    if (offset != count) {
      throw new AssertionError(
        String.format(
          "unexpected decoded byte count: %d != %d",
          offset, count
        )
      );
    }

    return decoded;
  }
}
