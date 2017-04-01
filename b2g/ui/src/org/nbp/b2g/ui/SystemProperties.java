package org.nbp.b2g.ui;

import org.nbp.common.CommonParameters;

import java.io.IOException;
import java.io.InputStream;

import java.io.File;
import java.io.FileInputStream;

import android.util.Log;

public abstract class SystemProperties {
  protected final static String LOG_TAG = SystemProperties.class.getName();

  private final String propertiesDirectory;

  protected SystemProperties (String directory) {
    propertiesDirectory = "/sys/" + directory;
  }

  public final String getProperty (String name) {
    File file = new File(propertiesDirectory, name);

    try {
      InputStream stream = new FileInputStream(file);

      try {
        int size = stream.available();
        byte[] buffer = new byte[size];
        int count = stream.read(buffer);
        String value = new String(buffer, 0, count, CommonParameters.INPUT_ENCODING_NAME);

        {
          int end = value.indexOf('\n');
          if (end >= 0) value = value.substring(0, end);
        }

        return value;
      } finally {
        stream.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("property read error: %s: %s", name, exception.getMessage()));
    }

    return null;
  }

  public final int getIntegerProperty (String name, int defaultValue) {
    String value = getProperty(name);

    if (value != null) {
      try {
        return Integer.valueOf(value);
      } catch (NumberFormatException exception) {
        Log.w(LOG_TAG, String.format("not an integer property: %s: %s", name, value));
      }
    }

    return defaultValue;
  }
}
