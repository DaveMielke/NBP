package org.nbp.ipaws;

import android.util.Log;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import org.nbp.common.speech.SpeechParameters;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.net.URLEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.io.OutputStream;
import java.io.FileOutputStream;

public abstract class Announcements extends ApplicationComponent {
  private final static String LOG_TAG = Announcements.class.getName();

  private Announcements () {
    super();
  }

  private static File getDirectory () {
    File directory = getFilesDirectory("announcements");
    directory.setWritable(true, false);
    return directory;
  }

  private static File getFile (String identifier) {
    File directory = getDirectory();
    File file = new File(directory, identifier);

    if (!file.exists()) {
      String encoding = "UTF-8";

      try {
        file = new File(directory, URLEncoder.encode(identifier, encoding));
      } catch (UnsupportedEncodingException exception) {
        Log.w(LOG_TAG, ("unsupported character encoding: " + encoding));
      }
    }

    return file;
  }

  public static void remove (String identifier) {
    Log.d(LOG_TAG, ("removing announcement: " + identifier));
    File file = getFile(identifier);
    file.delete();
  }

  public static File create (String identifier, byte[] content) {
    File file = getFile(identifier);

    try {
      OutputStream stream = new FileOutputStream(file);
      stream.write(content);
      stream.close();

      file.setReadOnly();
      return file;
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("announcement file creation error: " + exception.getMessage()));
      file.delete();
    }

