#include "lljni.h"

JAVA_METHOD(
  org_liblouis_InternalTable, getEmphasisBit, jshort,
  jstring jTableList, jstring jEmphasisClass
) {
  const char *cTableList = (*env)->GetStringUTFChars(env, jTableList, NULL);
  const char *cEmphasisClass = (*env)->GetStringUTFChars(env, jEmphasisClass, NULL);
  formtype bit = lou_getTypeformForEmphClass(cTableList, cEmphasisClass);

  (*env)->ReleaseStringUTFChars(env, jTableList, cTableList);
  (*env)->ReleaseStringUTFChars(env, jEmphasisClass, cEmphasisClass);
  return bit;
}

JAVA_METHOD(
  org_liblouis_InternalTable, addRule, jboolean,
  jstring jTableList, jstring jRule
) {
  const char *cTableList = (*env)->GetStringUTFChars(env, jTableList, NULL);
  const char *cRule = (*env)->GetStringUTFChars(env, jRule, NULL);
  int successful = lou_compileString(cTableList, cRule);

  (*env)->ReleaseStringUTFChars(env, jTableList, cTableList);
  (*env)->ReleaseStringUTFChars(env, jRule, cRule);
  return successful? JNI_TRUE: JNI_FALSE;
}
