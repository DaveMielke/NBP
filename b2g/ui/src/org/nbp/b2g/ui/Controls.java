package org.nbp.b2g.ui;
import org.nbp.b2g.ui.controls.*;

import java.util.Arrays;
import java.util.Collection;

public abstract class Controls {
  private final static WordWrapControl wordWrapControl = new WordWrapControl();

  private final static LongPressControl longPressControl = new LongPressControl();
  private final static ReversePanningControl reversePanningControl = new ReversePanningControl();
  private final static OneHandControl oneHandControl = new OneHandControl();

  private final static TypingModeControl typingModeControl = new TypingModeControl();
  private final static TypingBoldControl typingBoldControl = new TypingBoldControl();
  private final static TypingItalicControl typingItalicControl = new TypingItalicControl();
  private final static TypingStrikeControl typingStrikeControl = new TypingStrikeControl();
  private final static TypingUnderlineControl typingUnderlineControl = new TypingUnderlineControl();

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
  private final static LogSpeechControl logSpeechControl = new LogSpeechControl();

  private final static Control[] allControls = new Control[] {
    longPressControl,
    reversePanningControl,
    oneHandControl,

    typingModeControl,
    typingBoldControl,
    typingItalicControl,
    typingStrikeControl,
    typingUnderlineControl,

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
    logBrailleControl,
    logSpeechControl
  };

  public static WordWrapControl getWordWrapControl () {
    return wordWrapControl;
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

  public static TypingModeControl getTypingModeControl () {
    return typingModeControl;
  }

  public static TypingBoldControl getTypingBoldControl () {
    return typingBoldControl;
  }

  public static TypingItalicControl getTypingItalicControl () {
    return typingItalicControl;
  }

  public static TypingStrikeControl getTypingStrikeControl () {
    return typingStrikeControl;
  }

  public static TypingUnderlineControl getTypingUnderlineControl () {
    return typingUnderlineControl;
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

  public static LogSpeechControl getLogSpeechControl () {
    return logSpeechControl;
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

  private final static BooleanControl[] highlightedTypingControls = new BooleanControl[] {
    typingBoldControl,
    typingItalicControl,
    typingStrikeControl,
    typingUnderlineControl
  };

  public static void resetHighlightedTyping () {
    for (BooleanControl control : highlightedTypingControls) {
      control.setValue(false);
    }
  }

  public static void restoreSaneValues () {
    typingModeControl.setValue(TypingMode.TEXT);
    resetHighlightedTyping();
    oneHandControl.setValue(false);
    brailleEnabledControl.setValue(true);
  }

  private Controls () {
  }
}
