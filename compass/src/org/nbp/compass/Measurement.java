package org.nbp.compass;

public class Measurement {
  private final int size = ApplicationParameters.MEASUREMENT_DAMPING_FACTOR;
  private final float[] values = new float[size];

  private int count = 0;
  private float sum = 0f;

  public final void add (float value) {
    int index = count % size;
    if (++count > size) sum -= values[index];
    sum += values[index] = value;
  }

  public float get () {
    return sum / Math.min(count, size);
  }

  public Measurement () {
  }
}
