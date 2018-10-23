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

  public final Bundle getNewParameters () {
    return newParameters;
  }

  public final HashMap<String, String> getOldParameters () {
    return oldParameters;
  }

  public final String getParameter (String key) {
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

  public final void setParameter (String key, String value) {
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

  public final static boolean verifyVolume (float value) {
    return verifyRange("volume", value, VOLUME_MINIMUM, VOLUME_MAXIMUM);
  }

  public final String getVolume () {
    return getParameter(TextToSpeech.Engine.KEY_PARAM_VOLUME);
  }

  public final void setVolume (float value) {
    setParameter(TextToSpeech.Engine.KEY_PARAM_VOLUME, value);
  }

  public final static float BALANCE_CENTER = 0.0f;
  public final static float BALANCE_RIGHT = 1.0f;
  public final static float BALANCE_LEFT = -BALANCE_RIGHT;

  public final static boolean verifyBalance (float value) {
    return verifyRange("balance", value, BALANCE_LEFT, BALANCE_RIGHT);
  }

  public final String getBalance () {
    return getParameter(TextToSpeech.Engine.KEY_PARAM_PAN);
  }

  public final void setBalance (float value) {
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
}
