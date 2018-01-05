package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

import android.content.Context;
import android.telephony.TelephonyManager;

public class AnswerCall extends Action {
  @Override
  public boolean performAction () {
    TelephonyManager tm = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);

    if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
      ApplicationUtilities.message(R.string.answerCall_message_not_ringing);
      return false;
    }

    try {
      return InputService.injectKey(KeyEvent.KEYCODE_HEADSETHOOK);
    } finally {
      if (!Endpoints.setHostEndpoint()) return false;
    }
  }

  public AnswerCall (Endpoint endpoint) {
    super(endpoint, false);
  }
}
