package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.util.Log;
import android.os.Build;

import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import android.text.Spanned;

public abstract class TextSpeaker {
  private final static String LOG_TAG = TextSpeaker.class.getName();

  protected abstract void initializeProperties ();

  protected boolean isLogging () {
    return false;
  }

  private final AudioManager audioManager;

  private final static Map<Integer, String> audioStreams =
         new LinkedHashMap<Integer, String>() {
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

  private final HashMap<String, String> ttsParameters =
            new HashMap<String, String>();

  private final static int OK = TextToSpeech.SUCCESS;
  private int ttsStatus = TextToSpeech.ERROR;

  private TextToSpeech ttsObject = null;
  private int maximumLength;
  private String pendingSayText = null;

  private final void logSpeechFailure (String action, Exception exception) {
    Log.w(LOG_TAG, String.format(
      "speech failure: %s: %s", action, exception.getMessage()
    ));
  }

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

  protected boolean isActive () {
    return isStarted();
  }

  public final boolean stopSpeaking () {
    synchronized (this) {
      if (isStarted()) {
        try {
          if (!ttsObject.isSpeaking()) return true;
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("test speaking", exception);
          return false;
        }

        if (isLogging()) {
          Log.d(LOG_TAG, "speech: stop");
        }

        try {
          if (ttsObject.stop() == OK) return true;
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("stop speaking", exception);
        }
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
                line = line.substring(end);
              } else {
                segment = line;
                line = "";
              }

              if (isLogging()) {
                Log.d(LOG_TAG, ("speech: say: " + segment));
              }

              try {
                if (ttsObject.speak(segment, TextToSpeech.QUEUE_ADD, ttsParameters) != OK) return false;
              } catch (IllegalArgumentException exception) {
                logSpeechFailure("speak", exception);
                return false;
              }
            }
          }
        } else {
          pendingSayText = text;
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
    if (verifyRange("volume", volume, SpeechParameters.VOLUME_MINIMUM, SpeechParameters.VOLUME_MAXIMUM)) {
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
    if (verifyRange("balance", balance, SpeechParameters.BALANCE_MINIMUM, SpeechParameters.BALANCE_MAXIMUM)) {
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
    if (verifyRange("rate", rate, SpeechParameters.RATE_MINIMUM, SpeechParameters.RATE_MAXIMUM)) {
      synchronized (this) {
        if (isStarted()) {
          try {
            if (ttsObject.setSpeechRate(rate) == OK) return true;
          } catch (IllegalArgumentException exception) {
            logSpeechFailure("set rate", exception);
          }
        }
      }
    }

    return false;
  }

  public final boolean setPitch (float pitch) {
    if (verifyRange("pitch", pitch, SpeechParameters.PITCH_MINIMUM, SpeechParameters.PITCH_MAXIMUM)) {
      synchronized (this) {
        if (isStarted()) {
          try {
            if (ttsObject.setPitch(pitch) == OK) return true;
          } catch (IllegalArgumentException exception) {
            logSpeechFailure("set pitch", exception);
          }
        }
      }
    }

    return false;
  }

  private final Timeout ttsRetry = new Timeout(CommonParameters.SPEECH_RETRY_DELAY, "speech-retry-delay") {
    @Override
    public void run () {
      synchronized (TextSpeaker.this) {
        ttsStart();
      }
    }
  };

  private final void ttsStart () {
    synchronized (this) {
      Log.d(LOG_TAG, "speech starting");

      TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit (int status) {
          synchronized (TextSpeaker.this) {
            ttsStatus = status;

            if (isStarted()) {
              Log.d(LOG_TAG, "speech started");
              initializeProperties();
              maximumLength = 4000;

              if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
                try {
                  maximumLength = ttsObject.getMaxSpeechInputLength();
                } catch (IllegalArgumentException exception) {
                  logSpeechFailure("get length", exception);
                }
              }

              if (pendingSayText != null) {
                String text = pendingSayText;
                pendingSayText = null;
                say(text);
              }
            } else {
              Log.d(LOG_TAG, "speech failed with status " + ttsStatus);

              try {
                ttsObject.shutdown();
              } catch (IllegalArgumentException exception) {
                logSpeechFailure("shut down", exception);
              }

              ttsObject = null;
              ttsRetry.start();
            }
          }
        }
      };

      ttsObject = new TextToSpeech(CommonContext.getContext(), onInitListener);
    }
  }

  public TextSpeaker () {
    audioManager = CommonContext.getAudioManager();
    ttsStart();
  }
}
