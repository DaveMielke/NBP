package org.nbp.calculator;

import java.util.Collection;

import android.app.Activity;

import org.nbp.common.AlertDialogBuilder;
import android.app.AlertDialog;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static String[] toArray (Collection<String> collection) {
    return collection.toArray(new String[collection.size()]);
  }

  public final static AlertDialog.Builder newAlertDialogBuilder (Activity owner, int subtitle, String detail) {
    return new AlertDialogBuilder(owner, subtitle, detail)
              .setNegativeButton(R.string.button_cancel, null)
              .setCancelable(true)
              ;
  }

  public final static AlertDialog.Builder newAlertDialogBuilder (Activity owner, int subtitle) {
    return newAlertDialogBuilder(owner, subtitle, null);
  }

  public final static AlertDialog.Builder newAlertDialogBuilder (Activity owner, int subtitle, int detail) {
    return newAlertDialogBuilder(owner, subtitle, owner.getString(detail));
  }
}
