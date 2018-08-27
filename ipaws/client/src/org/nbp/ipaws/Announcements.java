package org.nbp.ipaws;

import android.util.Log;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import java.io.File;

public abstract class Announcements extends ApplicationComponent {
  private final static String LOG_TAG = Announcements.class.getName();

  private Announcements () {
    super();
  }

  public static File getAnnouncementsDirectory () {
    File directory = getFilesDirectory("announcements");
    directory.setWritable(true, false);
    return directory;
  }

  private static File getAnnouncementFile (String identifier) {
    return new File(getAnnouncementsDirectory(), identifier);
  }

  public static void add (String identifier, String text) {
    Log.d(LOG_TAG, ("adding announcement: " + identifier));
    announcementQueue.offer(new Announcement(identifier, text));
  }

  public static void remove (String identifier) {
    Log.d(LOG_TAG, ("removing announcement: " + identifier));
    File file = getAnnouncementFile(identifier);
    file.delete();
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

  private static class ConversionThread extends Thread {
    private final static String LOG_TAG = ConversionThread.class.getName();

    public ConversionThread () {
      super("announcement-conversion");
    }

    private TextToSpeech ttsObject = null;
    private int ttsStatus = TextToSpeech.ERROR;

    private final UtteranceProgressListener utteranceProgressListener =
      new UtteranceProgressListener() {
        @Override
        public void onStart (String identifier) {
          Log.d(LOG_TAG, ("utterance starting: " + identifier));
        }

        @Override
        public void onError (String identifier) {
          Log.d(LOG_TAG, ("utterance failed: " + identifier));
          remove(identifier);
        }

        @Override
        public void onDone (String identifier) {
          Log.d(LOG_TAG, ("utterance done: " + identifier));
          File file = getAnnouncementFile(identifier);

          if (file.exists()) {
            file.setReadOnly();
            AlertPlayer.play(file, false);
          } else {
            Log.w(LOG_TAG, ("announcement file not created: " + file.getAbsolutePath()));
          }
        }
      };

    private final void convertAnnouncement (Announcement announcement) {
      File file = getAnnouncementFile(announcement.identifier);
      HashMap<String, String> parameters = new HashMap<String, String>();

      parameters.put(
        TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
        announcement.identifier
      );

      int status = ttsObject.synthesizeToFile(
        announcement.text,
        parameters,
        file.getAbsolutePath()
      );

      if (status == TextToSpeech.SUCCESS) {
      } else {
      }
    }

    private final boolean startEngine () {
      TextToSpeech.OnInitListener listener =
        new TextToSpeech.OnInitListener() {
          @Override
          public void onInit (int status) {
            Thread thread = ConversionThread.this;

            synchronized (thread) {
              Log.d(LOG_TAG, ("TTS engine initialization status: " + status));
              ttsStatus = status;
              thread.notify();
            }
          }
        };

      synchronized (this) {
        Log.d(LOG_TAG, "starting TTS engine");
        ttsObject = new TextToSpeech(getContext(), listener);

        try {
          Log.d(LOG_TAG, "waiting for TTS engine initialization");
          wait();
        } catch (InterruptedException exception) {
          Log.d(LOG_TAG, "TTS engine initialization wait interrupted");
        }

        switch (ttsStatus) {
          case TextToSpeech.SUCCESS:
            Log.d(LOG_TAG, "TTS engine initialized successfully");
            ttsObject.setOnUtteranceProgressListener(utteranceProgressListener);
            return true;

          default:
            Log.d(LOG_TAG, "TTS engine failed to initialize");
            ttsObject = null;
            return false;
        }
      }
    }

    @Override
    public void run () {
      if (startEngine()) {
        while (true) {
          Announcement announcement;

          try {
            announcement = announcementQueue.poll(1, TimeUnit.DAYS);
            if (announcement != null) convertAnnouncement(announcement);
          } catch (InterruptedException exception) {
          }
        }
      }
    }
  }

  static {
    new ConversionThread().start();
  }
}
