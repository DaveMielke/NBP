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

  public static void saveControls (Control[] controls) {
    for (Control control : controls) {
      control.saveValue();
    }
  }

  public static void saveControls () {
    saveControls(allControls);
  }

  public static void restoreControls (Control[] controls) {
    for (Control control : controls) {
      control.restoreValue();
    }
  }

  public static void restoreControls () {
    restoreControls(allControls);
  }

  public static void resetControls (Control[] controls) {
    for (Control control : controls) {
      control.resetValue();
    }
  }

  public static void resetControls () {
    resetControls(allControls);
  }

  private Controls () {
  }
}
