package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class HelpHTML extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_ui_manual_html));
  }

  public HelpHTML (Endpoint endpoint) {
    super(endpoint, false);
  }
}
