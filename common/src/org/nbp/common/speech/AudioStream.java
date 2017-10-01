package org.nbp.common.speech;
import org.nbp.common.*;

import android.os.Build;
import android.content.Context;
import android.media.AudioManager;

public enum AudioStream {
/*ACCESSIBILITY(
    AudioManager.STREAM_ACCESSIBILITY,
    Build.VERSION_CODES.O
  ),
*/

  ALARM(
    AudioManager.STREAM_ALARM,
    Build.VERSION_CODES.BASE
  ),

  DTMF(
    AudioManager.STREAM_DTMF,
    Build.VERSION_CODES.ECLAIR
  ),

  MUSIC(
    AudioManager.STREAM_MUSIC,
    Build.VERSION_CODES.BASE
  ),

  NOTIFICATION(
    AudioManager.STREAM_NOTIFICATION,
    Build.VERSION_CODES.CUPCAKE
  ),

  RING(
    AudioManager.STREAM_RING,
    Build.VERSION_CODES.BASE
  ),

  SYSTEM(
    AudioManager.STREAM_SYSTEM,
    Build.VERSION_CODES.BASE
  ),

  CALL(
    AudioManager.STREAM_VOICE_CALL,
    Build.VERSION_CODES.BASE
  ),

  ; // end of enumeration

  private final int audioStream;
  private final int minimumSDK;

  private AudioStream (int stream, int sdk) {
    audioStream = stream;
    minimumSDK = sdk;
  }

  public final int getAudioStream () {
    return audioStream;
  }

  public final int getMinimumSDK () {
    return minimumSDK;
  }

  public final boolean isAvailable () {
    return CommonUtilities.haveAndroidSDK(minimumSDK);
  }

  private final static Object AUDIO_MANAGER_LOCK = new Object();
  private static AudioManager audioManager = null;

  private final static AudioManager getAudioManager () {
    synchronized (AUDIO_MANAGER_LOCK) {
      if (audioManager == null) {
        audioManager = CommonContext.getAudioManager();
      }
    }

    return audioManager;
  }

  public final int getCurrentVolume () {
    return getAudioManager().getStreamVolume(audioStream);
  }

  public final int getMaximumVolume () {
    return getAudioManager().getStreamMaxVolume(audioStream);
  }

  public final void setVolume (int volume) {
    getAudioManager().setStreamVolume(audioStream, volume, 0);
  }

  private final void adjustVolume (int direction) {
    getAudioManager().adjustStreamVolume(audioStream, direction, 0);
  }

  public final void increaseVolume () {
    adjustVolume(AudioManager.ADJUST_RAISE);
  }

  public final void decreaseVolume () {
    adjustVolume(AudioManager.ADJUST_LOWER);
  }
}
