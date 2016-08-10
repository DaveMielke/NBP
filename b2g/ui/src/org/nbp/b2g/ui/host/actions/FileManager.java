package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class FileManager extends ExternalActivityAction {
  @Override
  protected String getPackageName () {
    return "com.estrongs.android.pop";
  }

  public FileManager (Endpoint endpoint) {
    super(endpoint, false);
  }
}
