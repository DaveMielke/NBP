package org.nbp.common.speech;
import org.nbp.common.*;

import org.nbp.common.controls.LinearFloatControl;

public abstract class VolumeControl extends LinearFloatControl {
  @Override
  protected int getResourceForNext () {
    return R.string.control_speech_volume_next;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_speech_volume_previous;
  }

  public VolumeControl () {
    super();
  }
}
