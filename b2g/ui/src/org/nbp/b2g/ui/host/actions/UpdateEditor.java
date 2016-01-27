package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;
import android.net.Uri;

public class UpdateEditor extends ViewerActivityAction {
  @Override
  protected Uri getUri () {
    return Uri.parse(ApplicationContext.getString(R.string.uri_editor_apk));
  }

  public UpdateEditor (Endpoint endpoint) {
    super(endpoint, true);
  }
}
