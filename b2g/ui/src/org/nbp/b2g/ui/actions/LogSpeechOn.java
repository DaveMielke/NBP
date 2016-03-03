package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogSpeechOn extends NextValueAction {
  public LogSpeechOn (Endpoint endpoint) {
    super(endpoint, Controls.getLogSpeechControl(), true);
  }
}
