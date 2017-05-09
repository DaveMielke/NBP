package org.nbp.editor;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static boolean verifyTextRange (int start, int end, int length) {
    return (0 <= start) && (start <= end) && (end <= length);
  }
}
