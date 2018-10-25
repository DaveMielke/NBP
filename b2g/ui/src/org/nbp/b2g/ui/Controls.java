package org.nbp.b2g.ui;
import org.nbp.b2g.ui.controls.*;

import org.nbp.common.controls.Control;
import org.nbp.common.controls.BooleanControl;

public abstract class Controls {
  private Controls () {
  }

  static {
    Control.setConfirmationListener(
      new Control.ConfirmationListener() {
        @Override
        public void confirm (String confirmation) {
          ApplicationUtilities.message(confirmation);
        }
      }
    );
  }

  // general settings
  public final static LiteraryBrailleControl literaryBraille = new LiteraryBrailleControl();
  public final static BrailleCodeControl brailleCode = new BrailleCodeControl();
  public final static WordWrapControl wordWrap = new WordWrapControl();
  public final static ShowNotificationsControl showNotifications = new ShowNotificationsControl();

  // input settings
  public final static TypingModeControl typingMode = new TypingModeControl();
  public final static TypingBoldControl typingBold = new TypingBoldControl();
  public final static TypingItalicControl typingItalic = new TypingItalicControl();
  public final static TypingStrikeControl typingStrike = new TypingStrikeControl();
  public final static TypingUnderlineControl typingUnderline = new TypingUnderlineControl();

  // braille settings
  public final static ShowHighlightedControl showHighlighted = new ShowHighlightedControl();
  public final static SelectionIndicatorControl selectionIndicator = new SelectionIndicatorControl();
  public final static CursorIndicatorControl cursorIndicator = new CursorIndicatorControl();
  public final static BrailleFirmnessControl brailleFirmness = new BrailleFirmnessControl();
  public final static BrailleMonitorControl brailleMonitor = new BrailleMonitorControl();
  public final static BrailleEnabledControl brailleEnabled = new BrailleEnabledControl();

  // speech settings
  public final static SpeechEnabledControl speechEnabled = new SpeechEnabledControl();
  public final static EchoWordsControl echoWords = new EchoWordsControl();
  public final static EchoCharactersControl echoCharacters = new EchoCharactersControl();
  public final static EchoDeletionsControl echoDeletions = new EchoDeletionsControl();
  public final static EchoSelectionControl echoSelection = new EchoSelectionControl();
  public final static SpeakLinesControl speakLines = new SpeakLinesControl();
  public final static SpeechVolumeControl speechVolume = new SpeechVolumeControl();
  public final static SpeechRateControl speechRate = new SpeechRateControl();
  public final static SpeechPitchControl speechPitch = new SpeechPitchControl();
  public final static SpeechBalanceControl speechBalance = new SpeechBalanceControl();
  public final static SleepTalkControl sleepTalk = new SleepTalkControl();

  // keyboard settings
  public final static InputEditingControl inputEditing = new InputEditingControl();
  public final static LongPressControl longPress = new LongPressControl();
  public final static ReversePanningControl reversePanning = new ReversePanningControl();

  // one hand settings
  public final static OneHandControl oneHand = new OneHandControl();
  public final static SpaceTimeoutControl spaceTimeout = new SpaceTimeoutControl();
  public final static PressedTimeoutControl pressedTimeout = new PressedTimeoutControl();

  // remote settings
  public final static RemoteDisplayControl remoteDisplay = new RemoteDisplayControl();
  public final static SecureConnectionControl secureConnection = new SecureConnectionControl();

  // advanced settings
  public final static ComputerBrailleControl computerBraille = new ComputerBrailleControl();
  public final static ScreenOrientationControl screenOrientation = new ScreenOrientationControl();
  public final static PhoneticAlphabetControl phoneticAlphabet = new PhoneticAlphabetControl();

  // developer settings
  public final static CrashEmailsControl crashEmails = new CrashEmailsControl();
  public final static AdvancedActionsControl advancedActions = new AdvancedActionsControl();
  public final static ExtraIndicatorsControl extraIndicators = new ExtraIndicatorsControl();
  public final static EventMessagesControl eventMessages = new EventMessagesControl();
  public final static LogActionsControl logActions = new LogActionsControl();
  public final static LogNavigationControl logNavigation = new LogNavigationControl();
  public final static LogUpdatesControl logUpdates = new LogUpdatesControl();
  public final static LogKeyboardControl logKeyboard = new LogKeyboardControl();
  public final static LogEmulationsControl logEmulations = new LogEmulationsControl();
  public final static LogBrailleControl logBraille = new LogBrailleControl();
  public final static LogSpeechControl logSpeech = new LogSpeechControl();

  static {
    brailleMonitor.addDependencies(brailleEnabled);
    brailleEnabled.addDependencies(brailleFirmness);
    brailleEnabled.addDependencies(selectionIndicator, cursorIndicator);
    brailleEnabled.addDependencies(showHighlighted, showNotifications);
    brailleEnabled.addDependencies(computerBraille, literaryBraille);
    computerBraille.addDependencies(wordWrap);
    literaryBraille.addDependencies(brailleCode);

    sleepTalk.addDependencies(speechEnabled);
    speechEnabled.addDependencies(speechVolume, speechBalance);
    speechEnabled.addDependencies(speechRate, speechPitch);

    oneHand.addDependencies(spaceTimeout, pressedTimeout);
    remoteDisplay.addDependencies(secureConnection);
  }

  public final static Control[] inCreationOrder = Control.getControlsInCreationOrder();
  public final static Control[] inRestoreOrder = Control.getControlsInRestoreOrder();

  public static void saveValues () {
    Control.saveValues(inCreationOrder);
  }

  public static void restoreSavedValues () {
    Control.restoreSavedValues(inRestoreOrder);
  }

  public static void restoreDefaultValues () {
    Control.restoreDefaultValues(inRestoreOrder);
  }

  public static void restoreCurrentValues () {
    Control.restoreCurrentValues(inRestoreOrder);
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
    if (!speechEnabled.getBooleanValue()) brailleEnabled.setValue(true);
    if (ApplicationSettings.ONE_HAND) oneHand.confirmValue();

    typingMode.setValue(TypingMode.TEXT);
    resetHighlightedTyping();
  }
}
