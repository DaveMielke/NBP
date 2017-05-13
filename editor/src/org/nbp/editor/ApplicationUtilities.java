package org.nbp.editor;

import java.net.URLEncoder;
import java.net.URLDecoder;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static String encodeString (String string) {
    return URLEncoder.encode(string);
  }

  public final static String decodeString (String string) {
    return URLDecoder.decode(string);
  }

  public final static boolean verifyTextRange (int start, int end, int length) {
    return (0 <= start) && (start <= end) && (end <= length);
  }
}
