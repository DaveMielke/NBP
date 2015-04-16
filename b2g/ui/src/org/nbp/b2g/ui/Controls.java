package org.nbp.b2g.ui;

public class Controls {
  private final static Control volumeControl = new VolumeControl();
  private final static Control balanceControl = new BalanceControl();
  private final static Control rateControl = new RateControl();
  private final static Control pitchControl = new PitchControl();

  private final static Control[] controls = new Control[] {
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

  private Controls () {
  }
}
