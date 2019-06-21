#include "lljni.h"

JAVA_METHOD(
  org_liblouis_Louis, setLogLevel, void,
  jchar character
) {
  int level;

  switch (character) {
    case 'A': level = LOU_LOG_ALL;   break;
    case 'D': level = LOU_LOG_DEBUG; break;
    case 'I': level = LOU_LOG_INFO;  break;
    case 'W': level = LOU_LOG_WARN;  break;
    case 'E': level = LOU_LOG_ERROR; break;
    case 'F': level = LOU_LOG_FATAL; break;
    case 'O': level = LOU_LOG_OFF;   break;

    default:
      logPrint(LOU_LOG_WARN, "ignoring unrecognized log level character: 0X%02X", character);
      return;
  }

  logPrint(LOU_LOG_DEBUG, "setting log level: %c -> %d", character, level);
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
  logPrint(LOU_LOG_DEBUG, "setting data path: %s", cPath);
  lou_setDataPath(cPath);
  (*env)->ReleaseStringUTFChars(env, jPath, cPath);
}

JAVA_METHOD(
  org_liblouis_Louis, compileTable, jboolean,
  jstring jTableList
) {
  jboolean isCopy;
  const char *cTableList = (*env)->GetStringUTFChars(env, jTableList, &isCopy);
  logPrint(LOU_LOG_DEBUG, "compiling table: %s", cTableList);
  void *table = lou_getTable(cTableList);
  (*env)->ReleaseStringUTFChars(env, jTableList, cTableList);
  return table? JNI_TRUE: JNI_FALSE;
}
