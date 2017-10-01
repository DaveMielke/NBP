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

  private final int streamNumber;
  private final int minimumSDK;

  private AudioStream (int stream, int sdk) {
    streamNumber = stream;
    minimumSDK = sdk;
  }

  public final int getStreamNumber () {
    return streamNumber;
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

  public final int getMaximumVolume () {
    return getAudioManager().getStreamMaxVolume(streamNumber);
  }

  public final int getCurrentVolume () {
    return getAudioManager().getStreamVolume(streamNumber);
  }

  public final void setCurrentVolume (int volume) {
    getAudioManager().setStreamVolume(streamNumber, volume, 0);
  }

  public final float getNormalizedVolume () {
    return (float)getCurrentVolume() / (float)getMaximumVolume();
  }

  private final void adjustVolume (int direction) {
    getAudioManager().adjustStreamVolume(streamNumber, direction, 0);
  }

  public final void increaseVolume () {
    adjustVolume(AudioManager.ADJUST_RAISE);
  }

  public final void decreaseVolume () {
    adjustVolume(AudioManager.ADJUST_LOWER);
  }
}
