package org.liblouis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.res.AssetManager;

import android.util.Log;
import android.os.AsyncTask;

public class AssetsExtractor {
  private final static String LOG_TAG = AssetsExtractor.class.getName();

  private final AssetManager assetManager;
  private final File intoDirectory;

  public AssetsExtractor (Context context) {
    assetManager = context.getAssets();
    intoDirectory = context.getDir("extracted_assets", Context.MODE_PRIVATE);
  }

  public final File getDirectory () {
    return intoDirectory;
  }

  public static void removeTree (File root) {
    if (root.isDirectory()) {
      root.setWritable(true, true);

      for (File file : root.listFiles()) {
        removeTree(file);
      }
    }

    root.delete();
  }

  private final boolean extractAsset (String asset, File location) {
    try {
      String[] names = assetManager.list(asset);
      boolean isDirectory = names.length > 0;
      String path = location.getAbsolutePath();

      if (isDirectory) {
        if (!location.exists()) {
          if (!location.mkdir()) {
            Log.w(LOG_TAG, "directory not created: " + path);
            return false;
          }
        } else if (!location.isDirectory()) {
          Log.w(LOG_TAG, "not a directory: " + path);
          return false;
        }

        for (String name : names) {
          if (!extractAsset(new File(asset, name).getPath(), new File(location, name))) {
            return false;
          }
        }
      } else {
        InputStream input = assetManager.open(asset);

        try {
          OutputStream output = new FileOutputStream(location);

          try {
            byte[] buffer = new byte[0X1000];

            for (int count; ((count = input.read(buffer)) > 0); ) {
              output.write(buffer, 0, count);
            }
          } finally {
            output.close();
          }
        } finally {
          input.close();
        }
      }

      location.setExecutable(isDirectory, false);
      location.setWritable(false, false);
      location.setReadable(true, false);

      return true;
    } catch (IOException exception) {
      Log.e(LOG_TAG, "directory refresh error: " + exception.getMessage());
    }

    return false;
  }

  private final boolean extractAsset (String asset) {
    String name = asset;
    String oldName = name + ".old";
    String newName = name + ".new";

    File location = new File(intoDirectory, name);
    File oldLocation = new File(intoDirectory, oldName);
    File newLocation = new File(intoDirectory, newName);

    removeTree(oldLocation);
    removeTree(newLocation);

    if (!extractAsset(name, newLocation)) {
      return false;
    }

    location.renameTo(oldLocation);
    newLocation.renameTo(location);
    removeTree(oldLocation);

    return true;
  }

  public interface Listener {
    public void onFinished ();
  }

  public final AssetsExtractor extractAssets (final Listener listener, String... assets) {
    Log.d(LOG_TAG, "begin extracting assets");

    new AsyncTask<String, Void, Boolean>() {
      @Override
      protected Boolean doInBackground (String... assets) {
        for (String asset : assets) {
          if (!extractAsset(asset)) {
            return false;
          }
        }

        return true;
      }

      @Override
      protected void onPostExecute (Boolean finished) {
        Log.d(LOG_TAG, "end extracting assets");

        if (listener != null) {
          if (finished) {
            listener.onFinished();
          }
        }
      }
    }.execute(assets);

    return this;
  }

  public final AssetsExtractor extractAssets (String... assets) {
    return extractAssets(null, assets);
  }
}
