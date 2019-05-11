package org.nbp.common;

import android.app.Activity;

import android.view.View;
import android.widget.Button;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.text.Editable;
import android.text.SpannableStringBuilder;

public abstract class ActivityComponent {
  protected final Activity ownerActivity;

  protected ActivityComponent (Activity owner) {
    ownerActivity = owner;
  }

  protected final String getString (int string) {
    return ownerActivity.getString(string);
  }

  protected final View inflateLayout (int layout) {
    return ownerActivity.getLayoutInflater().inflate(layout, null);
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

  protected final void setView (AlertDialog.Builder builder, int layout) {
    builder.setView(inflateLayout(layout));
  }

  protected final AlertDialog.Builder newAlertDialogBuilder () {
    return new AlertDialog.Builder(ownerActivity)
             .setCancelable(false)
             ;
  }

  protected final void setTitle (AlertDialog.Builder builder, int title, Integer subtitle, CharSequence... details) {
    Editable text = new SpannableStringBuilder(getString(subtitle));

    if (subtitle != null) {
      text.append(" - ");
      text.append(getString(subtitle));
    }

    for (CharSequence detail : details) {
      if (detail == null) continue;
      if (detail.length() == 0) continue;

      text.append('\n');
      text.append(detail);
    }

    builder.setTitle(text.subSequence(0, text.length()));
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
