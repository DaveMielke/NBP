package org.liblouis;

import java.util.Arrays;

import android.util.Log;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

public abstract class Translation extends TranslationComponent {
  private final static String LOG_TAG = Translation.class.getName();

  public final static String TEXT_TAG = "TXT";
  public final static String BRAILLE_TAG = "BRL";

  public abstract String getInputTag ();
  public abstract String getOutputTag ();

  public abstract int getBrailleLength ();
  public abstract int getBrailleOffset (int textOffset);
  public abstract int findFirstBrailleOffset (int brailleOffset);
  public abstract int findLastBrailleOffset (int brailleOffset);
  public abstract Integer getBrailleCursor ();

  public abstract int getTextLength ();
  public abstract int getTextOffset (int brailleOffset);
  public abstract int findFirstTextOffset (int textOffset);
  public abstract int findLastTextOffset (int textOffset);
  public abstract Integer getTextCursor ();

  private final Translator translator;
  private final boolean translationSucceeded;

  private final CharSequence suppliedInput;
  private final CharSequence consumedInput;
  private final int[] inputOffsets;
  private final Integer inputCursor;

  private final char[] outputArray;
  private String outputString = null;
  private CharSequence outputWithSpans = null;
  private final int[] outputOffsets;
  private final Integer outputCursor;

  public final Translator getTranslator () {
    return translator;
  }

  public final CharSequence getSuppliedInput () {
    return suppliedInput;
  }

  public final CharSequence getConsumedInput () {
    return consumedInput;
  }

  public final int getInputLength () {
    return consumedInput.length();
  }

  public final int getInputOffset (int outputOffset) {
    if (outputOffset == getOutputLength()) return getInputLength();
    return inputOffsets[outputOffset];
  }

  public final int findFirstInputOffset (int inputOffset) {
    final int outputOffset = getOutputOffset(inputOffset);

    while (inputOffset > 0) {
      int next = inputOffset - 1;
      if (getOutputOffset(next) != outputOffset) break;
      inputOffset = next;
    }

    return inputOffset;
  }

  public final int findLastInputOffset (int inputOffset) {
    final int outputOffset = getOutputOffset(inputOffset);
    final int inputLength = getInputLength();

    while (inputOffset < inputLength) {
      int next = inputOffset + 1;
      if (getOutputOffset(next) != outputOffset) break;
      inputOffset = next;
    }

    return inputOffset;
  }

  public final Integer getInputCursor () {
    return inputCursor;
  }

  public final char[] getOutputAsArray () {
    return outputArray;
  }

  public final String getOutputAsString () {
    synchronized (this) {
      if (outputString == null) {
        outputString = new String(outputArray);
      }
    }

    return outputString;
  }

