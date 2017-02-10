package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class LogScreenLogger extends ScreenLogger {
  @Override
  protected final boolean write (String text) {
    Log.i("screen-log", text);
    return true;
  }

  @Override
  public final boolean logScreen () {
    write("begin");
    writeScreen();
    write("end");
    return true;
  }

  public LogScreenLogger () {
    super();
  }
}
