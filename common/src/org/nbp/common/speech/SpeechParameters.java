package org.nbp.common.speech;

import android.util.Log;
import org.nbp.common.CommonUtilities;

import android.speech.tts.TextToSpeech;
import android.os.Bundle; // new parameters
import java.util.HashMap; // old parameters

public class SpeechParameters {
  private final static String LOG_TAG = SpeechParameters.class.getName();

  private Bundle newParameters = null;
  private HashMap<String, String> oldParameters = null;

  protected SpeechParameters () {
    if (CommonUtilities.haveLollipop) {
      newParameters = new Bundle();
    } else {
      oldParameters = new HashMap<String, String>();
    }
  }

  public final String get (String key) {
    if (newParameters != null) {
      synchronized (newParameters) {
        return newParameters.getString(key);
      }
    }

    if (oldParameters != null) {
      synchronized (oldParameters) {
        return oldParameters.get(key);
      }
    }

    return null;
  }

  public final void set (String key, String value) {
    if (newParameters != null) {
      synchronized (newParameters) {
        newParameters.putString(key, value);
      }
    }

    if (oldParameters != null) {
      synchronized (oldParameters) {
        oldParameters.put(key, value);
      }
    }
  }

  public final void set(String key, int value) {
    set(key, Integer.toString(value));
  }

  public final void set(String key, float value) {
    set(key, Float.toString(value));
  }

  public final String getUtteranceIdentifier () {
    return get(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
  }

  public final void setUtteranceIdentifier (String identifier) {
    set(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, identifier);
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

  public final boolean verifyVolume (float value) {
    return verifyRange("volume", value, VOLUME_MINIMUM, VOLUME_MAXIMUM);
  }

  public final String getVolume () {
    return get(TextToSpeech.Engine.KEY_PARAM_VOLUME);
  }

  public final void setVolume (float value) {
    set(TextToSpeech.Engine.KEY_PARAM_VOLUME, value);
  }

  public final static float BALANCE_CENTER = 0.0f;
  public final static float BALANCE_RIGHT = 1.0f;
  public final static float BALANCE_LEFT = -BALANCE_RIGHT;

  public final boolean verifyBalance (float value) {
    return verifyRange("balance", value, BALANCE_LEFT, BALANCE_RIGHT);
  }

  public final String getBalance () {
    return get(TextToSpeech.Engine.KEY_PARAM_PAN);
  }

  public final void setBalance (float value) {
    set(TextToSpeech.Engine.KEY_PARAM_PAN, value);
  }

  public final static float RATE_REFERENCE = 1.0f;
  public final static float RATE_MAXIMUM = 4.0f;
  public final static float RATE_MINIMUM = 1.0f / 3.0f;

  public final boolean verifyRate (float value) {
    return verifyRange("rate", value, RATE_MINIMUM, RATE_MAXIMUM);
  }

  public final static float PITCH_REFERENCE = 1.0f;
  public final static float PITCH_MAXIMUM = 2.0f;
  public final static float PITCH_MINIMUM = 1.0f / 2.0f;

  public final boolean verifyPitch (float value) {
    return verifyRange("pitch", value, PITCH_MINIMUM, PITCH_MAXIMUM);
  }
}
