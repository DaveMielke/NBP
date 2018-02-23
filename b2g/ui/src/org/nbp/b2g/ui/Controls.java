package org.nbp.b2g.ui;
import org.nbp.b2g.ui.controls.*;

import org.nbp.common.controls.Control;
import org.nbp.common.controls.BooleanControl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;

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

  public final static InputEditingControl inputEditing = new InputEditingControl();
  public final static LongPressControl longPress = new LongPressControl();
  public final static ReversePanningControl reversePanning = new ReversePanningControl();

  public final static OneHandControl oneHand = new OneHandControl();
  public final static SpaceTimeoutControl spaceTimeout = new SpaceTimeoutControl();
  public final static PressedTimeoutControl pressedTimeout = new PressedTimeoutControl();

  public final static RemoteDisplayControl remoteDisplay = new RemoteDisplayControl();
  public final static SecureConnectionControl secureConnection = new SecureConnectionControl();

  public final static ComputerBrailleControl computerBraille = new ComputerBrailleControl();
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
    echoWords,
    echoCharacters,
    echoDeletions,
    echoSelection,
    speakLines,
    speechVolume,
    speechRate,
    speechPitch,
    speechBalance,
    sleepTalk,

    inputEditing,
    longPress,
    reversePanning,

    oneHand,
    spaceTimeout,
    pressedTimeout,

    remoteDisplay,
    secureConnection,

    computerBraille,
    crashEmails,
    advancedActions,
    extraIndicators,
    eventMessages,
    logActions,
    logNavigation,
    logUpdates,
    logKeyboard,
    logEmulations,
    logBraille,
    logSpeech
  };

  public final static Collection<Control> ALL;
  static {
    ArrayList<Control> list = new ArrayList<Control>(allControls.length);

    for (Control control : allControls) {
      list.add(control);
    }

    list.trimToSize();
    ALL = Collections.unmodifiableCollection(list);
  }

  public static void saveValues () {
    Control.saveValues(allControls);
  }

  public static void restoreSavedValues () {
    Control.restoreSavedValues(allControls);
  }

  public static void restoreDefaultValues () {
    Control.restoreDefaultValues(allControls);
  }

  public static void restoreCurrentValues () {
    Control.restoreCurrentValues(allControls);
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
