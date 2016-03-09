package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class QuickStartHTML extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_ui_start_html));
  }

  public QuickStartHTML (Endpoint endpoint) {
    super(endpoint, false);
  }
}
