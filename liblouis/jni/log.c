#include "lljni.h"
#include <android/log.h>

static int
toAndroidLogPriority (logLevels level) {
  switch (level) {
    case LOU_LOG_ALL:   return ANDROID_LOG_VERBOSE;
    case LOU_LOG_DEBUG: return ANDROID_LOG_DEBUG;
    case LOU_LOG_INFO:  return ANDROID_LOG_INFO;
    case LOU_LOG_WARN:  return ANDROID_LOG_WARN;
    case LOU_LOG_ERROR: return ANDROID_LOG_ERROR;
    case LOU_LOG_FATAL: return ANDROID_LOG_FATAL;
    case LOU_LOG_OFF:   return ANDROID_LOG_SILENT;
    default:        return ANDROID_LOG_UNKNOWN;
  }
}

void
logPrint (logLevels level, const char *format, ...) {
  va_list args;
  va_start(args, format);
  __android_log_vprint(toAndroidLogPriority(level), "liblouis", format, args);
  va_end(args);
}

static void
logMessage (logLevels level, const char *message) {
  logPrint(level, "%s", message);
}

JNIEXPORT jint
JNI_OnLoad (JavaVM *vm, void *reserved) {
  lou_registerLogCallback(logMessage);
  return JNI_VERSION_1_6;
}
