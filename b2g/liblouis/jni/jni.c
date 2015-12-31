#include <stdlib.h>
#include <unistd.h>
#include <android/log.h>
#include <liblouis.h>
#include <jni.h>

#define JAVA_METHOD(object, name, type, ...) \
  JNIEXPORT type JNICALL Java_ ## object ## _ ## name ( \
    JNIEnv *env, jobject this, ## __VA_ARGS__ \
  )

#define LOG_TAG "liblouis/JNI"
#define LOG(priority,...) \
  __android_log_print(ANDROID_LOG_##priority, LOG_TAG, __VA_ARGS__)

JAVA_METHOD(
  org_liblouis_Translation, translate, jboolean,
  jstring jTable, jstring jText, jcharArray jBraille,
  jintArray jOutputOffsets, jintArray jInputOffsets, jintArray jResultValues
) {
  const char *cTable = (*env)->GetStringUTFChars(env, jTable, NULL);
  const jchar *cText = (*env)->GetStringChars(env, jText, NULL);
  jchar *cBraille = (*env)->GetCharArrayElements(env, jBraille, NULL);
  jint *cOutputOffsets = (*env)->GetIntArrayElements(env, jOutputOffsets, NULL);
  jint *cInputOffsets = (*env)->GetIntArrayElements(env, jInputOffsets, NULL);
  jint *cResultValues = (*env)->GetIntArrayElements(env, jResultValues, NULL);

  jint *textLength    = &cResultValues[0];
  jint *brailleLength = &cResultValues[1];
  jint *cursorOffset  = &cResultValues[2];

  if (*cursorOffset < 0) cursorOffset = NULL;
  int successful = lou_translate(cTable,
                                 cText, textLength,
                                 cBraille, brailleLength,
                                 NULL, NULL,
                                 cOutputOffsets, cInputOffsets,
                                 cursorOffset, (dotsIO | ucBrl));

  (*env)->ReleaseStringUTFChars(env, jTable, cTable);
  (*env)->ReleaseStringChars(env, jText, cText);
  (*env)->ReleaseCharArrayElements(env, jBraille, cBraille, 0);
  (*env)->ReleaseIntArrayElements(env, jOutputOffsets, cOutputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jInputOffsets, cInputOffsets, 0);
  (*env)->ReleaseIntArrayElements(env, jResultValues, cResultValues, 0);

  return successful? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_liblouis_Louis, getVersion, jstring
) {
  return (*env)->NewStringUTF(env, lou_version());
}

JAVA_METHOD(
  org_liblouis_Louis, setDataPath, void,
  jstring jPath
) {
  jboolean isCopy;
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, &isCopy);
  LOG(DEBUG, "setting data path: %s", cPath);
  lou_setDataPath(cPath);
  (*env)->ReleaseStringUTFChars(env, jPath, cPath);
}

JAVA_METHOD(
  org_liblouis_Louis, setLogLevel, void,
  jchar character
) {
  int level;

  switch (character) {
    case 'A': level = LOG_ALL;   break;
    case 'D': level = LOG_DEBUG; break;
    case 'I': level = LOG_INFO;  break;
    case 'W': level = LOG_WARN;  break;
    case 'E': level = LOG_ERROR; break;
    case 'F': level = LOG_FATAL; break;
    case 'O': level = LOG_OFF;   break;

    default:
      LOG(WARN, "ignoring unrecognized log level character: 0X%02X", character);
      return;
  }

  LOG(DEBUG, "setting log level: %c -> %d", character, level);
  lou_setLogLevel(level);
}

static int
toAndroidLogPriority (int level) {
  switch (level) {
    case LOG_ALL:   return ANDROID_LOG_VERBOSE;
    case LOG_DEBUG: return ANDROID_LOG_DEBUG;
    case LOG_INFO:  return ANDROID_LOG_INFO;
    case LOG_WARN:  return ANDROID_LOG_WARN;
    case LOG_ERROR: return ANDROID_LOG_ERROR;
    case LOG_FATAL: return ANDROID_LOG_FATAL;
    case LOG_OFF:   return ANDROID_LOG_SILENT;
    default:        return ANDROID_LOG_UNKNOWN;
  }
}

static void
logMessage (int level, const char *message) {
  __android_log_write(toAndroidLogPriority(level), "liblouis", message);
}

JNIEXPORT jint
JNI_OnLoad (JavaVM *vm, void *reserved) {
  lou_registerLogCallback(logMessage);
  return JNI_VERSION_1_6;
}
