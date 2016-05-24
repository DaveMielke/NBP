package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class WarrantyHTML extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_b2g_warranty_html));
  }

  public WarrantyHTML (Endpoint endpoint) {
    super(endpoint, false);
  }
}
