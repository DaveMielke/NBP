package org.nbp.compass;

import org.nbp.common.TextSpeaker;
import org.nbp.common.Control;

public class EventAnnouncer extends TextSpeaker {
  private EventAnnouncer () {
    super();
  }

  private final static Object SINGLETON_LOCK = new Object();
  private static EventAnnouncer eventAnnouncer = null;

  public final static EventAnnouncer singleton () {
    synchronized (SINGLETON_LOCK) {
      if (eventAnnouncer == null) eventAnnouncer = new EventAnnouncer();
    }

    return eventAnnouncer;
  }

  @Override
  protected final void initializeProperties () {
    Control.restoreCurrentValues(
      Controls.speechVolume,
      Controls.speechRate,
      Controls.speechPitch,
      Controls.speechBalance
    );
  }
}
