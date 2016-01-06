#include "lljni.h"
#include <android/log.h>

static int
toAndroidLogPriority (int level) {
  switch (level) {
    case LOG_ALL:   return ANDROID_LOG_VERBOSE;
    case LOG_DEBUG: return ANDROID_LOG_DEBUG;
    case LOG_INFO:  return ANDROID_LOG_INFO;
    case LOG_WARN:  return ANDROID_LOG_WARN;
    case LOG_ERROR: return ANDROID_LOG_ERROR;
    case LOG_FATAL: return ANDROID_LOG_FATAL;
    case LOG_OFF:   return ANDROID_LOG_SILENT;
    default:        return ANDROID_LOG_UNKNOWN;
  }
}

void
logPrint (int level, const char *format, ...) {
  va_list args;
  va_start(args, format);
  __android_log_vprint(toAndroidLogPriority(level), "liblouis", format, args);
  va_end(args);
}

static void
logMessage (int level, const char *message) {
  logPrint(level, "%s", message);
}

JNIEXPORT jint
JNI_OnLoad (JavaVM *vm, void *reserved) {
  lou_registerLogCallback(logMessage);
  return JNI_VERSION_1_6;
}
