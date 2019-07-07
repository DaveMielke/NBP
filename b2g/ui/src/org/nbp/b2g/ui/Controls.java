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
  public final static InputEditingControl inputEditing = new InputEditingControl();
  public final static TypingModeControl typingMode = new TypingModeControl();
  public final static TypingBoldControl typingBold = new TypingBoldControl();
  public final static TypingItalicControl typingItalic = new TypingItalicControl();
  public final static TypingStrikeControl typingStrike = new TypingStrikeControl();
  public final static TypingUnderlineControl typingUnderline = new TypingUnderlineControl();

  // keyboard settings
  public final static LongPressControl longPress = new LongPressControl();
  public final static ReversePanningControl reversePanning = new ReversePanningControl();

  // braille settings
  public final static ShowHighlightedControl showHighlighted = new ShowHighlightedControl();
  public final static SelectionIndicatorControl selectionIndicator = new SelectionIndicatorControl();
  public final static CursorIndicatorControl cursorIndicator = new CursorIndicatorControl();
  public final static BrailleFirmnessControl brailleFirmness = new BrailleFirmnessControl();
  public final static BrailleMonitorControl brailleMonitor = new BrailleMonitorControl();
  public final static BrailleEnabledControl brailleEnabled = new BrailleEnabledControl();

  // speech settings
  public final static SpeechEnabledControl speechEnabled = new SpeechEnabledControl();
  public final static SpeechVolumeControl speechVolume = new SpeechVolumeControl();
  public final static SpeechRateControl speechRate = new SpeechRateControl();
  public final static SpeechPitchControl speechPitch = new SpeechPitchControl();
  public final static SpeechBalanceControl speechBalance = new SpeechBalanceControl();
  public final static SleepTalkControl sleepTalk = new SleepTalkControl();

  // speech feedback
  public final static EchoWordsControl echoWords = new EchoWordsControl();
  public final static EchoCharactersControl echoCharacters = new EchoCharactersControl();
  public final static EchoDeletionsControl echoDeletions = new EchoDeletionsControl();
  public final static EchoSelectionControl echoSelection = new EchoSelectionControl();
  public final static SpeakLinesControl speakLines = new SpeakLinesControl();

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
    brailleEnabled.addDependencies(brailleFirmness, logBraille);
    brailleEnabled.addDependencies(selectionIndicator, cursorIndicator);
    brailleEnabled.addDependencies(showHighlighted, showNotifications);
    brailleEnabled.addDependencies(literaryBraille);
    literaryBraille.addDependencies(brailleCode, computerBraille);
    computerBraille.addDependencies(wordWrap);

    sleepTalk.addDependencies(speechEnabled);
    speechEnabled.addDependencies(speechVolume, speechBalance);
    speechEnabled.addDependencies(speechRate, speechPitch);
    speechEnabled.addDependencies(logSpeech);

    oneHand.addDependencies(spaceTimeout, pressedTimeout);
    remoteDisplay.addDependencies(secureConnection);
  }

  public final static Control[] inCreationOrder = Control.getControlsInCreationOrder();
  public final static Control[] inRestoreOrder = Control.getControlsInRestoreOrder();

  public static void saveValues () {
    Control.saveValues(inCreationOrder);
  }

  private final static Object RESTORE_LOCK = new Object();
  private static boolean restoringControls = false;

  public static boolean amRestoringControls () {
    synchronized (RESTORE_LOCK) {
      return restoringControls;
    }
  }

  private static void preRestore () {
    restoringControls = true;
  }

  private static void postRestore () {
    if (!speechEnabled.getBooleanValue()) brailleEnabled.setValue(true);
    restoringControls = false;
  }

  public static void restoreDefaultValues () {
    synchronized (RESTORE_LOCK) {
      preRestore();
      Control.restoreDefaultValues(inRestoreOrder);
      postRestore();
    }
  }

  public static void restoreSavedValues () {
    synchronized (RESTORE_LOCK) {
      preRestore();
      Control.restoreSavedValues(inRestoreOrder);
      postRestore();
    }
  }

  public static void restoreCurrentValues () {
    synchronized (RESTORE_LOCK) {
      preRestore();
      ApplicationSettings.BRAILLE_ENABLED = false;
      ApplicationSettings.SPEECH_ENABLED = false;
      Control.restoreCurrentValues(inRestoreOrder);
      postRestore();
    }
  }

  private final static BooleanControl[] highlightedTypingControls =
    new BooleanControl[] {
      typingBold,
      typingItalic,
      typingStrike,
      typingUnderline
    };

  public static void resetHighlightedTypingControls () {
    for (BooleanControl control : highlightedTypingControls) {
      control.setValue(false);
    }
  }

  public static void restoreSaneValues () {
    if (ApplicationSettings.ONE_HAND) oneHand.confirmValue();

    typingMode.setValue(TypingMode.TEXT);
    resetHighlightedTypingControls();
  }
}
