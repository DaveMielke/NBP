package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.controls.EnumerationControl;

public abstract class ActivationLevelControl extends EnumerationControl<ActivationLevel> {
  protected abstract void startTask ();
  protected abstract void stopTask ();

  public final void onResume () {
    if (getEnumerationValue() != ActivationLevel.OFF) startTask();
  }

  public final void onPause () {
    if (getEnumerationValue() != ActivationLevel.ALWAYS) stopTask();
  }

  protected final void onChange () {
    if (getEnumerationValue() == ActivationLevel.ALWAYS) {
      startTask();
    } else {
      stopTask();
    }
  }

  protected ActivationLevelControl () {
    super();
  }
}
