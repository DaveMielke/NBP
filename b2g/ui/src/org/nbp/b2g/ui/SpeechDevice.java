package org.nbp.b2g.ui;

import android.speech.tts.TextToSpeech;
import java.util.HashMap;
import android.util.Log;

public class SpeechDevice {
  private final static String LOG_TAG = SpeechDevice.class.getName();

  public final static float MINIMUM_VOLUME = 0.0f;
  public final static float MAXIMUM_VOLUME = 1.0f;
  public final static float DEFAULT_VOLUME = 1.0f;

  public final static float MINIMUM_BALANCE = -1.0f;
  public final static float MAXIMUM_BALANCE = 1.0f;
  public final static float DEFAULT_BALANCE = 0.0f;

  public final static float MINIMUM_RATE = 0.5f;
  public final static float MAXIMUM_RATE = 2.0f;
  public final static float DEFAULT_RATE = 1.0f;

  public final static float MINIMUM_PITCH = 0.5f;
  public final static float MAXIMUM_PITCH = 2.0f;
  public final static float DEFAULT_PITCH = 1.0f;

  private float currentVolume = DEFAULT_VOLUME;
  private float currentBalance = DEFAULT_BALANCE;
  private float currentRate = DEFAULT_RATE;
  private float currentPitch = DEFAULT_PITCH;

  private final static int OK = TextToSpeech.SUCCESS;

  private final HashMap<String, String> ttsParameters = new HashMap<String, String>();
  private TextToSpeech ttsObject = null;
  private int ttsStatus = TextToSpeech.ERROR;

  private boolean isStarted () {
    return ttsStatus == OK;
  }

  private boolean isActive () {
    return isStarted() && ApplicationParameters.SPEECH_ON;
  }

  public boolean stopSpeaking () {
    synchronized (this) {
      if (isStarted()) {
        if (!ttsObject.isSpeaking()) return true;
        if (ttsObject.stop() == OK) return true;
      }
    }

    return false;
  }

  public boolean say (String text) {
    if (text != null) {
      synchronized (this) {
        if (isActive()) {
          if (ttsObject.speak(text, TextToSpeech.QUEUE_ADD, ttsParameters) == OK) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private static boolean verifyRange (String label, float value, float minimum, float maximum) {
    if ((value >= minimum) && (value <= maximum)) return true;
    Log.w(LOG_TAG, ("invalid " + label + ": " + value));
    return false;
  }

  public float getVolume () {
    return currentVolume;
  }

  public boolean setVolume (float volume) {
    if (verifyRange("volume", volume, MINIMUM_VOLUME, MAXIMUM_VOLUME)) {
      synchronized (this) {
        if (isStarted()) {
          ttsParameters.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, Float.toString(volume));
          currentVolume = volume;
          return true;
        }
      }
    }

    return false;
  }

  public boolean setVolume () {
    return setVolume(ApplicationParameters.SPEECH_VOLUME);
  }

  public float getBalance () {
    return currentBalance;
  }

  public boolean setBalance (float balance) {
    if (verifyRange("balance", balance, MINIMUM_BALANCE, MAXIMUM_BALANCE)) {
      synchronized (this) {
        if (isStarted()) {
          ttsParameters.put(TextToSpeech.Engine.KEY_PARAM_PAN, Float.toString(balance));
          currentBalance = balance;
          return true;
        }
      }
    }

    return false;
  }

  public boolean setBalance () {
    return setBalance(ApplicationParameters.SPEECH_BALANCE);
  }

  public float getRate () {
    return currentRate;
  }

  public boolean setRate (float rate) {
    if (verifyRange("rate", rate, MINIMUM_RATE, MAXIMUM_RATE)) {
      synchronized (this) {
        if (isStarted()) {
          if (ttsObject.setSpeechRate(rate) == OK) {
            currentRate = rate;
            return true;
          }
        }
      }
    }

    return false;
  }

  public boolean setRate () {
    return setRate(ApplicationParameters.SPEECH_RATE);
  }

  public float getPitch () {
    return currentPitch;
  }

  public boolean setPitch (float pitch) {
    if (verifyRange("pitch", pitch, MINIMUM_PITCH, MAXIMUM_PITCH)) {
      synchronized (this) {
        if (isStarted()) {
          if (ttsObject.setPitch(pitch) == OK) {
            currentPitch = pitch;
            return true;
          }
        }
      }
    }

    return false;
  }

  public boolean setPitch () {
    return setPitch(ApplicationParameters.SPEECH_PITCH);
  }

  public void revertSettings () {
    setVolume();
    setBalance();
    setRate();
    setPitch();
  }

  private Timeout ttsRetry = new Timeout(ApplicationParameters.SPEECH_RETRY_DELAY) {
    @Override
    public void run () {
      synchronized (SpeechDevice.this) {
        ttsStart();
      }
    }
  };

  private void ttsStart () {
    synchronized (this) {
      Log.d(LOG_TAG, "speech device starting");

      TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit (int status) {
          synchronized (SpeechDevice.this) {
            ttsStatus = status;

            if (isStarted()) {
              Log.d(LOG_TAG, "speech device started");
              revertSettings();
            } else {
              Log.d(LOG_TAG, "speech device failed with status " + ttsStatus);

              ttsObject.shutdown();
              ttsObject = null;

              ttsRetry.start();
            }
          }
        }
      };

      ttsObject = new TextToSpeech(ApplicationContext.get(), onInitListener);
    }
  }

  public SpeechDevice () {
    if (ApplicationParameters.ENABLE_SPEECH_DEVICE) ttsStart();
  }
}
