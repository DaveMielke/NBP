package org.nbp.editor;

import android.text.SpannableStringBuilder;

public class ASCIIBrailleOperations extends ByteOperations {
  @Override
  protected int processBytes (SpannableStringBuilder content, byte[] buffer, int count) {
    for (int index=0; index<count; index+=1) {
      byte brf = buffer[index];

      if (brf == '\n') {
        content.append((char)brf);
      } else {
        char character = ASCIIBraille.toCharacter(brf);
        if (character != 0) content.append(character);
      }
    }

    return super.processBytes(content, buffer, count);
  }

  public ASCIIBrailleOperations () {
    super();
  }
}
