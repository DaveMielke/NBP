package org.liblouis;

import java.util.Arrays;

import android.util.Log;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class Translation {
  private final static String LOG_TAG = Translation.class.getName();

  private native boolean translate (
    String tableName,
    String inputBuffer, char[] outputBuffer, short[] typeForm,
    int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate
  );

  private final TranslationTable translationTable;
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

  public final TranslationTable getTranslationTable () {
    return translationTable;
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

  private final static short TYPE_FORM_BOLD = Emphasis.getBoldBit();
  private final static short TYPE_FORM_ITALIC = Emphasis.getItalicBit();
  private final static short TYPE_FORM_UNDERLINE = Emphasis.getUnderlineBit();

  private static short[] createTypeForm (int length) {
    short[] typeForm = new short[length];
    Arrays.fill(typeForm, (short)0);
    return typeForm;
  }

  private static short[] createTypeForm (int length, CharSequence text) {
    short[] typeForm = null;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        for (Object span : spans) {
          short flags = 0;

          if (span instanceof UnderlineSpan) {
            flags |= TYPE_FORM_UNDERLINE;
          } else if (span instanceof StyleSpan) {
            switch (((StyleSpan)span).getStyle()) {
              case Typeface.BOLD:
                flags |= TYPE_FORM_BOLD;
                break;

              case Typeface.ITALIC:
                flags |= TYPE_FORM_ITALIC;
                break;

              case Typeface.BOLD_ITALIC:
                flags |= TYPE_FORM_BOLD;
                flags |= TYPE_FORM_ITALIC;
                break;
            }
          }

          if (flags != 0) {
            if (typeForm == null) typeForm = createTypeForm(length);

            int start = spanned.getSpanStart(span);
            int end = spanned.getSpanEnd(span);

            for (int index=start; index<end; index+=1) {
              typeForm[index] |= flags;
            }
          }
        }
      }
    }

    return typeForm;
  }

  private enum ResultValuesIndex {
    INPUT_LENGTH,
    OUTPUT_LENGTH,
    CURSOR_OFFSET,
    ; // end of enumeration
  }

  private final static int RESULT_VALUES_COUNT = ResultValuesIndex.values().length;
  private final static int RVI_INPUT_LENGTH = ResultValuesIndex.INPUT_LENGTH.ordinal();
  private final static int RVI_OUTPUT_LENGTH = ResultValuesIndex.OUTPUT_LENGTH.ordinal();
  private final static int RVI_CURSOR_OFFSET = ResultValuesIndex.CURSOR_OFFSET.ordinal();

  public Translation (TranslationBuilder builder, boolean backTranslate) {
    translationTable = builder.getTranslationTable();
    suppliedInput = builder.getInputCharacters();
    inputCursor = builder.getCursorOffset();

    final TableFile tableFile =
      backTranslate?
      translationTable.getBackwardTableFile():
      translationTable.getForwardTableFile();
    final String fileName = tableFile.getFileName();

    final boolean includeHighlighting = builder.getIncludeHighlighting();
    final boolean allowLongerOutput = builder.getAllowLongerOutput();
    int outputLength = builder.getOutputLength();

    final String inputString = suppliedInput.toString();
    final int inputLength = inputString.length();

    CharSequence input = suppliedInput;
    int[] outOffsets = new int[inputLength];
    final int[] resultValues = new int[RESULT_VALUES_COUNT];

    char[] output;
    int[] inOffsets;
    boolean translated;

    int retryCount = 0;
    int previousConsumed = -1;

    while (true) {
      output = new char[outputLength];
      inOffsets = new int[outputLength];

      resultValues[RVI_INPUT_LENGTH]  = inputLength;
      resultValues[RVI_OUTPUT_LENGTH] = outputLength;
      resultValues[RVI_CURSOR_OFFSET] = (inputCursor != null)? inputCursor: -1;

      int typeFormLength = Math.max(inputLength, outputLength);
      short[] typeForm = !includeHighlighting? null:
                        backTranslate? null:
                        createTypeForm(typeFormLength, input);

      synchronized (Louis.NATIVE_LOCK) {
        translated = translate(
          fileName,
          inputString, output, typeForm,
          outOffsets, inOffsets, resultValues,
          backTranslate
        );
      }

      if (!translated) {
        Log.w(LOG_TAG, "translation failed");

        if (resultValues[RVI_INPUT_LENGTH] > resultValues[RVI_OUTPUT_LENGTH]) {
          resultValues[RVI_INPUT_LENGTH] = resultValues[RVI_OUTPUT_LENGTH];
        } else if (resultValues[RVI_OUTPUT_LENGTH] > resultValues[RVI_INPUT_LENGTH]) {
          resultValues[RVI_OUTPUT_LENGTH] = resultValues[RVI_INPUT_LENGTH];
        }

        for (int offset=0; offset<resultValues[RVI_INPUT_LENGTH]; offset+=1) {
          inOffsets[offset] = outOffsets[offset] = offset;
        }

        if (resultValues[RVI_CURSOR_OFFSET] >= resultValues[RVI_INPUT_LENGTH]) {
          resultValues[RVI_CURSOR_OFFSET] = -1;
        }

        inputString.getChars(0, inputString.length(), output, 0);
        break;
      }

      if (backTranslate) {
        int outStart = 0;

        while (true) {
          int inOffset = resultValues[RVI_INPUT_LENGTH];
          if (inOffset == inputLength) break;

          int outOffset = outOffsets[inOffset];
          if (outOffset < outStart) break;
          if (outOffset > outputLength) break;

          outStart = outOffset;
          resultValues[RVI_INPUT_LENGTH] = inOffset + 1;
        }
      }

    FIX_END:
      if (resultValues[RVI_INPUT_LENGTH] == inputLength) {
        if (backTranslate) {
          int outLength = resultValues[RVI_OUTPUT_LENGTH];

          while (true) {
            int inOffset = resultValues[RVI_INPUT_LENGTH];
            if (inOffset == 0) break;

            if (outOffsets[inOffset -= 1] != outLength) break;
            resultValues[RVI_INPUT_LENGTH] = inOffset;
          }

          while (true) {
            int inOffset = resultValues[RVI_INPUT_LENGTH];
            if (inOffset == inputLength) break;

            int outOffset = resultValues[RVI_OUTPUT_LENGTH];
            if (outOffset == outputLength) break FIX_END;

            output[outOffset] = inputString.charAt(inOffset);
            outOffsets[inOffset] = outOffset;
            inOffsets[outOffset] = inOffset;

            resultValues[RVI_INPUT_LENGTH] = inOffset + 1;
            resultValues[RVI_OUTPUT_LENGTH] = outOffset + 1;
          }
        }

        break;
      }

      if (!allowLongerOutput) break;
      int currentConsumed = resultValues[RVI_INPUT_LENGTH];

      if (currentConsumed == previousConsumed) {
        if (++retryCount > 5) break;
        if ((outputLength - resultValues[RVI_OUTPUT_LENGTH]) > 100) break;
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
      input = input.subSequence(0, newInputLength);
      outOffsets = Arrays.copyOf(outOffsets, newInputLength);
    }

    if (newOutputLength < outputLength) {
      output = Arrays.copyOf(output, newOutputLength);
      inOffsets = Arrays.copyOf(inOffsets, newOutputLength);
    }

    consumedInput = input;
    outputArray = output;
    outputOffsets = outOffsets;
    inputOffsets = inOffsets;
    outputCursor = (newCursorOffset < 0)? null: Integer.valueOf(newCursorOffset);

    translationSucceeded = translated;
  }
}