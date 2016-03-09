package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class UserManualHTML extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_ui_manual_html));
  }

  public UserManualHTML (Endpoint endpoint) {
    super(endpoint, false);
  }
}
