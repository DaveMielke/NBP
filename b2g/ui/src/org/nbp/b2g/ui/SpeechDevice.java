package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.nbp.common.Timeout;

import android.util.Log;
import android.os.Build;

import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import android.text.Spanned;

public class SpeechDevice {
  private final static String LOG_TAG = SpeechDevice.class.getName();

  public final static float MAXIMUM_VOLUME = 1.0f;
  public final static float MINIMUM_VOLUME = 0.0f;

  public final static float MAXIMUM_BALANCE = 1.0f;
  public final static float MINIMUM_BALANCE = -MAXIMUM_BALANCE;

  public final static float MAXIMUM_RATE = 4.0f;
  public final static float MINIMUM_RATE = 1.0f / 3.0f;

  public final static float MAXIMUM_PITCH = 2.0f;
  public final static float MINIMUM_PITCH = 1.0f / 2.0f;

  private final AudioManager audioManager;

  private final static Map<Integer, String> audioStreams = new LinkedHashMap<Integer, String>() {
    {
      put(AudioManager.STREAM_MUSIC, "music");
      put(AudioManager.STREAM_NOTIFICATION, "notification");
      put(AudioManager.STREAM_ALARM, "alarm");
      put(AudioManager.STREAM_RING, "ring");
      put(AudioManager.STREAM_SYSTEM, "system");
      put(AudioManager.STREAM_VOICE_CALL, "voice");
      put(AudioManager.STREAM_DTMF, "DTMF");
    }
  };

  private final static int OK = TextToSpeech.SUCCESS;

  private final HashMap<String, String> ttsParameters = new HashMap<String, String>();
  private TextToSpeech ttsObject = null;
  private int ttsStatus = TextToSpeech.ERROR;
  private int maximumLength;

  private final void setParameter (String parameter, String value) {
    synchronized (ttsParameters) {
      ttsParameters.put(parameter, value);
    }
  }

  public final void setAudioStream (int stream) {
    setParameter(TextToSpeech.Engine.KEY_PARAM_STREAM, Integer.toString(stream));
  }

  public final void setAudioStream () {
    setAudioStream(TextToSpeech.Engine.DEFAULT_STREAM);
  }

  public final void selectLoudestAudioStream () {
    int selected = -1;
    float loudest = -1f;

    for (int stream : audioStreams.keySet()) {
      int current = audioManager.getStreamVolume(stream);
      int maximum = audioManager.getStreamMaxVolume(stream);
      float volume = (float)current / (float)maximum;

      if (volume > loudest) {
        loudest = volume;
        selected = stream;
      }

      if (loudest == 1f) break;
    }

    setAudioStream(selected);
  }

  private final boolean isStarted () {
    return ttsStatus == OK;
  }

  private final boolean isActive () {
    if (!isStarted()) return false;
    if (!ApplicationSettings.SPEECH_ENABLED) return false;
    if (ApplicationSettings.SLEEP_TALK) return true;
    return ApplicationContext.isAwake();
  }

  public final boolean stopSpeaking () {
    synchronized (this) {
      if (isStarted()) {
        if (!ttsObject.isSpeaking()) return true;
        if (ttsObject.stop() == OK) return true;
      }
    }

    return false;
  }

  public final boolean say (String text) {
    if (text != null) {
      synchronized (this) {
        if (isActive()) {
          while (true) {
            if (text.length() == 0) return true;

            int nl = text.indexOf('\n');
            String line;

            if (nl == -1) {
              line = text;
              text = "";
            } else {
              line = text.substring(0, nl);
              text = text.substring(nl+1);
            }

            while (true) {
              int length = line.length();
              if (length == 0) break;
              String segment;

              if (length > maximumLength) {
                int end = line.lastIndexOf(' ', maximumLength);
                if (end == -1) end = maximumLength;
                segment = line.substring(0, end);
                line = line.substring(end+1);
              } else {
                segment = line;
                line = "";
              }

              if (ttsObject.speak(segment, TextToSpeech.QUEUE_ADD, ttsParameters) != OK) return false;
            }
          }
        }
      }
    }

    return false;
  }

  public final boolean say (CharSequence text) {
    if (!(text instanceof Spanned)) return say(text.toString());

    Spanned spanned = (Spanned)text;
    int length = spanned.length();
    int start = 0;

    while (start < length) {
      int end = spanned.nextSpanTransition(start, length, SpeechSpan.class);
      SpeechSpan[] spans = spanned.getSpans(start, start+1, SpeechSpan.class);
      String segment;

      if (spans.length == 0) {
        segment = spanned.subSequence(start, end).toString();
      } else {
        segment = spans[0].getText();
      }

      if (!say(segment)) return false;
      start = end;
    }

    return true;
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

  public final boolean setVolume (float volume) {
    if (verifyRange("volume", volume, MINIMUM_VOLUME, MAXIMUM_VOLUME)) {
      synchronized (this) {
        if (isStarted()) {
          setParameter(TextToSpeech.Engine.KEY_PARAM_VOLUME, Float.toString(volume));
          return true;
        }
      }
    }

    return false;
  }

  public final boolean setBalance (float balance) {
    if (verifyRange("balance", balance, MINIMUM_BALANCE, MAXIMUM_BALANCE)) {
      synchronized (this) {
        if (isStarted()) {
          setParameter(TextToSpeech.Engine.KEY_PARAM_PAN, Float.toString(balance));
          return true;
        }
      }
    }

    return false;
  }

  public final boolean setRate (float rate) {
    if (verifyRange("rate", rate, MINIMUM_RATE, MAXIMUM_RATE)) {
      synchronized (this) {
        if (isStarted()) {
          if (ttsObject.setSpeechRate(rate) == OK) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public final boolean setPitch (float pitch) {
    if (verifyRange("pitch", pitch, MINIMUM_PITCH, MAXIMUM_PITCH)) {
      synchronized (this) {
        if (isStarted()) {
          if (ttsObject.setPitch(pitch) == OK) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public final void restoreControls () {
    Control[] controls = new Control[] {
      Controls.getSpeechVolumeControl(),
      Controls.getSpeechBalanceControl(),
      Controls.getSpeechRateControl(),
      Controls.getSpeechPitchControl()
    };

    Controls.forEachControl(controls, Controls.restoreCurrentValue);
  }

  private final Timeout ttsRetry = new Timeout(ApplicationParameters.SPEECH_RETRY_DELAY, "speech-device-retry-delay") {
    @Override
    public void run () {
      synchronized (SpeechDevice.this) {
        ttsStart();
      }
    }
  };

  private final void ttsStart () {
    synchronized (this) {
      Log.d(LOG_TAG, "speech device starting");

      TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit (int status) {
          synchronized (SpeechDevice.this) {
            ttsStatus = status;

            if (isStarted()) {
              Log.d(LOG_TAG, "speech device started");
              restoreControls();

              if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
                maximumLength = ttsObject.getMaxSpeechInputLength();
              } else {
                maximumLength = 4000;
              }
            } else {
              Log.d(LOG_TAG, "speech device failed with status " + ttsStatus);

              ttsObject.shutdown();
              ttsObject = null;

              ttsRetry.start();
            }
          }
        }
      };

      ttsObject = new TextToSpeech(ApplicationContext.getContext(), onInitListener);
    }
  }

  public SpeechDevice () {
    audioManager = ApplicationContext.getAudioManager();
    if (ApplicationParameters.ENABLE_SPEECH_DEVICE) ttsStart();
  }
}
