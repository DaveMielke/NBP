package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DescribeBuild extends Action {
  private final static String LOG_TAG = DescribeBuild.class.getName();

  private static void addBuildProperty (
    StringBuilder sb, Reader reader, int... labels
  ) {
    BufferedReader input;

    if (reader instanceof BufferedReader) {
      input = (BufferedReader)reader;
    } else {
      input = new BufferedReader(reader);
    }

    for (int label : labels) {
      try {
        String line = input.readLine().trim();

        if (!line.isEmpty()) {
          if (sb.length() > 0) sb.append('\n');
          sb.append(ApplicationContext.getString(label));
          sb.append(": ");
          sb.append(line);
        } else {
          Log.w(LOG_TAG, "build property not available: " + label);
        }
      } catch (IOException exception) {
        Log.w(LOG_TAG, "input error: " + label, exception);
        break;
      }
    }
  }

  private static void addBuildProperty (
    StringBuilder sb, InputStream stream, int... labels
  ) {
    String encoding = "UTF8";

    try {
      Reader reader = new InputStreamReader(stream, encoding);
      addBuildProperty(sb, reader, labels);
    } catch (UnsupportedEncodingException exception) {
      Log.w(LOG_TAG, "unsupported input encoding: " + encoding);
    }
  }

  private static void addBuildProperty (
    StringBuilder sb, AssetManager assets,
    String property, int... labels
  ) {
    String asset = "build." + property;

    try {
      InputStream stream = assets.open(asset);

      try {
        addBuildProperty(sb, stream, labels);
      } finally {
        stream.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "asset not found: " + asset);
    }
  }

  @Override
  public boolean performAction () {
    Context context = ApplicationContext.getContext();
    if (context == null) return false;

    AssetManager assets = context.getAssets();
    if (assets == null) return false;

    StringBuilder sb = new StringBuilder();
    addBuildProperty(sb, assets, "time", R.string.build_property_time);
    addBuildProperty(sb, assets, "revision", R.string.build_property_revision);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeBuild (Endpoint endpoint) {
    super(endpoint, false);
  }
}
