#include "lljni.h"

JAVA_METHOD(
  org_liblouis_Metadata, findTable, jstring,
  jstring jQuery
) {
  const char *cQuery = (*env)->GetStringUTFChars(env, jQuery, NULL);

  char *cTable = lou_findTable(cQuery);
  jstring jTable;

  if (cTable) {
    jTable = (*env)->NewStringUTF(env, cTable);
    free(cTable);
  } else {
    jTable = NULL;
  }

  (*env)->ReleaseStringUTFChars(env, jQuery, cQuery);
  return jTable;
}

JAVA_METHOD(
  org_liblouis_Metadata, getValueForKey, jstring,
  jstring jTable, jstring jKey
) {
  const char *cTable = (*env)->GetStringUTFChars(env, jTable, NULL);
  const char *cKey = (*env)->GetStringUTFChars(env, jKey, NULL);

  char *cValue = lou_getTableInfo(cTable, cKey);
  jstring jValue;

  if (cValue) {
    jValue = (*env)->NewStringUTF(env, cValue);
    free(cValue);
  } else {
    jValue = NULL;
  }

  (*env)->ReleaseStringUTFChars(env, jTable, cTable);
  (*env)->ReleaseStringUTFChars(env, jKey, cKey);
  return jValue;
}