    return null;
  }

  private static class Announcement {
    public final String identifier;
    public final String text;

    public Announcement (String identifier, String text) {
      this.identifier = identifier;
      this.text = text;
    }
  }

  private final static LinkedBlockingDeque<Announcement> announcementQueue =
                   new LinkedBlockingDeque<Announcement>();

  public static void add (String identifier, String text) {
    Log.d(LOG_TAG, ("adding announcement: " + identifier));
    announcementQueue.offer(new Announcement(identifier, text));
  }

  private final static UtteranceProgressListener utteranceProgressListener =
    new UtteranceProgressListener() {
      @Override
      public void onStart (String identifier) {
        Log.d(LOG_TAG, ("utterance generation starting: " + identifier));
      }

      @Override
      public void onError (String identifier) {
        Log.w(LOG_TAG, ("utterance generation failed: " + identifier));
        remove(identifier);
      }

      @Override
      public void onError (String identifier, int error) {
        Log.w(LOG_TAG,
          String.format(
            "utterance generation error %d: %s",
            error, identifier
          )
        );

        remove(identifier);
      }

      @Override
      public void onStop (String identifier, boolean interrupted) {
        Log.w(LOG_TAG, 
          String.format(
            "utterance generation %s: %s",
            (interrupted? "interrupted": "stopped"), identifier
          )
        );

        remove(identifier);
      }

      @Override
      public void onDone (String identifier) {
        Log.d(LOG_TAG, ("utterance generation done: " + identifier));
        File file = getFile(identifier);

        if (file.exists()) {
          file.setExecutable(false, false);
          file.setWritable(false, false);
          file.setReadable(true, false);
          AlertPlayer.play(file, false);
        } else {
          Log.w(LOG_TAG, ("announcement file not found: " + file.getAbsolutePath()));
        }
      }
    };

  private final static Object TTS_LOCK = new Object();
  private static TextToSpeech ttsObject = null;
  private static String ttsEngine = null;
  private static int ttsMaximumLength = 0;

  public static void ttsStopConversion () {
    synchronized (TTS_LOCK) {
      if (ttsObject != null) {
        Log.d(LOG_TAG, "stopping announcement conversion");
        ttsObject.stop();
      }
    }
  }

  public static boolean ttsStartEngine () {
    synchronized (TTS_LOCK) {
      String engine = ApplicationSettings.SPEECH_ENGINE;
      if (engine.equals(ttsEngine)) return true;

      class Listener implements TextToSpeech.OnInitListener {
        public int engineStatus = TextToSpeech.ERROR;

        @Override
        public void onInit (int status) {
          synchronized (this) {
            Log.d(LOG_TAG, ("TTS engine initialization status: " + status));
            engineStatus = status;
            notify();
          }
        }
      }

      Log.d(LOG_TAG, ("starting TTS engine: " + engine));
      Listener listener = new Listener();
      TextToSpeech tts;

      synchronized (listener) {
        tts = new TextToSpeech(getContext(), listener, engine);

        try {
          Log.d(LOG_TAG, "waiting for TTS engine initialization");
          listener.wait();
        } catch (InterruptedException exception) {
          Log.w(LOG_TAG, "TTS engine initialization wait interrupted");
        }
      }

      switch (listener.engineStatus) {
        case TextToSpeech.SUCCESS: {
          Log.d(LOG_TAG, "TTS engine initialized successfully");
          tts.setOnUtteranceProgressListener(utteranceProgressListener);

          tts.setSpeechRate(SpeechParameters.RATE_REFERENCE);
          tts.setPitch(SpeechParameters.PITCH_REFERENCE);

          if (ttsObject != null) {
            ttsObject.stop();
            ttsObject.shutdown();
          }

          ttsObject = tts;
          ttsEngine = engine;
          ttsMaximumLength = SpeechParameters.getMaximumLength(tts);

          return true;
        }

        default:
          Log.d(LOG_TAG, ("unexpected TTS engine initialization status: " + listener.engineStatus));
          /* fall through */
        case TextToSpeech.ERROR:
          Log.w(LOG_TAG, "TTS engine failed to initialize");
          return false;
      }
    }
  }

  private static class AnnouncementConversionThread extends Thread {
    private final static String LOG_TAG = AnnouncementConversionThread.class.getName();

    public AnnouncementConversionThread () {
      super("announcement-conversion");
    }

    private final String fixText (String text) {
      text = text
             // remove newlines between words to reduce pauses within the speech
             .replaceAll("(\\w)\\s*\\n\\s*(\\w)", "$1 $2")
             // the last period of an ellipsis can be interpreted
             // as a decimal point when immediately before a digit
             .replaceAll("(\\.{2})(\\d)", "$1 $2")
             ;

      {
        int length = ttsMaximumLength;

        if (text.length() > length) {
          while (length > 0) {
            if (Character.isWhitespace(text.charAt(length))) break;
            length -= 1;
          }

          while (length > 0) {
            int last = length - 1;
            if (!Character.isWhitespace(text.charAt(last))) break;
            length = last;
          }

          Log.w(LOG_TAG,
            String.format(
              "announcement too long: %d > %d -> %d",
              text.length(), ttsMaximumLength, length
            )
          );

          if (length > 0) text = text.substring(0, length);
        }
      }

      return text;
    }

    private final void convertAnnouncement (Announcement announcement) {
      int status;

      SpeechParameters parameters = new SpeechParameters();
      parameters.setVolume(SpeechParameters.VOLUME_MAXIMUM);

      String identifier = announcement.identifier;
      parameters.setUtteranceIdentifier(identifier);

      synchronized (TTS_LOCK) {
        status = parameters.synthesize(
          ttsObject,
          fixText(announcement.text),
          getFile(identifier)
        );
      }

      if (status == TextToSpeech.SUCCESS) {
        Log.d(LOG_TAG, ("announcement conversion started: " + identifier));
      } else {
        Log.w(LOG_TAG, ("announcement conversion not started: " + identifier));
      }
    }

    @Override
    public void run () {
      while (!ttsStartEngine()) {
        try {
          Log.d(LOG_TAG, "delaying before TTS engine start retry");
          sleep(ApplicationParameters.TTS_RETRY_DELAY);
        } catch (InterruptedException exception) {
          Log.w(LOG_TAG, ("TTS engine start retry delay interrupted: " + exception.getMessage()));
        }
      }

      while (true) {
        Announcement announcement;

        try {
          announcement = announcementQueue.poll(1, TimeUnit.DAYS);
          if (announcement == null) continue;
        } catch (InterruptedException exception) {
          continue;
        }

        ttsStartEngine();
        convertAnnouncement(announcement);
      }
    }
  }

  private final static AnnouncementConversionThread announcementConversionThread;

  static {
    announcementConversionThread = new AnnouncementConversionThread();
    announcementConversionThread.start();
  }

  public static void stop () {
    ttsStopConversion();
  }
}
