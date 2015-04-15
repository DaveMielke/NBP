package org.nbp.b2g.ui;

import android.speech.tts.TextToSpeech;
import java.util.HashMap;
import android.util.Log;

public class SpeechDevice {
  private final static String LOG_TAG = SpeechDevice.class.getName();

  private final HashMap<String, String> ttsParameters = new HashMap<String, String>();
  private TextToSpeech ttsObject = null;
  private int ttsStatus = TextToSpeech.ERROR;

  private boolean isStarted () {
    return ttsStatus == TextToSpeech.SUCCESS;
  }

  public void say (String text) {
    if (text != null) {
      synchronized (this) {
        if (isStarted()) {
          ttsObject.speak(text, TextToSpeech.QUEUE_ADD, ttsParameters);
        }
      }
    }
  }

  public void stop () {
    synchronized (this) {
      if (isStarted()) {
        ttsObject.stop();
      }
    }
  }

  public void setVolume (float volume) {
    synchronized (this) {
      if (isStarted()) {
        ttsParameters.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, Float.toString(volume));
      }
    }
  }

  public void setBalance (float balance) {
    synchronized (this) {
      if (isStarted()) {
        ttsParameters.put(TextToSpeech.Engine.KEY_PARAM_PAN, Float.toString(balance));
      }
    }
  }

  public void setRate (float rate) {
    synchronized (this) {
      if (isStarted()) {
        ttsObject.setSpeechRate(rate);
      }
    }
  }

  public void setPitch (float pitch) {
    synchronized (this) {
      if (isStarted()) {
        ttsObject.setPitch(pitch);
      }
    }
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
