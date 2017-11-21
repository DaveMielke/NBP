package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public abstract class ViewerActivityAction extends ActivityAction {
  protected abstract Uri getUri ();

  @Override
  protected Intent getIntent (Context context) {
    return new Intent(Intent.ACTION_VIEW, getUri());
  }

  protected ViewerActivityAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
