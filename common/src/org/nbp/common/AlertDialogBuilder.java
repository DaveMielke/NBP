package org.nbp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

public class AlertDialogBuilder extends AlertDialog.Builder {
  public AlertDialogBuilder (Context context, int... subtitles) {
    super(context);

    StringBuilder title = new StringBuilder();
    title.append(context.getString(R.string.app_name));

    for (Integer subtitle : subtitles) {
      title.append(" - ");
      title.append(context.getString(subtitle));
    }

    setTitle(title.toString());
  }
}
