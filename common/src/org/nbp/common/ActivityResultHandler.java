package org.nbp.common;

import android.content.Intent;

public interface ActivityResultHandler {
  public void handleActivityResult (int code, Intent intent);
}
