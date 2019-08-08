#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <string.h>
#include "liblouis.h"
#include <jni.h>

extern void logPrint (
  logLevels level, const char *format, ...
) __attribute__((format(printf, 2, 3)));

#define JAVA_METHOD(object, name, type, ...) \
  JNIEXPORT type JNICALL Java_ ## object ## _ ## name ( \
    JNIEnv *env, jobject class, ## __VA_ARGS__ \
  )
