#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>

#include <jni.h>

#include <android/log.h>

#define PACKAGE_PATH "org.nbp.b2g.input"
#define MAKE_LOG_TAG(component) static const char LOG_TAG[] = PACKAGE_PATH ".JNI." component
#define MAKE_FILE_LOG_TAG MAKE_LOG_TAG(__FILE__)

#define LOG(level, ...) __android_log_print(ANDROID_LOG_##level, LOG_TAG, __VA_ARGS__)
extern void logSystemError (const char *tag, const char *action);
extern void logMallocError (const char *tag);

extern int executeHostCommand (const char *command);

extern int isWritable (const char *path);
extern int makeWritable (const char *path);

extern int awaitInput (int fileDescriptor);

extern int checkException (JNIEnv *env);
