package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class VoiceOverText extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return VoiceOverActivity.class;
  }

  public VoiceOverText (Endpoint endpoint) {
    super(endpoint, false);
  }
}
