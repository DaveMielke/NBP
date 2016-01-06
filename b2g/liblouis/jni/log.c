#include "lljni.h"
#include <android/log.h>

JAVA_METHOD(
  org_liblouis_Louis, setLogLevel, void,
  jchar character
) {
  int level;

  switch (character) {
    case 'A': level = LOG_ALL;   break;
    case 'D': level = LOG_DEBUG; break;
    case 'I': level = LOG_INFO;  break;
    case 'W': level = LOG_WARN;  break;
    case 'E': level = LOG_ERROR; break;
    case 'F': level = LOG_FATAL; break;
    case 'O': level = LOG_OFF;   break;

    default:
      logPrint(LOG_WARN, "ignoring unrecognized log level character: 0X%02X", character);
      return;
  }

  logPrint(LOG_DEBUG, "setting log level: %c -> %d", character, level);
  lou_setLogLevel(level);
}

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
