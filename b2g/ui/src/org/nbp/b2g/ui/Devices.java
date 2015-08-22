package org.nbp.b2g.ui;

public abstract class Devices {
  public final static LazyInstantiator<BrailleDevice> braille = new
    LazyInstantiator<BrailleDevice>(BrailleDevice.class);

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

  public final static LazyInstantiator<ToneDevice> tone = new
    LazyInstantiator<ToneDevice>(ToneDevice.class);

  private Devices () {
  }
}
