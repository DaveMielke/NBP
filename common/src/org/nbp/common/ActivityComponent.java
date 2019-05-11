package org.nbp.common;

import android.app.Activity;

import android.view.View;
import android.widget.Button;

import android.content.DialogInterface;
import android.app.AlertDialog;

public abstract class ActivityComponent {
  protected final Activity ownerActivity;

  protected ActivityComponent (Activity owner) {
    ownerActivity = owner;
  }

  protected final String getString (int resource) {
    return ownerActivity.getString(resource);
  }

  protected final View inflateLayout (int resource) {
    return ownerActivity.getLayoutInflater().inflate(resource, null);
  }

  protected final <T extends View> T findView (int id) {
    return ownerActivity.findViewById(id);
  }

  protected final <T extends View> T findView (DialogInterface dialog, int id) {
    return ((AlertDialog)dialog).findViewById(id);
  }

  protected final Button getButton (DialogInterface dialog, int button) {
    return ((AlertDialog)dialog).getButton(button);
  }

  protected final void setButtonEnabled (DialogInterface dialog, int button, boolean yes) {
    getButton(dialog, button).setEnabled(yes);
  }

  protected final AlertDialog.Builder newAlertDialogBuilder () {
    return new AlertDialog.Builder(ownerActivity)
                          .setCancelable(false)
                          ;
  }

  protected final void setTitle (AlertDialog.Builder builder, int subtitle, String... details) {
    StringBuilder title = new StringBuilder(getString(subtitle));

    for (String detail : details) {
      title.append('\n');
      title.append(detail);
    }

    builder.setTitle(title.toString());
  }

  protected final void showMessage (
    int message, CharSequence detail,
    DialogInterface.OnClickListener dismissListener
  ) {
    newAlertDialogBuilder()
      .setTitle(message)
      .setMessage(detail)
      .setNeutralButton(android.R.string.yes, dismissListener)
      .show();
  }

  protected final void setDoneButton (
    AlertDialog.Builder builder,
    DialogInterface.OnClickListener listener
  ) {
    builder.setPositiveButton(R.string.ActivityComponent_action_done, listener);
  }
}
