package org.nbp.common;

import java.util.Queue;
import java.util.LinkedList;

public abstract class TextSegmentGenerator {
  private TextSegmentGenerator () {
  }

  public abstract void removeText ();
  public abstract void addText (CharSequence text);
  public abstract CharSequence nextSegment ();

  private final static class BaseGenerator extends TextSegmentGenerator {
    public BaseGenerator () {
      super();
    }

    private final Queue<CharSequence> textSegments = new LinkedList<CharSequence>();

    @Override
    public final void removeText () {
      textSegments.clear();
    }

    @Override
    public final void addText (CharSequence text) {
      if (text != null) textSegments.add(text);
    }

    @Override
    public final CharSequence nextSegment () {
      return textSegments.poll();
    }
  }

  public abstract static class OuterGenerator extends TextSegmentGenerator {
    public OuterGenerator () {
      super();
    }

    private TextSegmentGenerator innerGenerator = null;
    protected CharSequence remainingText = null;
    protected abstract CharSequence generateSegment ();

    protected final TextSegmentGenerator getInnerGenerator () {
      return innerGenerator;
    }

    @Override
    public final void removeText () {
      remainingText = null;
      getInnerGenerator().removeText();
    }

    @Override
    public final void addText (CharSequence text) {
      getInnerGenerator().addText(text);
    }

    @Override
    public final CharSequence nextSegment () {
      while (true) {
        if (remainingText == null) {
          remainingText = getInnerGenerator().nextSegment();
          if (remainingText == null) return null;
        }

        CharSequence segment = generateSegment();

        if (segment == null) {
          segment = remainingText;
          remainingText = null;
        }

        int from = 0;
        int to = segment.length();

        while (from < to) {
          if (!Character.isWhitespace(segment.charAt(from))) break;
          from += 1;
        }

        while (to > from) {
          if (!Character.isWhitespace(segment.charAt(--to))) {
            to += 1;
            break;
          }
        }

        if (to > from) return segment.subSequence(from, to);
      }
    }

    protected final void removeText (int count) {
      remainingText = remainingText.subSequence(count, remainingText.length());
    }
  }

  public static class Builder {
    private TextSegmentGenerator outerGenerator;

    private final TextSegmentGenerator makeInitialOuterGenerator () {
      return new BaseGenerator();
    }

    public Builder () {
      outerGenerator = makeInitialOuterGenerator();
    }

    public final TextSegmentGenerator build () {
      TextSegmentGenerator generator = outerGenerator;
      outerGenerator = makeInitialOuterGenerator();
      return generator;
    }

    public final Builder add (OuterGenerator generator) {
      generator.innerGenerator = outerGenerator;
      outerGenerator = generator;
      return this;
    }

    public final Builder add (final char delimiter) {
      add(
        new OuterGenerator() {
          @Override
          protected final CharSequence generateSegment () {
            int index = remainingText.toString().indexOf(delimiter);
            if (index < 0) return null;

            CharSequence segment = remainingText.subSequence(0, index);
            removeText(index+1);
            return segment;
          }
        }
      );

      return this;
    }

    public final Builder add (final int length) {
      add(
        new OuterGenerator() {
          @Override
          protected final CharSequence generateSegment () {
            if (remainingText.length() <= length) return null;

            CharSequence segment = remainingText.subSequence(0, length);
            removeText(length);
            return segment;
          }
        }
      );

      return this;
    }
  }
}
