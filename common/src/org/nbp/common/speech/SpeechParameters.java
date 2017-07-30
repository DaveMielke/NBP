package org.nbp.common.speech;

public abstract class SpeechParameters {
  private SpeechParameters () {
  }

  public final static float VOLUME_MAXIMUM = 1.0f;
  public final static float VOLUME_MINIMUM = 0.0f;

  public final static float BALANCE_CENTER = 0.0f;
  public final static float BALANCE_MAXIMUM = 1.0f;
  public final static float BALANCE_MINIMUM = -BALANCE_MAXIMUM;

  public final static float RATE_REFERENCE = 1.0f;
  public final static float RATE_MAXIMUM = 4.0f;
  public final static float RATE_MINIMUM = 1.0f / 3.0f;

  public final static float PITCH_REFERENCE = 1.0f;
  public final static float PITCH_MAXIMUM = 2.0f;
  public final static float PITCH_MINIMUM = 1.0f / 2.0f;
}
