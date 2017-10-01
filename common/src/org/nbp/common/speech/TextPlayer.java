package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.os.Build;
import android.os.Bundle;

import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import android.text.Spanned;

public abstract class TextPlayer {
  private final static String LOG_TAG = TextPlayer.class.getName();

  protected abstract void initializeProperties ();

  protected boolean isLogging () {
    return false;
  }

  private final void logSpeechAction (CharSequence... phrases) {
    if (isLogging()) {
      StringBuilder sb = new StringBuilder();

      for (CharSequence phrase : phrases) {
        if (phrase == null) continue;
        if (phrase.length() == 0) continue;

        if (sb.length() > 0) sb.append(": ");
        sb.append(phrase);
      }

      if (sb.length() > 0) {
        sb.insert(0, "speech action: ");
        Log.d(LOG_TAG, sb.toString());
      }
    }
  }

  private final void logSpeechFailure (String action, Exception exception) {
    Log.w(LOG_TAG, String.format(
      "speech failure: %s: %s", action, exception.getMessage()
    ));
  }

  private final Bundle ttsParameterBundle = new Bundle();
  private final HashMap<String, String> ttsParameterMap =
            new HashMap<String, String>();

  private final void setParameter (String key, String value) {
    if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
      synchronized (ttsParameterBundle) {
        ttsParameterBundle.putString(key, value);
      }
    } else {
      synchronized (ttsParameterMap) {
        ttsParameterMap.put(key, value);
      }
    }
  }

  private final void setParameter (String key, int value) {
    setParameter(key, Integer.toString(value));
  }

  private final void setParameter (String key, float value) {
    setParameter(key, Float.toString(value));
  }

  private final static int OK = TextToSpeech.SUCCESS;
  private int ttsStatus = TextToSpeech.ERROR;

  private final AudioManager audioManager;

  private final static AudioStream[] audioStreams = {
    AudioStream.MUSIC,
    AudioStream.NOTIFICATION,
    AudioStream.ALARM,
    AudioStream.RING,
    AudioStream.SYSTEM,
    AudioStream.CALL,
    AudioStream.DTMF
  };

  public final void setAudioStream (int stream) {
    setParameter(TextToSpeech.Engine.KEY_PARAM_STREAM, stream);
  }

  public final void setAudioStream () {
    setAudioStream(TextToSpeech.Engine.DEFAULT_STREAM);
  }

  public final void setAudioStream (AudioStream stream) {
    setAudioStream(stream.getStreamNumber());
  }

  public final AudioStream getLoudestAudioStream () {
    AudioStream loudestStream = null;
    float loudestVolume = -1f;

    for (AudioStream stream : audioStreams) {
      int current = stream.getCurrentVolume();
      int maximum = stream.getMaximumVolume();
      float volume = (float)current / (float)maximum;

      if (volume > loudestVolume) {
        loudestVolume = volume;
        loudestStream = stream;
      }

      if (loudestVolume == 1f) break;
    }

    return loudestStream;
  }

  public final void selectLoudestAudioStream () {
    setAudioStream(getLoudestAudioStream());
  }

  private final boolean isStarted () {
    return ttsStatus == OK;
  }

  protected boolean isActive () {
    return isStarted();
  }

  private TextToSpeech ttsObject = null;
  private CharSequence pendingSpeakText = null;

  private TextSegmentGenerator textSegmentGenerator;
  private boolean isSpeaking = false;
  private int utteranceIdentifier = 0;

  private final void speakingStopped () {
    isSpeaking = false;
    if (textSegmentGenerator != null) textSegmentGenerator.removeText();
  }

  public final boolean stopSpeaking () {
    synchronized (this) {
      speakingStopped();

      if (isStarted()) {
        try {
          if (!ttsObject.isSpeaking()) return true;
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("test speaking", exception);
          return false;
        }

        logSpeechAction("stop");

        try {
          if (ttsObject.stop() == OK) return true;
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("stop speaking", exception);
        }
      }
    }

    return false;
  }

  private final boolean speak () {
    synchronized (this) {
      if (isActive()) {
        CharSequence segment = textSegmentGenerator.nextSegment();

        if (segment == null) {
          isSpeaking = false;
          return true;
        }

        String utterance = Integer.toString(++utteranceIdentifier);
        logSpeechAction("speak", utterance, segment);

        try {
          int queueMode = TextToSpeech.QUEUE_ADD;
          int status;

          if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
            status = ttsObject.speak(segment, queueMode, ttsParameterBundle, utterance);
          } else {
            setParameter(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
            status = ttsObject.speak(segment.toString(), queueMode, ttsParameterMap);
          }

          if (status == OK) {
            return true;
          }
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("speak", exception);
        }
      }
    }

    return false;
  }

  public final boolean say (CharSequence text) {
    synchronized (this) {
      if (isActive()) {
        textSegmentGenerator.addText(text);

        if (!isSpeaking) {
          isSpeaking = true;
          speak();
        }
      } else {
        pendingSpeakText = text;
      }
    }

    return false;
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
          setParameter(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume);
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

  public final boolean setBalance (float balance) {
    if (verifyRange("balance", balance, SpeechParameters.BALANCE_MINIMUM, SpeechParameters.BALANCE_MAXIMUM)) {
      synchronized (this) {
        if (isStarted()) {
          setParameter(TextToSpeech.Engine.KEY_PARAM_PAN, balance);
          return true;
        }
      }
    }

    return false;
  }

  private final int getMaximumLength () {
    if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
      try {
        return ttsObject.getMaxSpeechInputLength();
      } catch (IllegalArgumentException exception) {
        logSpeechFailure("get length", exception);
      }
    }

    return 4000;
  }

  private final TextSegmentGenerator makeTextSegmentGenerator () {
    TextSegmentGenerator.OuterGenerator speechSpanGenerator =
      new TextSegmentGenerator.OuterGenerator() {
        @Override
        protected final CharSequence generateSegment () {
          if (remainingText instanceof Spanned) {
            Spanned text = (Spanned)remainingText;

            if (text.length() > 0) {
              SpeechSpan[] spans = text.getSpans(0, 0, SpeechSpan.class);

              if (spans != null) {
                if (spans.length > 0) {
                  SpeechSpan span = spans[0];
                  removeText(text.getSpanEnd(span));
                  return span.getText();
                }
              }

              {
                int length = text.length();
                int end = text.nextSpanTransition(0, length, SpeechSpan.class);

                if (end < length) {
                  CharSequence segment = text.subSequence(0, end);
                  removeText(end);
                  return segment;
                }
              }
            }
          }

          return null;
        }
      };

    return new TextSegmentGenerator.Builder()
                                   .add(speechSpanGenerator)
                                   .add('\n')
                                   .add('\t')
                                   .add(getMaximumLength())
                                   .build();
  }

  private final void setUtteranceProgressListener () {
    ttsObject.setOnUtteranceProgressListener(
      new UtteranceProgressListener() {
        // added in API Level 15 (Icecream Sandwich)
        public void onStart (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("starting", utterance);
          }
        }

        // added in API Level 15 (Icecream Sandwich)
        public void onError (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("error", utterance);
            speakingStopped();
          }
        }

        // added in API Level 21 (Lollipop)
        public void onError (String utterance, int error) {
          synchronized (TextPlayer.this) {
            logSpeechAction(("error " + Integer.toString(error)), utterance);
            speakingStopped();
          }
        }

        // added in API Level 23 (MarshMallow)
        public void onStop (String utterance, boolean interrupted) {
          synchronized (TextPlayer.this) {
            logSpeechAction("stopped", utterance);
            speakingStopped();
          }
        }

        // added in API Level 15 (Icecream Sandwich)
        public void onDone (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("done", utterance);
            speak();
          }
        }
      }
    );
  }

  private final Timeout ttsRetry = new Timeout(CommonParameters.SPEECH_RETRY_DELAY, "speech-retry-delay") {
    @Override
    public void run () {
      synchronized (TextPlayer.this) {
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
          synchronized (TextPlayer.this) {
            ttsStatus = status;

            if (isStarted()) {
              Log.d(LOG_TAG, "speech started");
              initializeProperties();
              textSegmentGenerator = makeTextSegmentGenerator();
              setUtteranceProgressListener();

              if (pendingSpeakText != null) {
                CharSequence text = pendingSpeakText;
                pendingSpeakText = null;
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

  public TextPlayer () {
    audioManager = CommonContext.getAudioManager();
    ttsStart();
  }
}
