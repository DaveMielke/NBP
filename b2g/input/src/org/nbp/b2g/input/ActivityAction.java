package org.nbp.b2g.input;

import android.content.Intent;

public final class ActivityAction extends Action {
  protected final Class activityClass;

  @Override
  public String getActionName () {
    return activityClass.getName();
  }

  @Override
  public final boolean performAction () {
    InputService inputService = getInputService();

    if (inputService != null) {
      Intent intent = new Intent(inputService, activityClass);

      intent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK |
        Intent.FLAG_ACTIVITY_CLEAR_TOP |
        Intent.FLAG_ACTIVITY_SINGLE_TOP |
        Intent.FLAG_FROM_BACKGROUND
      );

      inputService.startActivity(intent);
      return true;
    }

    return false;
  }

  public ActivityAction (Class activityClass) {
    super();
    this.activityClass = activityClass;
  }
}
