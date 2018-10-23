package org.nbp.common.speech;

import android.util.Log;
import org.nbp.common.CommonUtilities;

import android.speech.tts.TextToSpeech;
import android.os.Bundle; // new parameters
import java.util.HashMap; // old parameters

import java.io.File;

public class SpeechParameters {
  private final static String LOG_TAG = SpeechParameters.class.getName();

  public static class OldParameters extends HashMap<String, String> {
    public OldParameters () {
      super();
    }
  }

  public final static boolean USE_NEW_PARAMETERS = CommonUtilities.haveLollipop;
  private Bundle newParameters = null;
  private OldParameters oldParameters = null;

  public SpeechParameters () {
    if (USE_NEW_PARAMETERS) {
      newParameters = new Bundle();
    } else {
      oldParameters = new OldParameters();
    }
  }

  public final Bundle getNewParameters () {
    return newParameters;
  }

  public final OldParameters getOldParameters () {
    return oldParameters;
  }

  public final String getParameter (String key) {
    if (USE_NEW_PARAMETERS) {
      synchronized (newParameters) {
        return newParameters.getString(key);
      }
    } else {
      synchronized (oldParameters) {
        return oldParameters.get(key);
      }
    }
  }

  public final void setParameter (String key, String value) {
    if (USE_NEW_PARAMETERS) {
      synchronized (newParameters) {
        newParameters.putString(key, value);
      }
    } else {
      synchronized (oldParameters) {
        oldParameters.put(key, value);
      }
    }
  }

  public final void setParameter (String key, int value) {
    setParameter(key, Integer.toString(value));
  }

  public final void setParameter (String key, float value) {
    setParameter(key, Float.toString(value));
  }

  public final String getUtteranceIdentifier () {
    return getParameter(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
  }

  public final void setUtteranceIdentifier (String identifier) {
    setParameter(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, identifier);
  }

  private Integer currentStream = null;

  public final Integer getStream () {
    return currentStream;
  }

  public final void setStream (int value) {
    currentStream = value;
    setParameter(TextToSpeech.Engine.KEY_PARAM_STREAM, value);
  }

  private static boolean verifyRange (String label, float value, float minimum, float maximum) {
    String reason;

    if (value < minimum) {
      reason = value + " less than " + minimum;
    } else if (value > maximum) {
      reason = value + " greater than " + maximum;
    } else {
      return true;
    }

    Log.w(LOG_TAG, ("invalid " + label + ": " + reason));
    return false;
  }

  public final static float VOLUME_MAXIMUM = 1.0f;
  public final static float VOLUME_MINIMUM = 0.0f;
  private Float currentVolume = null;

  public final static boolean verifyVolume (float value) {
    return verifyRange("volume", value, VOLUME_MINIMUM, VOLUME_MAXIMUM);
  }

  public final Float getVolume () {
    return currentVolume;
  }

  public final void setVolume (float value) {
    currentVolume = value;
    setParameter(TextToSpeech.Engine.KEY_PARAM_VOLUME, value);
  }

  public final static float BALANCE_CENTER = 0.0f;
  public final static float BALANCE_RIGHT = 1.0f;
  public final static float BALANCE_LEFT = -BALANCE_RIGHT;
  private Float currentBalance = null;

  public final static boolean verifyBalance (float value) {
    return verifyRange("balance", value, BALANCE_LEFT, BALANCE_RIGHT);
  }

  public final Float getBalance () {
    return currentBalance;
  }

  public final void setBalance (float value) {
    currentBalance = value;
    setParameter(TextToSpeech.Engine.KEY_PARAM_PAN, value);
  }

  public final static float RATE_REFERENCE = 1.0f;
  public final static float RATE_MAXIMUM = 4.0f;
  public final static float RATE_MINIMUM = 1.0f / 3.0f;

  public final static boolean verifyRate (float value) {
    return verifyRange("rate", value, RATE_MINIMUM, RATE_MAXIMUM);
  }

  public final static float PITCH_REFERENCE = 1.0f;
  public final static float PITCH_MAXIMUM = 2.0f;
  public final static float PITCH_MINIMUM = 1.0f / 2.0f;

  public final static boolean verifyPitch (float value) {
    return verifyRange("pitch", value, PITCH_MINIMUM, PITCH_MAXIMUM);
  }

  public static int getMaximumLength (TextToSpeech tts) {
    if (CommonUtilities.haveJellyBeanMR2) {
      try {
        return tts.getMaxSpeechInputLength();
      } catch (IllegalArgumentException exception) {
        Log.w(LOG_TAG, "get maximum input length", exception);
      }
    }

    return 4000;
  }

  public final int speak (TextToSpeech tts, CharSequence text, int queueMode) {
    if (USE_NEW_PARAMETERS) {
      return tts.speak(text, queueMode, newParameters, getUtteranceIdentifier());
    } else {
      return tts.speak(text.toString(), queueMode, oldParameters);
    }
  }

  public final int synthesize (TextToSpeech tts, CharSequence text, File file) {
    if (USE_NEW_PARAMETERS) {
      return tts.synthesizeToFile(
        text, newParameters, file, getUtteranceIdentifier()
      );
    } else {
      return tts.synthesizeToFile(
        text.toString(), oldParameters, file.getAbsolutePath()
      );
    }
  }
}
