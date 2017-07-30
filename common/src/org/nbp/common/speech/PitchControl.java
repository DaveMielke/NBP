package org.nbp.common.speech;
import org.nbp.common.*;

public abstract class PitchControl extends LogarithmicFloatControl {
  @Override
  protected int getResourceForNext () {
    return R.string.control_speech_pitch_next;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_speech_pitch_previous;
  }

  public PitchControl () {
    super();
  }
}
