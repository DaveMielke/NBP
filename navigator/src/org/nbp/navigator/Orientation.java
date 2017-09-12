package org.nbp.navigator;

public class Orientation {
  private final long orientationTime;
  private final float orientationHeading;
  private final float orientationPitch;
  private final float orientationRoll;

  public Orientation (float heading, float pitch, float roll) {
    orientationTime = System.currentTimeMillis();
    orientationHeading = heading;
    orientationPitch = pitch;
    orientationRoll = roll;
  }

  public final long getTime () {
    return orientationTime;
  }

  public final float getHeading () {
    return orientationHeading;
  }

  public final float getPitch () {
    return orientationPitch;
  }

  public final float getRoll () {
    return orientationRoll;
  }
}
