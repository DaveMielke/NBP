package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.io.File;

public abstract class PathViewerActivity extends FileViewerActivity {
  protected abstract String getPath ();

  @Override
  protected final File getFile () {
    return new File(getPath());
  }
}
