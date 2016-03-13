package org.nbp.phone;

import android.text.TextWatcher;
import android.text.Editable;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class PhoneNumberWatcher implements TextWatcher {
  private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, 100);

  private final void playTone (int tone) {
    toneGenerator.startTone(tone, 100);
  }

  @Override
  public final void beforeTextChanged (CharSequence text, int start, int before, int after) {
  }

  @Override
  public final void onTextChanged (CharSequence text, int start, int before, int after) {
    for (int offset=0; offset<after; offset+=1) {
      char character = text.charAt(start + offset);

      switch (character) {
        case '1':
          playTone(ToneGenerator.TONE_DTMF_1);
          break;

        case '2':
          playTone(ToneGenerator.TONE_DTMF_2);
          break;

        case '3':
          playTone(ToneGenerator.TONE_DTMF_3);
          break;

        case '4':
          playTone(ToneGenerator.TONE_DTMF_4);
          break;

        case '5':
          playTone(ToneGenerator.TONE_DTMF_5);
          break;

        case '6':
          playTone(ToneGenerator.TONE_DTMF_6);
          break;

        case '7':
          playTone(ToneGenerator.TONE_DTMF_7);
          break;

        case '8':
          playTone(ToneGenerator.TONE_DTMF_8);
          break;

        case '9':
          playTone(ToneGenerator.TONE_DTMF_9);
          break;

        case '*':
          playTone(ToneGenerator.TONE_DTMF_S);
          break;

        case '0':
          playTone(ToneGenerator.TONE_DTMF_0);
          break;

        case '#':
          playTone(ToneGenerator.TONE_DTMF_P);
          break;

        default:
          break;
      }
    }
  }

  @Override
  public final void afterTextChanged (Editable editable) {
  }

  public PhoneNumberWatcher () {
  }
}
