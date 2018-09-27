#include "java.h"

jobject
javaGlobalReference (JNIEnv *env, jobject local) {
  if (!local) return NULL;
  jobject global = (*env)->NewGlobalRef(env, local);

  (*env)->DeleteLocalRef(env, local);
  local = NULL;

  return global;
}

jclass
javaFindClass (JNIEnv *env, jclass *class, const char *name) {
  if (!*class) *class = javaGlobalReference(env, (*env)->FindClass(env, name));
  return *class;
}

jclass
javaFindStringClass (JNIEnv *env) {
  static jclass class = NULL;
  return javaFindClass(env, &class, "java/lang/String");
}
