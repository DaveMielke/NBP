#include "lljni.h"

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
      logPrint(LOG_WARN, "ignoring unrecognized log level character: 0X%02X", character);
      return;
  }

  logPrint(LOG_DEBUG, "setting log level: %c -> %d", character, level);
  lou_setLogLevel(level);
}

JAVA_METHOD(
  org_liblouis_Louis, releaseMemory, void
) {
  lou_free();
}

static jstring
makeStringObject (JNIEnv *env, const char *string) {
  if (!string) return NULL;
  return (*env)->NewStringUTF(env, string);
}

JAVA_METHOD(
  org_liblouis_Louis, getVersion, jstring
) {
  return makeStringObject(env, lou_version());
}

JAVA_METHOD(
  org_liblouis_Louis, getDataPath, jstring
) {
  return makeStringObject(env, lou_getDataPath());
}

JAVA_METHOD(
  org_liblouis_Louis, setDataPath, void,
  jstring jPath
) {
  jboolean isCopy;
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, &isCopy);
  logPrint(LOG_DEBUG, "setting data path: %s", cPath);
  lou_setDataPath(cPath);
  (*env)->ReleaseStringUTFChars(env, jPath, cPath);
}

JAVA_METHOD(
  org_liblouis_Louis, compileTable, jboolean,
  jstring jPath
) {
  jboolean isCopy;
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, &isCopy);
  logPrint(LOG_DEBUG, "compiling table: %s", cPath);
  void *table = lou_getTable(cPath);
  (*env)->ReleaseStringUTFChars(env, jPath, cPath);
  return table? JNI_TRUE: JNI_FALSE;
}
