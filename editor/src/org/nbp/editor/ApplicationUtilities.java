package org.nbp.editor;

import java.net.URLEncoder;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import android.util.Log;

public abstract class ApplicationUtilities {
  private final static String LOG_TAG = ApplicationUtilities.class.getName();

  private ApplicationUtilities () {
  }

  public final static String encodeString (String string) {
    String encoding = ApplicationParameters.STRING_ENCODING;

    try {
      return URLEncoder.encode(string, encoding);
    } catch (UnsupportedEncodingException exception) {
      Log.w(LOG_TAG, ("unsupported string encoding: " + encoding));
    }

    return null;
  }

  public final static String decodeString (String string) {
    String encoding = ApplicationParameters.STRING_ENCODING;

    try {
      return URLDecoder.decode(string, encoding);
    } catch (UnsupportedEncodingException exception) {
      Log.w(LOG_TAG, ("unsupported string encoding: " + encoding));
    }

    return null;
  }

  public final static boolean verifyTextRange (int start, int end, int length) {
    return (0 <= start) && (start <= end) && (end <= length);
  }
}
