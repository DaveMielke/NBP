package org.nbp.b2g.ui;

public class Controls {
  private final static Control volumeControl = new VolumeControl();
  private final static Control balanceControl = new BalanceControl();
  private final static Control rateControl = new RateControl();
  private final static Control pitchControl = new PitchControl();

  private final static Control[] allControls = new Control[] {
    volumeControl,
    balanceControl,
    rateControl,
    pitchControl
  };

  public static Control getVolumeControl () {
    return volumeControl;
  }

  public static Control getBalanceControl () {
    return balanceControl;
  }

  public static Control getRateControl () {
    return rateControl;
  }

  public static Control getPitchControl () {
    return pitchControl;
  }

  public static void forEachControl (Control[] controls, ControlProcessor processor) {
    for (Control control : controls) {
      if (!processor.processControl(control)) break;
    }
  }

  public static void forEachControl (ControlProcessor processor) {
    forEachControl(allControls, processor);
  }

  public final static ControlProcessor saveControl = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.saveValue();
      return true;
    }
  };

  public static void saveControls () {
    forEachControl(saveControl);
  }

  public final static ControlProcessor restoreControl = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.restoreValue();
      return true;
    }
  };

  public static void restoreControls () {
    forEachControl(restoreControl);
  }

  public final static ControlProcessor resetControl = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.resetValue();
      return true;
    }
  };

  public static void resetControls () {
    forEachControl(resetControl);
  }

  private Controls () {
  }
}
