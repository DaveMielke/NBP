package org.nbp.editor;

import android.content.DialogInterface;

public abstract class OnDialogClickListener implements DialogInterface.OnClickListener {
  protected abstract void onClick ();

  @Override
  public final void onClick (DialogInterface dialog, int which) {
    onClick();
  }

  public OnDialogClickListener () {
    super();
  }
}
