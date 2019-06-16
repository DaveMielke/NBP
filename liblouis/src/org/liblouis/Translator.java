package org.liblouis;

public abstract class Translator extends TranslationComponent {
  protected Translator () {
    super();
  }

  public final static int makeInputOffsets (int[] inputOffsets, int[] outputOffsets) {
    int inputLength = outputOffsets.length - 1;
    int outputLength = Math.min(inputOffsets.length-1, outputOffsets[inputLength]);

    int inputIndex = 0;
    int outputIndex = 0;

    int inputOffset = 0;
    int outputOffset;

    while (true) {
      outputOffset = outputOffsets[inputIndex];
      if (inputIndex == inputLength) break;
      if (outputOffset >= outputLength) break;

      while (outputIndex < outputOffset) {
        inputOffsets[outputIndex++] = inputOffset;
      }

      inputOffset = inputIndex++;
    }

    if (outputOffset > outputLength) {
      if (inputIndex == 0) {
        outputOffset = 0;
      } else {
        outputOffset = outputOffsets[--inputIndex];

        while (inputIndex > 0) {
          if (outputOffsets[--inputIndex] != outputOffset) {
            inputIndex += 1;
            break;
          }
        }
      }
    }

    while (outputIndex < outputOffset) {
      inputOffsets[outputIndex++] = inputOffset;
    }

    inputOffsets[outputIndex] = inputIndex;
    return inputIndex;
  }

  public abstract boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate,
    boolean includeHighlighting, boolean noContractions
  );
}
