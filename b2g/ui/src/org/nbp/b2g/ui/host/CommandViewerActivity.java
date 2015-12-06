package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

public abstract class CommandViewerActivity extends ViewerActivity {
  private final static String LOG_TAG = CommandViewerActivity.class.getName();

  protected abstract String[] getCommand ();

  @Override
  protected final InputStream getInputStream () {
    String[] command = getCommand();

    {
      StringBuilder sb = new StringBuilder("command:");

      for (String argument : command) {
        sb.append(' ');
        sb.append(argument);
      }

      Log.d(LOG_TAG, sb.toString());
    }

    try {
      return new ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()
            .getInputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "command not started: " + exception.getMessage());
    }

    return null;
  }
}
