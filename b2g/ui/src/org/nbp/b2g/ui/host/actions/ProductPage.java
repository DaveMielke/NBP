package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class ProductPage extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_b2g_product_html));
  }

  public ProductPage (Endpoint endpoint) {
    super(endpoint, false);
  }
}
