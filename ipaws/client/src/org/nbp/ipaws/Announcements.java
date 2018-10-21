package org.nbp.ipaws;

import org.nbp.common.CommonUtilities;

import android.util.Log;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
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

  private static class AnnouncementConversionThread extends Thread {
    private final static String LOG_TAG = AnnouncementConversionThread.class.getName();

    public AnnouncementConversionThread () {
      super("announcement-conversion");
    }

    private TextToSpeech ttsObject = null;
    private int ttsStatus = TextToSpeech.ERROR;
    private int ttsLengthLimit = 0;

    private final UtteranceProgressListener utteranceProgressListener =
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

    private final String fixText (String text) {
      text = text
             // remove newlines between words to reduce pauses within the speech
             .replaceAll("(\\w)\\s*\\n\\s*(\\w)", "$1 $2")
             // the last period of an ellipsis can be interpreted
             // as a decimal point when immediately before a digit
             .replaceAll("(\\.{2})(\\d)", "$1 $2")
             ;

      {
        int length = ttsLengthLimit;

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
              text.length(), ttsLengthLimit, length
            )
          );

          if (length > 0) text = text.substring(0, length);
        }
      }

      return text;
    }

    private final void convertAnnouncement (Announcement announcement) {
      String identifier = announcement.identifier;
      File file = getFile(identifier);
      String text = fixText(announcement.text);
      int status;

      if (CommonUtilities.haveLollipop) {
        status = ttsObject.synthesizeToFile(
          text, null, file, identifier
        );
      } else {
        HashMap<String, String> parameters = new HashMap<String, String>();

        parameters.put(
          TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
          identifier
        );

        status = ttsObject.synthesizeToFile(
          text, parameters, file.getAbsolutePath()
        );
      }

      if (status == TextToSpeech.SUCCESS) {
        Log.d(LOG_TAG, ("announcement conversion started: " + identifier));
      } else {
        Log.w(LOG_TAG, ("announcement conversion not started: " + identifier));
      }
    }

    private final boolean ttsStartEngine () {
      TextToSpeech.OnInitListener listener =
        new TextToSpeech.OnInitListener() {
          @Override
          public void onInit (int status) {
            Thread thread = AnnouncementConversionThread.this;

            synchronized (thread) {
              Log.d(LOG_TAG, ("TTS engine initialization status: " + status));
              ttsStatus = status;
              thread.notify();
            }
          }
        };

      synchronized (this) {
        Log.d(LOG_TAG, "starting TTS engine");

        ttsStatus = TextToSpeech.ERROR;
        ttsObject = new TextToSpeech(getContext(), listener, ApplicationSettings.SPEECH_ENGINE);

        try {
          Log.d(LOG_TAG, "waiting for TTS engine initialization");
          wait();
        } catch (InterruptedException exception) {
          Log.w(LOG_TAG, "TTS engine initialization wait interrupted");
        }

        switch (ttsStatus) {
          case TextToSpeech.SUCCESS:
            Log.d(LOG_TAG, "TTS engine initialized successfully");
            ttsLengthLimit = CommonUtilities.haveJellyBeanMR2?
                             ttsObject.getMaxSpeechInputLength():
                             4000;
            ttsLengthLimit -= 1; // Android returns the wrong value

            ttsObject.setOnUtteranceProgressListener(utteranceProgressListener);
            return true;

          default:
            Log.d(LOG_TAG, ("unexpected TTS engine initialization status: " + ttsStatus));
            /* fall through */
          case TextToSpeech.ERROR:
            Log.w(LOG_TAG, "TTS engine failed to initialize");
            ttsObject = null;
            return false;
        }
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
          if (announcement != null) convertAnnouncement(announcement);
        } catch (InterruptedException exception) {
        }
      }
    }

    public final void stopAnnouncementConversion () {
      synchronized (this) {
        if (ttsStatus == TextToSpeech.SUCCESS) {
          Log.d(LOG_TAG, "stopping announcement conversion");
          ttsObject.stop();
        }
      }
    }
  }

  private final static AnnouncementConversionThread announcementConversionThread;

  static {
    announcementConversionThread = new AnnouncementConversionThread();
    announcementConversionThread.start();
  }

  public static void stop () {
    announcementConversionThread.stopAnnouncementConversion();
  }
}
