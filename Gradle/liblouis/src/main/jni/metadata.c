#include "lljni.h"

static void
getClass (JNIEnv *env, jclass *class, const char *path) {
  if (!*class) *class = (*env)->FindClass(env, path);
}

static jclass
getStringClass (JNIEnv *env) {
  static jclass class = 0;
  getClass(env, &class, "java/lang/String");
  return class;
}

static jstring
toString (JNIEnv *env, char *cString) {
  if (!cString) return NULL;
  jstring jString = (*env)->NewStringUTF(env, cString);
  free(cString);
  return jString;
}

static jobjectArray
toStringArray (JNIEnv *env, char **cArray) {
  if (!cArray) return NULL;

  jsize size = 0;
  while (cArray[size]) size += 1;
  jobjectArray jArray = (*env)->NewObjectArray(env, size, getStringClass(env), NULL);

  for (int index=0; index<size; index+=1) {
    (*env)->SetObjectArrayElement(env, jArray, index, toString(env, cArray[index]));
  }

  free(cArray);
  return jArray;
}

JAVA_METHOD(
  org_liblouis_Metadata, listTables, jobjectArray
) {
  return toStringArray(env, lou_listTables());
}

JAVA_METHOD(
  org_liblouis_Metadata, findTables, jobjectArray,
  jstring jQuery
) {
  const char *cQuery = (*env)->GetStringUTFChars(env, jQuery, NULL);
  jobjectArray jArray = toStringArray(env, lou_findTables(cQuery));

  (*env)->ReleaseStringUTFChars(env, jQuery, cQuery);
  return jArray;
}

JAVA_METHOD(
  org_liblouis_Metadata, findTable, jstring,
  jstring jQuery
) {
  const char *cQuery = (*env)->GetStringUTFChars(env, jQuery, NULL);
  jstring jTable = toString(env, lou_findTable(cQuery));

  (*env)->ReleaseStringUTFChars(env, jQuery, cQuery);
  return jTable;
}

JAVA_METHOD(
  org_liblouis_Metadata, getValueForKey, jstring,
  jstring jTable, jstring jKey
) {
  const char *cTable = (*env)->GetStringUTFChars(env, jTable, NULL);
  const char *cKey = (*env)->GetStringUTFChars(env, jKey, NULL);
  jstring jValue = toString(env, lou_getTableInfo(cTable, cKey));

  (*env)->ReleaseStringUTFChars(env, jTable, cTable);
  (*env)->ReleaseStringUTFChars(env, jKey, cKey);
  return jValue;
}
