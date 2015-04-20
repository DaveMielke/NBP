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

  public final static ControlProcessor saveValue = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.saveValue();
      return true;
    }
  };

  public static void saveValues () {
    forEachControl(saveValue);
  }

  public final static ControlProcessor restoreDefaultValue = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.restoreDefaultValue();
      return true;
    }
  };

  public static void restoreDefaultValues () {
    forEachControl(restoreDefaultValue);
  }

  public final static ControlProcessor restoreSavedValue = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.restoreSavedValue();
      return true;
    }
  };

  public static void restoreSavedValues () {
    forEachControl(restoreSavedValue);
  }

  public final static ControlProcessor restoreCurrentValue = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.restoreCurrentValue();
      return true;
    }
  };

  public static void restoreCurrentValues () {
    forEachControl(restoreCurrentValue);
  }

  private Controls () {
  }
}