  private final void copyInputSpans (SpannableStringBuilder sb) {
    CharSequence input = consumedInput;

    if (input instanceof Spanned) {
      Spanned spanned = (Spanned)input;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        int length = sb.length();

        for (Object span : spans) {
          int start = getOutputOffset(spanned.getSpanStart(span));
          int end = getOutputOffset(spanned.getSpanEnd(span));
          int flags = spanned.getSpanFlags(span);

          if (start == length) continue;
          if (end == start) continue;

          try {
            sb.setSpan(span, start, end, flags);
          } catch (RuntimeException exception) {
            Log.w(LOG_TAG, ("set span failed: " + exception.getMessage()));
          }
        }
      }
    }
  }

  public final CharSequence getOutputWithSpans () {
    synchronized (this) {
      if (outputWithSpans == null) {
        SpannableStringBuilder sb = new SpannableStringBuilder(getOutputAsString());
        copyInputSpans(sb);
        outputWithSpans = sb.subSequence(0, sb.length());
      }
    }

    return outputWithSpans;
  }

  public final int getOutputLength () {
    return outputArray.length;
  }

  public final int getOutputOffset (int inputOffset) {
    if (inputOffset == getInputLength()) return getOutputLength();
    return outputOffsets[inputOffset];
  }

  public final int findFirstOutputOffset (int outputOffset) {
    final int inputOffset = getInputOffset(outputOffset);

    while (outputOffset > 0) {
      int next = outputOffset - 1;
      if (getInputOffset(next) != inputOffset) break;
      outputOffset = next;
    }

    return outputOffset;
  }

  public final int findLastOutputOffset (int outputOffset) {
    final int inputOffset = getInputOffset(outputOffset);
    final int outputLength = getOutputLength();

    while (outputOffset < outputLength) {
      int next = outputOffset + 1;
      if (getInputOffset(next) != inputOffset) break;
      outputOffset = next;
    }

    return outputOffset;
  }

  public final Integer getOutputCursor () {
    return outputCursor;
  }

  public Translation (TranslationBuilder builder, boolean backTranslate) {
    super();

    translator = builder.getTranslator();
    suppliedInput = builder.getInputCharacters();
    inputCursor = builder.getCursorOffset();

    final boolean includeHighlighting = builder.getIncludeHighlighting();
    final boolean noContractions = builder.getNoContractions();
    final boolean allowLongerOutput = builder.getAllowLongerOutput();

    final int inputLength = suppliedInput.length();
    int outputLength = builder.getOutputLength();

    int[] outOffsets = new int[inputLength + 1];
    final int[] resultValues = new int[RESULT_VALUES_COUNT];

    char[] output;
    int[] inOffsets;
    boolean translated;

    int retryCount = 0;
    int previousConsumed = -1;

    while (true) {
      output = new char[outputLength];
      inOffsets = new int[outputLength + 1];

      resultValues[RVI_INPUT_LENGTH]  = inputLength;
      resultValues[RVI_OUTPUT_LENGTH] = outputLength;
      resultValues[RVI_CURSOR_OFFSET] = (inputCursor != null)? inputCursor: NO_CURSOR;

      translated = translator.translate(
        suppliedInput, output, outOffsets, inOffsets,
        resultValues, backTranslate, includeHighlighting, noContractions
      );

      if (!translated) {
        Log.w(LOG_TAG, "translation failed");

        if (resultValues[RVI_INPUT_LENGTH] > resultValues[RVI_OUTPUT_LENGTH]) {
          resultValues[RVI_INPUT_LENGTH] = resultValues[RVI_OUTPUT_LENGTH];
        } else if (resultValues[RVI_OUTPUT_LENGTH] > resultValues[RVI_INPUT_LENGTH]) {
          resultValues[RVI_OUTPUT_LENGTH] = resultValues[RVI_INPUT_LENGTH];
        }

        {
          final int length = resultValues[RVI_INPUT_LENGTH];

          for (int offset=0; offset<=length; offset+=1) {
            inOffsets[offset] = outOffsets[offset] = offset;
          }

          if (resultValues[RVI_CURSOR_OFFSET] >= length) {
            resultValues[RVI_CURSOR_OFFSET] = NO_CURSOR;
          }

          suppliedInput.toString().getChars(0, length, output, 0);
        }

        break;
      }

      if (backTranslate) {
        while (true) {
          final int inOffset = resultValues[RVI_INPUT_LENGTH];
          if (inOffset == inputLength) break;

          final int outOffset = resultValues[RVI_OUTPUT_LENGTH];
          if (outOffset == outputLength) break;

          output[outOffset] = suppliedInput.charAt(inOffset);
          outOffsets[inOffset] = outOffset;
          inOffsets[outOffset] = inOffset;

          resultValues[RVI_INPUT_LENGTH] = inOffset + 1;
          resultValues[RVI_OUTPUT_LENGTH] = outOffset + 1;
        }
      }

      int currentConsumed = resultValues[RVI_INPUT_LENGTH];
      if (currentConsumed == inputLength) break;
      if (!allowLongerOutput) break;

      if (currentConsumed == previousConsumed) {
        if (++retryCount > ApplicationParameters.TRANSLATION_RETRY_LIMIT) break;
        if ((outputLength - resultValues[RVI_OUTPUT_LENGTH]) > ApplicationParameters.TRANSLATION_UNUSED_LIMIT) break;
      } else {
        retryCount = 0;
        previousConsumed = currentConsumed;
      }

      outputLength <<= 1;
    }

    int newInputLength  = resultValues[RVI_INPUT_LENGTH];
    int newOutputLength = resultValues[RVI_OUTPUT_LENGTH];
    int newCursorOffset = resultValues[RVI_CURSOR_OFFSET];

    if (newInputLength < inputLength) {
      consumedInput = suppliedInput.subSequence(0, newInputLength);
      outOffsets = Arrays.copyOf(outOffsets, newInputLength + 1);
    } else {
      consumedInput = suppliedInput;
    }

    if (newOutputLength < outputLength) {
      output = Arrays.copyOf(output, newOutputLength);
      inOffsets = Arrays.copyOf(inOffsets, newOutputLength + 1);
    }

    outOffsets[newInputLength] = newOutputLength;
    inOffsets[newOutputLength] = newInputLength;

    outputArray = output;
    outputOffsets = outOffsets;
    inputOffsets = inOffsets;
    outputCursor = (newCursorOffset < 0)? null: Integer.valueOf(newCursorOffset);

    translationSucceeded = translated;
  }
}
