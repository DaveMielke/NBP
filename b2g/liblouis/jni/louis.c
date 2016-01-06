#include "lljni.h"

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
  org_liblouis_Louis, compileTranslationTable, jboolean,
  jstring jTable
) {
  jboolean isCopy;
  const char *cTable = (*env)->GetStringUTFChars(env, jTable, &isCopy);
  logPrint(LOG_DEBUG, "compiling translation table: %s", cTable);
  void *table = lou_getTable(cTable);
  (*env)->ReleaseStringUTFChars(env, jTable, cTable);
  return table? JNI_TRUE: JNI_FALSE;
}
