package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Set;
import java.util.HashSet;

import android.util.Log;

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

  protected String getEngineName () {
    return null;
  }

  private final static int OK = TextToSpeech.SUCCESS;

  private String defaultEngineName = null;
  private String currentEngineName = null;

  private TextToSpeech currentEngine = null;
  private TextToSpeech newEngine = null;

  private final boolean hasStarted () {
    return currentEngine != null;
  }

  protected boolean isActive () {
    return hasStarted();
  }

  private CharSequence pendingText = null;

  private final SpeechParameters speechParameters = new SpeechParameters();
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

      if (hasStarted()) {
        try {
          if (!currentEngine.isSpeaking()) return true;
        } catch (IllegalArgumentException exception) {
          logSpeechFailure("test speaking", exception);
          return false;
        }

        logSpeechAction("stop");

        try {
          if (currentEngine.stop() == OK) return true;
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
          speechParameters.setUtteranceIdentifier(utterance);

          try {
            int queueMode = TextToSpeech.QUEUE_ADD;
            int status = speechParameters.speak(currentEngine, segment, queueMode);

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
        if (hasStarted()) {
          speechParameters.setVolume(volume);
          return true;
        }
      }
    }

    return false;
  }

  public final boolean setRate (float rate) {
    if (SpeechParameters.verifyRate(rate)) {
      synchronized (this) {
        if (hasStarted()) {
          try {
            if (currentEngine.setSpeechRate(rate) == OK) return true;
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
        if (hasStarted()) {
          try {
            if (currentEngine.setPitch(pitch) == OK) return true;
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
        if (hasStarted()) {
          speechParameters.setBalance(balance);
          return true;
        }
      }
    }

    return false;
  }

  private final TextSegmentGenerator makeSegmentGenerator () {
    int maximumLength = SpeechParameters.getMaximumLength(currentEngine);
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
    currentEngine.setOnUtteranceProgressListener(
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

  public final void startEngine () {
    synchronized (this) {
      String name = getEngineName();
      if (name == null) name = "";
      if (name.isEmpty()) name = TTS.getDefaultEngine();
      if (name.equals(currentEngineName)) return;

      {
        StringBuilder log = new StringBuilder();
        log.append("starting engine");

        log.append(": ");
        log.append(name);

        Log.d(LOG_TAG, log.toString());
      }

      class Engine {
        private final TextToSpeech engine;

        public final TextToSpeech getEngine () {
          return engine;
        }

        final TextToSpeech.OnInitListener listener =
          new TextToSpeech.OnInitListener() {
            @Override
            public void onInit (int status) {
              synchronized (TextPlayer.this) {
                if (engine == newEngine) {
                  newEngine = null;

                  if (status == TextToSpeech.SUCCESS) {
                    Log.d(LOG_TAG, "engine started successfully");

                    if (currentEngine != null) {
                      stopSpeaking();
                      currentEngine.shutdown();
                    }

                    currentEngine = engine;
                    initializeProperties();
                    segmentGenerator = makeSegmentGenerator();
                    setUtteranceProgressListener();

                    if (pendingText != null) {
                      CharSequence text = pendingText;
                      pendingText = null;
                      say(text);
                    }
                  } else {
                    Log.w(LOG_TAG, "engine start failed with status " + status);

                    try {
                      engine.shutdown();
                    } catch (IllegalArgumentException exception) {
                      logSpeechFailure("shut down", exception);
                    }

                    retryDelay.start();
                  }
                }
              }
            }
          };

        public Engine (String name) {
          engine = new TextToSpeech(CommonContext.getContext(), listener, name);
        }
      }

      if (newEngine != null) newEngine.shutdown();
      newEngine = new Engine(name).getEngine();
      currentEngineName = name;
    }
  }

  private final Timeout retryDelay =
    new Timeout(CommonParameters.SPEECH_RETRY_DELAY, "speech-retry-delay") {
      @Override
      public void run () {
        synchronized (TextPlayer.this) {
          startEngine();
        }
      }
    };

  public TextPlayer () {
    startEngine();
  }
}
