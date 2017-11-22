package org.liblouis;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.InputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.duxburysystems.BrlTrn;
import com.duxburysystems.BrailleTranslationException;
import com.duxburysystems.BrailleTranslationErrors;

import android.os.Build;
import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

public abstract class DuxburyTranslator extends Translator {
  private final static String LOG_TAG = DuxburyTranslator.class.getName();

  private final static String authorizationAsset = Louis.toAssetsPath("duxbury.auth");
  private static Boolean isAuthorized = null;

  private final static void verifyAuthorization () {
    String serialNumber = Build.SERIAL;

    if (isAuthorized == null) {
      {
        String buildType = Build.TYPE;
        if (buildType == null) buildType = Build.UNKNOWN;

        if (buildType.equals("eng")) {
          isAuthorized = true;
          Log.i(LOG_TAG, ("Android build type is authorized: " + buildType));
          return;
        }
      }

      if ((serialNumber != null) && !serialNumber.isEmpty()) {
        Context context = Louis.getContext();
        AssetManager assets = context.getAssets();

        try {
          InputStream stream = assets.open(authorizationAsset);

          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            try {
              String line;

              while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");

                if (words.length > 0) {
                  if (serialNumber.equals(words[0])) {
                    isAuthorized = true;
                    Log.i(LOG_TAG, ("device serial number is authorized: " + serialNumber));
                    return;
                  }
                }
              }

              Log.w(LOG_TAG, ("device serial number not found: " + serialNumber));
            } finally {
              reader.close();
            }
          } finally {
            stream.close();
          }
        } catch (FileNotFoundException exception) {
          Log.w(LOG_TAG, ("asset not found: " + exception.getMessage()));
        } catch (IOException exception) {
          Log.w(LOG_TAG, ("Duxbury Systems translator authorization failure: " + exception.getMessage()));
        }
      } else {
        Log.w(LOG_TAG, "no device serial number");
      }

      isAuthorized = false;
    } else if (isAuthorized) {
      return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("device not authorized to use Duxbury Systems translators");

    if (serialNumber != null) {
      sb.append(": ");
      sb.append(serialNumber);
    }

    throw new SecurityException(sb.toString());
  }

  protected DuxburyTranslator () {
    super();
    verifyAuthorization();
  }

  protected abstract BrlTrn getForwardTranslator ();
  protected abstract BrlTrn getBackwardTranslator ();

  @Override
  public final boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  ) {
    BrlTrn translator =
      backTranslate?
      getBackwardTranslator():
      getForwardTranslator();

    int inputLength = inputBuffer.length();
    int outputLength = outputBuffer.length;

    {
      String input = inputBuffer.toString();
      String output = translator.translateWithPositionMappings(input, outputOffsets);

      if (output == null) {
        throw new BrailleTranslationException(BrailleTranslationErrors.NO_MEMORY);
      }

      inputLength = makeInputOffsets(inputOffsets, outputOffsets);
      outputLength = outputOffsets[inputLength];
      output.getChars(0, outputLength, outputBuffer, 0);
    }

    resultValues[RVI_INPUT_LENGTH]  = inputLength;
    resultValues[RVI_OUTPUT_LENGTH] = outputLength;
    resultValues[RVI_CURSOR_OFFSET] = NO_CURSOR;

    return true;
  }
}
