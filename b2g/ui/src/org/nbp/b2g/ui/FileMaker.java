package org.nbp.b2g.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import android.util.Log;
import android.content.Context;

public abstract class FileMaker {
  private final static String LOG_TAG = FileMaker.class.getName();

  protected abstract boolean writeContent (Writer writer) throws IOException;

  protected boolean setAttributes (File file) {
    return true;
  }

  public final File makeFile (String name, String owner) {
    Context context = ApplicationContext.getContext();

    if (context != null) {
      File directory = context.getDir(owner, Context.MODE_PRIVATE);

      if (directory != null) {
        File file = new File(directory, name);

        if (file != null) {
          try {
            boolean written = false;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            try {
              if (writeContent(writer)) written = true;
            } finally {
              writer.close();
            }

            if (written) {
              if (setAttributes(file)) {
                return file;
              }
            }

            file.delete();
          } catch (IOException exception) {
            Log.w(LOG_TAG, "file make error", exception);
          }
        }
      }
    }

    return null;
  }

  public final File makeFile (String name, Class owner) {
    return makeFile(name, owner.getSimpleName());
  }

  public final File makeFile (String name, Object owner) {
    return makeFile(name, owner.getClass());
  }

  public FileMaker () {
  }
}
