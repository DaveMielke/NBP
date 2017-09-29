package org.nbp.common.speech;
import org.nbp.common.*;

import android.media.AudioManager;
import android.os.Build;

public enum AudioStream {
/*Accessibility(
    AudioManager.STREAM_ACCESSIBILITY,
    Build.VERSION_CODES.O
  ),
*/

  Alarm(
    AudioManager.STREAM_ALARM,
    Build.VERSION_CODES.BASE
  ),

  DTMF(
    AudioManager.STREAM_DTMF,
    Build.VERSION_CODES.ECLAIR
  ),

  Music(
    AudioManager.STREAM_MUSIC,
    Build.VERSION_CODES.BASE
  ),

  Notification(
    AudioManager.STREAM_NOTIFICATION,
    Build.VERSION_CODES.CUPCAKE
  ),

  Ring(
    AudioManager.STREAM_RING,
    Build.VERSION_CODES.BASE
  ),

  System(
    AudioManager.STREAM_SYSTEM,
    Build.VERSION_CODES.BASE
  ),

  Call(
    AudioManager.STREAM_VOICE_CALL,
    Build.VERSION_CODES.BASE
  ),

  ; // end of enumeration

  private final int audioStream;
  private final int sdkVersion;

  private AudioStream (int stream, int version) {
    audioStream = stream;
    sdkVersion = version;
  }

  public final int getAudioStream () {
    return audioStream;
  }

  public final int getSDKVersion () {
    return sdkVersion;
  }
}
