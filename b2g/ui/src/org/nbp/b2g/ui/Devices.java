package org.nbp.b2g.ui;

import org.nbp.common.LazyInstantiator;

public abstract class Devices {
  private Devices () {
  }

  public final static LazyInstantiator<BrailleDevice> braille = new
    LazyInstantiator<BrailleDevice>(MetecBrailleDevice.class);

  public final static LazyInstantiator<KeyboardDevice> keyboard = new
    LazyInstantiator<KeyboardDevice>(KeyboardDevice.class);

  public final static LazyInstantiator<TouchDevice> touch = new
    LazyInstantiator<TouchDevice>(TouchDevice.class);

  public final static LazyInstantiator<PointerDevice> pointer = new
    LazyInstantiator<PointerDevice>(PointerDevice.class);

  public final static LazyInstantiator<MotionDevice> motion = new
    LazyInstantiator<MotionDevice>(MotionDevice.class);

  public final static LazyInstantiator<SpeechDevice> speech = new
    LazyInstantiator<SpeechDevice>(SpeechDevice.class);
}
