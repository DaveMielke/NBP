package com.duxburysystems;

import java.io.File;

import android.content.res.AssetManager;

public abstract class AssetUtilities {
  private AssetUtilities () {
  }

  private final static Object ROOT_FOLDER_LOCK = new Object();
  private static String rootFolder = null;

  public final static void setRootFolder (String folder) {
    synchronized (ROOT_FOLDER_LOCK) {
      rootFolder = folder;
    }
  }

  public final static String toPath (String asset) {
    synchronized (ROOT_FOLDER_LOCK) {
      String root = rootFolder;

      if ((root != null) && !root.isEmpty()) {
        asset = root + File.separatorChar + asset;
      }
    }

    return asset;
  }
}
