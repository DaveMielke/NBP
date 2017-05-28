package org.nbp.b2g.ui;
import org.nbp.b2g.ui.controls.*;

import org.nbp.common.Control;
import org.nbp.common.BooleanControl;

import java.util.Arrays;
import java.util.Collection;

public abstract class Controls {
  private Controls () {
  }

  static {
    Control.setValueConfirmationListener(
      new Control.ValueConfirmationListener() {
        @Override
        public void confirmValue (String confirmation) {
          ApplicationUtilities.message(confirmation);
        }
      }
    );
  }

  public final static LiteraryBrailleControl literaryBraille = new LiteraryBrailleControl();
  public final static BrailleCodeControl brailleCode = new BrailleCodeControl();
  public final static WordWrapControl wordWrap = new WordWrapControl();
  public final static ShowNotificationsControl showNotifications = new ShowNotificationsControl();

  public final static TypingModeControl typingMode = new TypingModeControl();
  public final static TypingBoldControl typingBold = new TypingBoldControl();
  public final static TypingItalicControl typingItalic = new TypingItalicControl();
  public final static TypingStrikeControl typingStrike = new TypingStrikeControl();
  public final static TypingUnderlineControl typingUnderline = new TypingUnderlineControl();

  public final static ShowHighlightedControl showHighlighted = new ShowHighlightedControl();
  public final static SelectionIndicatorControl selectionIndicator = new SelectionIndicatorControl();
  public final static CursorIndicatorControl cursorIndicator = new CursorIndicatorControl();
  public final static BrailleFirmnessControl brailleFirmness = new BrailleFirmnessControl();
  public final static BrailleMonitorControl brailleMonitor = new BrailleMonitorControl();
  public final static BrailleEnabledControl brailleEnabled = new BrailleEnabledControl();

  public final static SpeechEnabledControl speechEnabled = new SpeechEnabledControl();
  public final static SpeechVolumeControl speechVolume = new SpeechVolumeControl();
  public final static SpeechRateControl speechRate = new SpeechRateControl();
  public final static SpeechPitchControl speechPitch = new SpeechPitchControl();
  public final static SpeechBalanceControl speechBalance = new SpeechBalanceControl();
  public final static SleepTalkControl sleepTalk = new SleepTalkControl();

  public final static LongPressControl longPress = new LongPressControl();
  public final static ReversePanningControl reversePanning = new ReversePanningControl();
  public final static OneHandControl oneHand = new OneHandControl();

  public final static RemoteDisplayControl remoteDisplay = new RemoteDisplayControl();
  public final static SecureConnectionControl secureConnection = new SecureConnectionControl();

  public final static CrashEmailsControl crashEmails = new CrashEmailsControl();
  public final static AdvancedActionsControl advancedActions = new AdvancedActionsControl();
  public final static ExtraIndicatorsControl extraIndicators = new ExtraIndicatorsControl();
  public final static EventMessagesControl eventMessages = new EventMessagesControl();
  public final static LogUpdatesControl logUpdates = new LogUpdatesControl();
  public final static LogKeyboardControl logKeyboard = new LogKeyboardControl();
  public final static LogActionsControl logActions = new LogActionsControl();
  public final static LogNavigationControl logNavigation = new LogNavigationControl();
  public final static LogGesturesControl logGestures = new LogGesturesControl();
  public final static LogBrailleControl logBraille = new LogBrailleControl();
  public final static LogSpeechControl logSpeech = new LogSpeechControl();

  private final static Control[] allControls = new Control[] {
    literaryBraille,
    brailleCode,
    wordWrap,
    showNotifications,

    typingMode,
    typingBold,
    typingItalic,
    typingStrike,
    typingUnderline,

    showHighlighted,
    selectionIndicator,
    cursorIndicator,
    brailleFirmness,
    brailleMonitor,
    brailleEnabled,

    speechEnabled,
    speechVolume,
    speechRate,
    speechPitch,
    speechBalance,
    sleepTalk,

    longPress,
    reversePanning,
    oneHand,

    remoteDisplay,
    secureConnection,

    crashEmails,
    advancedActions,
    extraIndicators,
    eventMessages,
    logUpdates,
    logKeyboard,
    logActions,
    logNavigation,
    logGestures,
    logBraille,
    logSpeech
  };

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
    typingBold,
    typingItalic,
    typingStrike,
    typingUnderline
  };

  public static void resetHighlightedTyping () {
    for (BooleanControl control : highlightedTypingControls) {
      control.setValue(false);
    }
  }

  public static void restoreSaneValues () {
    typingMode.setValue(TypingMode.TEXT);
    resetHighlightedTyping();
    oneHand.setValue(false);
    brailleEnabled.setValue(true);
  }
}
