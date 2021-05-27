package org.nbp.common.speech;

import android.util.Log;
import org.nbp.common.CommonUtilities;

import android.speech.tts.TextToSpeech;
import android.os.Bundle; // new parameters
import java.util.HashMap; // old parameters

import java.io.File;

public class SpeechParameters {
  private final static String LOG_TAG = SpeechParameters.class.getName();

  private interface Paradigm {
    public String get (String key);

    public void set (String key, String value);
    public void set (String key, int value);
    public void set (String key, float value);

    public int speak (TextToSpeech tts, CharSequence text, int queueMode);
    public int synthesize (TextToSpeech tts, CharSequence text, File file);
  }

  private final Paradigm paradigm;

  public SpeechParameters () {
    if (CommonUtilities.haveLollipop) {
      paradigm = new Paradigm() {
        private final Bundle arguments = new Bundle();

        @Override
        public final String get (String key) {
          synchronized (arguments) {
            return arguments.getString(key);
          }
        }

        private final String getUtteranceIdentifier () {
          return get(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        }

        @Override
        public final void set (String key, String value) {
          synchronized (arguments) {
            arguments.putString(key, value);
          }
        }

        @Override
        public final void set (String key, int value) {
          synchronized (arguments) {
            arguments.putInt(key, value);
          }
        }

        @Override
        public final void set (String key, float value) {
          synchronized (arguments) {
            arguments.putFloat(key, value);
          }
        }

        @Override
        public final int speak (TextToSpeech tts, CharSequence text, int queueMode) {
          synchronized (arguments) {
            return tts.speak(text, queueMode, arguments, getUtteranceIdentifier());
          }
        }

        @Override
        public final int synthesize (TextToSpeech tts, CharSequence text, File file) {
          synchronized (arguments) {
            return tts.synthesizeToFile(text, arguments, file, getUtteranceIdentifier());
          }
        }
      };
    } else {
      paradigm = new Paradigm() {
        private final HashMap<String, String> arguments =
                  new HashMap<String, String>();

        @Override
        public final String get (String key) {
          synchronized (arguments) {
            return arguments.get(key);
          }
        }

        @Override
        public final void set (String key, String value) {
          synchronized (arguments) {
            arguments.put(key, value);
          }
        }

        @Override
        public final void set (String key, int value) {
          set(key, Integer.toString(value));
        }

        @Override
        public final void set (String key, float value) {
          set(key, Float.toString(value));
        }

        @Override
        public final int speak (TextToSpeech tts, CharSequence text, int queueMode) {
          synchronized (arguments) {
            return tts.speak(text.toString(), queueMode, arguments);
          }
        }

        @Override
        public final int synthesize (TextToSpeech tts, CharSequence text, File file) {
          synchronized (arguments) {
            return tts.synthesizeToFile(text.toString(), arguments, file.getAbsolutePath());
          }
        }
      };
    }
  }

  public final void setUtteranceIdentifier (String identifier) {
    synchronized (this) {
      paradigm.set(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, identifier);
    }
  }

  private Integer currentStream = null;

  public final Integer getStream () {
    synchronized (this) {
      return currentStream;
    }
  }

  public final void setStream (int value) {
    synchronized (this) {
      currentStream = value;
      paradigm.set(TextToSpeech.Engine.KEY_PARAM_STREAM, value);
    }
  }

  public final void setStream () {
    setStream(TextToSpeech.Engine.DEFAULT_STREAM);
  }

  public final void setStream (AudioStream stream) {
    setStream(stream.getStreamNumber());
  }

  public final void setLoudestStream () {
    setStream(AudioStream.getLoudestStream());
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
    synchronized (this) {
      return currentVolume;
    }
  }

  public final void setVolume (float value) {
    synchronized (this) {
      currentVolume = value;
      paradigm.set(TextToSpeech.Engine.KEY_PARAM_VOLUME, value);
    }
  }

  public final static float BALANCE_CENTER = 0.0f;
  public final static float BALANCE_RIGHT = 1.0f;
  public final static float BALANCE_LEFT = -BALANCE_RIGHT;
  private Float currentBalance = null;

  public final static boolean verifyBalance (float value) {
    return verifyRange("balance", value, BALANCE_LEFT, BALANCE_RIGHT);
  }

  public final Float getBalance () {
    synchronized (this) {
      return currentBalance;
    }
  }

  public final void setBalance (float value) {
    synchronized (this) {
      currentBalance = value;
      paradigm.set(TextToSpeech.Engine.KEY_PARAM_PAN, value);
    }
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
    int length = 4000;

    if (CommonUtilities.haveJellyBeanMR2) {
      try {
        length = tts.getMaxSpeechInputLength();
      } catch (IllegalArgumentException exception) {
        Log.w(LOG_TAG, "get maximum input length", exception);
      }
    }

    return length - 1; // Android returns the wrong value
  }

  public final int speak (TextToSpeech tts, CharSequence text, int queueMode) {
    synchronized (this) {
      return paradigm.speak(tts, text, queueMode);
    }
  }

  public final int synthesize (TextToSpeech tts, CharSequence text, File file) {
    synchronized (this) {
      return paradigm.synthesize(tts, text, file);
    }
  }
}
