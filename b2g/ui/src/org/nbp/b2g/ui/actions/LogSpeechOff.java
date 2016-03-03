package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogSpeechOff extends PreviousValueAction {
  public LogSpeechOff (Endpoint endpoint) {
    super(endpoint, Controls.getLogSpeechControl(), true);
  }
}
