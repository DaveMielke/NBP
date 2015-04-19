package org.nbp.b2g.ui;

import java.util.Arrays;
import java.util.Collection;

public class Controls {
  private final static BooleanControl oneHandControl = new OneHandControl();
  private final static BooleanControl longPressControl = new LongPressControl();

  private final static BooleanControl speechOnControl = new SpeechOnControl();
  private final static IntegerControl speechVolumeControl = new SpeechVolumeControl();
  private final static IntegerControl speechBalanceControl = new SpeechBalanceControl();
  private final static IntegerControl speechRateControl = new SpeechRateControl();
  private final static IntegerControl speechPitchControl = new SpeechPitchControl();

  private final static Control[] allControls = new Control[] {
    oneHandControl,
    longPressControl,

    speechOnControl,
    speechVolumeControl,
    speechBalanceControl,
    speechRateControl,
    speechPitchControl
  };

  public static BooleanControl getOneHandControl () {
    return oneHandControl;
  }

  public static BooleanControl getLongPressControl () {
    return longPressControl;
  }

  public static BooleanControl getSpeechOnControl () {
    return speechOnControl;
  }

  public static IntegerControl getSpeechVolumeControl () {
    return speechVolumeControl;
  }

  public static IntegerControl getSpeechBalanceControl () {
    return speechBalanceControl;
  }

  public static IntegerControl getSpeechRateControl () {
    return speechRateControl;
  }

  public static IntegerControl getSpeechPitchControl () {
    return speechPitchControl;
  }

  public static void forEachControl (Collection<Control> controls, ControlProcessor processor) {
    for (Control control : controls) {
      if (!processor.processControl(control)) break;
    }
  }

  public static void forEachControl (Control[] controls, ControlProcessor processor) {
    forEachControl(Arrays.asList(controls), processor);
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
