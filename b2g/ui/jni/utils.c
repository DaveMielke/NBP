#include "utils.h"

#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <poll.h>

MAKE_FILE_LOG_TAG;

void
logSystemError (const char *tag, const char *action) {
  __android_log_print(ANDROID_LOG_ERROR, tag,
                      "system error %d: %s: %s",
                      errno, action, strerror(errno));
}

void
logMallocError (const char *tag) {
  logSystemError(tag, "malloc");
}

int
executeHostCommand (const char *command) {
  int status = 0XFF;
  FILE *stream = popen(command, "r");

  LOG(DEBUG, "executing host command: %s", command);

  if (stream) {
    char buffer[0X1000];

    while (fgets(buffer, sizeof(buffer), stream)) {
      size_t length = strlen(buffer);

      while (length > 0) {
        size_t index = length - 1;

        if (!isspace(buffer[index])) break;
        buffer[(length = index)] = 0;
      }

      LOG(VERBOSE, "host command output: %s", buffer);
    }

    if ((status = pclose(stream)) != 0) {
      LOG(DEBUG, "host command failed: %d: %s", status, command);
    }
  } else {
    logSystemError(LOG_TAG, "popen");
  }

  return status;
}

int
isWritable (const char *path) {
  if (access(path,  W_OK) != -1) return 1;
  logSystemError(LOG_TAG, "access[writable]");
  return 0;
}

int
makeWritable (const char *path) {
  if (!isWritable(path)) {
    char command[0X100];
    snprintf(command, sizeof(command), "su -c 'chmod 666 %s'", path);
    int status = executeHostCommand(command);

    if (status != 0) return 0;
  }

  return 1;
}

int
awaitInput (int fileDescriptor) {
  struct pollfd pfd;
  int result;

  memset(&pfd, 0, sizeof(pfd));
  pfd.events = POLLIN;
  result = poll(&pfd, 1, -1);

  if (result == -1) {
    logSystemError(LOG_TAG, "poll");
  } else if (result != 1) {
    LOG(ERROR, "unexpected poll result: %d", result);
  } else if (pfd.revents & POLLIN) {
    return 1;
  } else {
    LOG(ERROR, "unexpected poll event: 0X%02X", pfd.revents);
  }

  return 0;
}

int
checkException (JNIEnv *env) {
  if (!(*env)->ExceptionCheck(env)) return 0;
  (*env)->ExceptionDescribe(env);
  (*env)->ExceptionClear(env);
  return 1;
}
