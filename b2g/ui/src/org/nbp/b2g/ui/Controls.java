package org.nbp.b2g.ui;

import java.util.Arrays;
import java.util.Collection;

public abstract class Controls {
  private final static InputModeControl inputModeControl = new InputModeControl();
  private final static InputBoldControl inputBoldControl = new InputBoldControl();
  private final static InputItalicControl inputItalicControl = new InputItalicControl();
  private final static InputUnderlineControl inputUnderlineControl = new InputUnderlineControl();
  private final static LongPressControl longPressControl = new LongPressControl();
  private final static ReversePanningControl reversePanningControl = new ReversePanningControl();
  private final static OneHandControl oneHandControl = new OneHandControl();

  private final static LiteraryBrailleControl literaryBrailleControl = new LiteraryBrailleControl();
  private final static BrailleCodeControl brailleCodeControl = new BrailleCodeControl();

  private final static BrailleEnabledControl brailleEnabledControl = new BrailleEnabledControl();
  private final static CursorIndicatorControl cursorIndicatorControl = new CursorIndicatorControl();
  private final static SelectionIndicatorControl selectionIndicatorControl = new SelectionIndicatorControl();
  private final static BrailleFirmnessControl brailleFirmnessControl = new BrailleFirmnessControl();
  private final static BrailleMonitorControl brailleMonitorControl = new BrailleMonitorControl();

  private final static SpeechEnabledControl speechEnabledControl = new SpeechEnabledControl();
  private final static SleepTalkControl sleepTalkControl = new SleepTalkControl();
  private final static SpeechVolumeControl speechVolumeControl = new SpeechVolumeControl();
  private final static SpeechBalanceControl speechBalanceControl = new SpeechBalanceControl();
  private final static SpeechRateControl speechRateControl = new SpeechRateControl();
  private final static SpeechPitchControl speechPitchControl = new SpeechPitchControl();

  private final static DeveloperEnabledControl developerEnabledControl = new DeveloperEnabledControl();
  private final static LogUpdatesControl logUpdatesControl = new LogUpdatesControl();
  private final static LogKeyboardControl logKeyboardControl = new LogKeyboardControl();
  private final static LogActionsControl logActionsControl = new LogActionsControl();
  private final static LogNavigationControl logNavigationControl = new LogNavigationControl();
  private final static LogGesturesControl logGesturesControl = new LogGesturesControl();
  private final static LogBrailleControl logBrailleControl = new LogBrailleControl();

  private final static Control[] allControls = new Control[] {
    inputModeControl,
    inputBoldControl,
    inputItalicControl,
    inputUnderlineControl,
    longPressControl,
    reversePanningControl,
    oneHandControl,

    literaryBrailleControl,
    brailleCodeControl,

    brailleEnabledControl,
    cursorIndicatorControl,
    selectionIndicatorControl,
    brailleFirmnessControl,
    brailleMonitorControl,

    speechEnabledControl,
    sleepTalkControl,
    speechVolumeControl,
    speechBalanceControl,
    speechRateControl,
    speechPitchControl,

    developerEnabledControl,
    logUpdatesControl,
    logKeyboardControl,
    logActionsControl,
    logNavigationControl,
    logGesturesControl,
    logBrailleControl
  };

  public static InputModeControl getInputModeControl () {
    return inputModeControl;
  }

  public static InputBoldControl getInputBoldControl () {
    return inputBoldControl;
  }

  public static InputItalicControl getInputItalicControl () {
    return inputItalicControl;
  }

  public static InputUnderlineControl getInputUnderlineControl () {
    return inputUnderlineControl;
  }

  public static LongPressControl getLongPressControl () {
    return longPressControl;
  }

  public static ReversePanningControl getReversePanningControl () {
    return reversePanningControl;
  }

  public static OneHandControl getOneHandControl () {
    return oneHandControl;
  }

  public static LiteraryBrailleControl getLiteraryBrailleControl () {
    return literaryBrailleControl;
  }

  public static BrailleCodeControl getBrailleCodeControl () {
    return brailleCodeControl;
  }

  public static BrailleEnabledControl getBrailleEnabledControl () {
    return brailleEnabledControl;
  }

  public static CursorIndicatorControl getCursorIndicatorControl () {
    return cursorIndicatorControl;
  }

  public static SelectionIndicatorControl getSelectionIndicatorControl () {
    return selectionIndicatorControl;
  }

  public static BrailleFirmnessControl getBrailleFirmnessControl () {
    return brailleFirmnessControl;
  }

  public static BrailleMonitorControl getBrailleMonitorControl () {
    return brailleMonitorControl;
  }

  public static SpeechEnabledControl getSpeechEnabledControl () {
    return speechEnabledControl;
  }

  public static SleepTalkControl getSleepTalkControl () {
    return sleepTalkControl;
  }

  public static SpeechVolumeControl getSpeechVolumeControl () {
    return speechVolumeControl;
  }

  public static SpeechBalanceControl getSpeechBalanceControl () {
    return speechBalanceControl;
  }

  public static SpeechRateControl getSpeechRateControl () {
    return speechRateControl;
  }

  public static SpeechPitchControl getSpeechPitchControl () {
    return speechPitchControl;
  }

  public static DeveloperEnabledControl getDeveloperEnabledControl () {
    return developerEnabledControl;
  }

  public static LogUpdatesControl getLogUpdatesControl () {
    return logUpdatesControl;
  }

  public static LogKeyboardControl getLogKeyboardControl () {
    return logKeyboardControl;
  }

  public static LogActionsControl getLogActionsControl () {
    return logActionsControl;
  }

  public static LogNavigationControl getLogNavigationControl () {
    return logNavigationControl;
  }

  public static LogGesturesControl getLogGesturesControl () {
    return logGesturesControl;
  }

  public static LogBrailleControl getLogBrailleControl () {
    return logBrailleControl;
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

  public static void restoreSaneValues () {
    inputModeControl.setValue(InputMode.TEXT);
    inputBoldControl.setValue(false);
    inputItalicControl.setValue(false);
    inputUnderlineControl.setValue(false);
    oneHandControl.setValue(false);
    brailleEnabledControl.setValue(true);
  }

  private Controls () {
  }
}
