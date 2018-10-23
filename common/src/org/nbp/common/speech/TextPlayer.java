package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import android.util.Log;
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
    if (CommonUtilities.haveLollipop) {
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
      float volume = stream.getNormalizedVolume();

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
  private CharSequence pendingText = null;

  private TextSegmentGenerator segmentGenerator;
  private int utteranceIdentifier = 0;
  private final Set<String> activeUtterances = new HashSet<String>();

  private final void cancelSpeaking () {
    synchronized (this) {
      if (segmentGenerator != null) segmentGenerator.removeText();
      activeUtterances.clear();
    }
  }

  public final boolean stopSpeaking () {
    synchronized (this) {
      cancelSpeaking();

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

  private final boolean startSpeaking () {
    synchronized (this) {
      if (isActive()) {
        while (true) {
          if (activeUtterances.size() >= CommonParameters.SPEECH_SYNTHESIS_CONCURRENCY) {
            return true;
          }

          CharSequence segment = segmentGenerator.nextSegment();
          if (segment == null) return true;

          String utterance = Integer.toString(++utteranceIdentifier);
          logSpeechAction("speak", utterance, segment);

          try {
            int queueMode = TextToSpeech.QUEUE_ADD;
            int status;

            if (CommonUtilities.haveLollipop) {
              status = ttsObject.speak(segment, queueMode, ttsParameterBundle, utterance);
            } else {
              setParameter(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
              status = ttsObject.speak(segment.toString(), queueMode, ttsParameterMap);
            }

            if (status != OK) break;
            activeUtterances.add(utterance);
          } catch (IllegalArgumentException exception) {
            logSpeechFailure("speak", exception);
            break;
          }
        }
      }
    }

    return false;
  }

  public final boolean say (CharSequence text) {
    synchronized (this) {
      if (isActive()) {
        segmentGenerator.addText(text);
        if (!startSpeaking()) return false;
      } else {
        pendingText = text;
      }
    }

    return true;
  }

  public final boolean setVolume (float volume) {
    if (SpeechParameters.verifyVolume(volume)) {
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
    if (SpeechParameters.verifyRate(rate)) {
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
    if (SpeechParameters.verifyPitch(pitch)) {
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
    if (SpeechParameters.verifyBalance(balance)) {
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
    if (CommonUtilities.haveJellyBeanMR2) {
      try {
        return ttsObject.getMaxSpeechInputLength();
      } catch (IllegalArgumentException exception) {
        logSpeechFailure("get length", exception);
      }
    }

    return 4000;
  }

  private final TextSegmentGenerator makeSegmentGenerator () {
    int maximumLength = getMaximumLength() - 1;
    logSpeechAction("maximum length", Integer.toString(maximumLength));

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
                                   .add(maximumLength, true)
                                   .build();
  }

  private final void setUtteranceProgressListener () {
    ttsObject.setOnUtteranceProgressListener(
      new UtteranceProgressListener() {
        @Override
        public void onStart (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("starting", utterance);
          }
        }

        @Override
        public void onError (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("failed", utterance);
            cancelSpeaking();
          }
        }

        @Override
        public void onError (String utterance, int error) {
          synchronized (TextPlayer.this) {
            logSpeechAction(("error " + Integer.toString(error)), utterance);
            cancelSpeaking();
          }
        }

        @Override
        public void onStop (String utterance, boolean interrupted) {
          synchronized (TextPlayer.this) {
            logSpeechAction((interrupted? "interrupted": "stopped"), utterance);
            cancelSpeaking();
          }
        }

        @Override
        public void onDone (String utterance) {
          synchronized (TextPlayer.this) {
            logSpeechAction("done", utterance);
            activeUtterances.remove(utterance);
            startSpeaking();
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
              segmentGenerator = makeSegmentGenerator();
              setUtteranceProgressListener();

              if (pendingText != null) {
                CharSequence text = pendingText;
                pendingText = null;
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
