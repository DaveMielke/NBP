package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;

import java.io.File;
import java.io.FileInputStream;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;

import java.util.Collection;

public abstract class InputProcessor {
  private final static String LOG_TAG = InputProcessor.class.getName();

  protected abstract boolean handleLine (String text, int number);

  public static void close (Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "close error", exception);
    }
  }

  public final boolean processInput (Reader reader) {
    BufferedReader buffer;

    if (reader instanceof BufferedReader) {
      buffer = (BufferedReader)reader;
    } else {
      buffer = new BufferedReader(reader);
    }

    try {
      for (int number=0; true; number+=1) {
        try {
          String text = buffer.readLine();
          if (text == null) break;
          if (!handleLine(text, number)) break;
        } catch (IOException exception) {
          Log.w(LOG_TAG, "line read error", exception);
          return false;
        }
      }
    } finally {
      if (buffer != reader) close(buffer);
    }

    return true;
  }

  public final boolean processInput (InputStream stream) {
    String encoding = "UTF8";

    try {
      Reader reader = new InputStreamReader(stream, encoding);

      try {
        return processInput(reader);
      } finally {
        close(reader);
      }
    } catch (UnsupportedEncodingException exception) {
      Log.w(LOG_TAG, "unsupported input encoding: " + encoding);
    }

    return false;
  }

  public final boolean processInput (File file) {
    try {
      InputStream stream = new FileInputStream(file);

      try {
        Log.d(LOG_TAG, "begin file: " + file);
        boolean processed = processInput(stream);
        Log.d(LOG_TAG, "end file: " + file);
        return processed;
      } finally {
        close(stream);
      }
    } catch (FileNotFoundException exception) {
      Log.w(LOG_TAG, "file not found: " + file.toString());
    }

    return false;
  }

  public final boolean processInput (String... names) {
    Context context = ApplicationContext.getContext();
    if (context == null) return false;

    File directory = ApplicationUtilities.getExternalStorageDirectory();
    AssetManager assets = context.getAssets();

    for (String name : names) {
      if (directory != null) {
        File file = new File(directory, name);

        if (file.exists()) {
          return processInput(file);
        }
      }

      if (assets != null) {
        try {
          InputStream stream = assets.open(name);

          try {
            Log.d(LOG_TAG, "begin asset: " + name);
            boolean processed = processInput(stream);
            Log.d(LOG_TAG, "end asset: " + name);
            return processed;
          } finally {
            close(stream);
          }
        } catch (IOException exception) {
          Log.w(LOG_TAG, "asset not found: " + name);
        }
      }
    }

    return false;
  }

  public final boolean processInput (Collection<String> names) {
    return processInput(names.toArray(new String[names.size()]));
  }

  public InputProcessor () {
  }
}
