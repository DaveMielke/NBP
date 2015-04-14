package org.nbp.b2g.ui;

import android.speech.tts.TextToSpeech;
import java.util.HashMap;
import android.util.Log;

public class SpeechDevice {
  private final static String LOG_TAG = SpeechDevice.class.getName();

  private final HashMap<String, String> ttsParameters = new HashMap<String, String>();
  private TextToSpeech ttsObject;
  private int ttsStatus;

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

  private Timeout ttsRetry = new Timeout(ApplicationParameters.SPEECH_RETRY_DELAY) {
    @Override
    public void run () {
      synchronized (SpeechDevice.this) {
        ttsStart();
      }
    }
  };

  private void ttsStart () {
    Log.d(LOG_TAG, "speech device starting");

    synchronized (this) {
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

      ttsObject = new TextToSpeech(ApplicationHooks.getContext(), onInitListener);
    }
  }

  public SpeechDevice () {
    ttsObject = null;
    ttsStatus = TextToSpeech.ERROR;
    ttsStart();
  }
}
