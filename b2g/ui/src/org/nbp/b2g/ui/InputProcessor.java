package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class InputProcessor {
  private final static String LOG_TAG = InputProcessor.class.getName();

  protected abstract boolean processLine (String text, int number);

  public boolean processInput (Reader reader) {
    BufferedReader input;

    if (reader instanceof BufferedReader) {
      input = (BufferedReader)reader;
    } else {
      input = new BufferedReader(reader);
    }

    for (int number=0; true; number+=1) {
      try {
        String text = input.readLine();
        if (text == null) break;
        if (!processLine(text, number)) break;
      } catch (IOException exception) {
        Log.w(LOG_TAG, "input error", exception);
        return false;
      }
    }

    return true;
  }

  public boolean processInput (InputStream stream) {
    String encoding = "UTF8";

    try {
      Reader reader = new InputStreamReader(stream, encoding);
      return processInput(reader);
    } catch (UnsupportedEncodingException exception) {
      Log.w(LOG_TAG, "unsupported input encoding: " + encoding);
    }

    return false;
  }

  public boolean processInput (String asset) {
    Context context = ApplicationContext.getContext();
    if (context == null) return false;

    AssetManager assets = context.getAssets();
    if (assets == null) return false;

    try {
      InputStream stream = assets.open(asset);

      try {
        return processInput(stream);
      } finally {
        stream.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "asset not found: " + asset);
    }

    return false;
  }

  public InputProcessor () {
  }
}
