#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include "java.h"

jobject
toGlobalReference (JNIEnv *env, jobject local) {
  if (!local) return NULL;
  jobject global = (*env)->NewGlobalRef(env, local);

  (*env)->DeleteLocalRef(env, local);
  local = NULL;

  return global;
}

jclass
findClass (JNIEnv *env, jclass *class, const char *name) {
  if (!*class) *class = toGlobalReference(env, (*env)->FindClass(env, name));
  return *class;
}

jclass
findStringClass (JNIEnv *env) {
  static jclass class = NULL;
  return findClass(env, &class, "java/lang/String");
}

void
throwErrnoException (JNIEnv *env, int error, const char *function, ...) {
  char message[0X100];
  char *position = message;
  const char *end = position + sizeof(message);

  position += snprintf(position, (end - position),
                       "system error %d: %s: %s",
                       error, function, strerror(error));

  {
    va_list arguments;
    va_start(arguments, function);

    while (1) {
      const char *detail = va_arg(arguments, const char *);
      if (!detail) break;
      position += snprintf(position, (end - position), ": %s", detail);
    }

    va_end(arguments);
  }

  {
    static jclass class = NULL;
    (*env)->ThrowNew(env, findClass(env, &class, "org/nbp/common/ErrnoException"), message);
  }
}
