package org.nbp.b2g.ui;

public abstract class WordAction extends HorizontalAction {
  @Override
  protected final int findNextObject (CharSequence text, int offset) {
    int length = text.length();
    boolean wasSpace = false;

    while (offset < length) {
      boolean isSpace = Character.isWhitespace(text.charAt(offset));
      if (wasSpace && !isSpace) return offset;

      wasSpace = isSpace;
      offset += 1;
    }

    return NOT_FOUND;
  }

  @Override
  protected final int findPreviousObject (CharSequence text, int offset) {
    boolean wasSpace = true;

    while (offset > 0) {
      boolean isSpace = Character.isWhitespace(text.charAt(--offset));

      if (isSpace) {
        if (!wasSpace) return offset + 1;
      } else {
        if (offset == 0) return offset;
      }

      wasSpace = isSpace;
    }

    return NOT_FOUND;
  }

  protected WordAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
