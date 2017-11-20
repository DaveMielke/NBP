package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.net.Uri;

public class VoiceOverHTML extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(getString(R.string.uri_b2g_vo_html));
  }

  public VoiceOverHTML (Endpoint endpoint) {
    super(endpoint, false);
  }
}
