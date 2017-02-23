package org.nbp.common;

import android.content.Intent;

public abstract class ActivityResultHandler {
  public abstract void handleActivityResult (int code, Intent intent);

  public final static int toResultMessage (int code) {
    switch (code) {
      case CommonActivity.RESULT_OK:
        return R.string.ActivityResultHandler_RESULT_OK;

      case CommonActivity.RESULT_CANCELED:
        return R.string.ActivityResultHandler_RESULT_CANCELED;

      default:
        return R.string.ActivityResultHandler_RESULT_UNKNOWN;
    }
  }
}
