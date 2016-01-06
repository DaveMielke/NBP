#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <liblouis.h>
#include <jni.h>

extern void logPrint (
  int level, const char *format, ...
) __attribute__((format(printf, 2, 3)));

#define JAVA_METHOD(object, name, type, ...) \
  JNIEXPORT type JNICALL Java_ ## object ## _ ## name ( \
    JNIEnv *env, jobject this, ## __VA_ARGS__ \
  )
