package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Map;
import java.util.HashMap;

import android.os.Build;
import android.content.Context;
import android.media.AudioManager;

public enum AudioStream {
  MUSIC(
    AudioManager.STREAM_MUSIC,
    Build.VERSION_CODES.BASE
  ),

  NOTIFICATION(
    AudioManager.STREAM_NOTIFICATION,
    Build.VERSION_CODES.CUPCAKE
  ),

  ALARM(
    AudioManager.STREAM_ALARM,
    Build.VERSION_CODES.BASE
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

  DTMF(
    AudioManager.STREAM_DTMF,
    Build.VERSION_CODES.ECLAIR
  ),

  ACCESSIBILITY(
    AudioManager.STREAM_ACCESSIBILITY,
    Build.VERSION_CODES.O
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

  public final void setNormalizedVolume (float volume) {
    int maximum = getMaximumVolume();
    int current = Math.round(volume * (float)maximum);

    current = Math.min(current, maximum);
    current = Math.max(current, 0);

    setCurrentVolume(current);
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

  public static AudioStream getLoudestStream () {
    AudioStream loudestStream = null;
    float loudestVolume = -1f;

    for (AudioStream stream : values()) {
      if (!stream.isAvailable()) continue;
      float volume = stream.getNormalizedVolume();

      if (volume > loudestVolume) {
        loudestVolume = volume;
        loudestStream = stream;
      }

      if (loudestVolume == 1f) break;
    }

    return loudestStream;
  }

  private final static Object STREAM_MAP_LOCK = new Object();
  private static Map<Integer, AudioStream> streamMap = null;

  private static Map<Integer, AudioStream> makeStreamMap () {
    Map<Integer, AudioStream> map = new HashMap<Integer, AudioStream>();

    for (AudioStream stream : values()) {
      map.put(stream.getStreamNumber(), stream);
    }

    return map;
  }

  public static AudioStream getAudioStream (int stream) {
    synchronized (STREAM_MAP_LOCK) {
      if (streamMap == null) streamMap = makeStreamMap();
    }

    return streamMap.get(stream);
  }
}
