package org.nbp.calculator;

import java.util.Collection;

import org.nbp.common.CommonActivity;
import org.nbp.common.AlertDialogBuilder;
import android.app.AlertDialog;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static String[] toArray (Collection<String> collection) {
    return collection.toArray(new String[collection.size()]);
  }

  public final static AlertDialog.Builder newAlertDialogBuilder (int... subtitles) {
    return new AlertDialogBuilder(CommonActivity.getActivity(), subtitles)
              .setNegativeButton(R.string.button_cancel, null)
              .setCancelable(true)
              ;
  }
}
