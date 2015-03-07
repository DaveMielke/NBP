#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>

#include <android/log.h>

#include "utils.h"

MAKE_FILE_LOG_TAG;

void
logSystemError (const char *action) {
  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,
                      "system error %d: %s: %s", errno, action, strerror(errno));
}

int
executeHostCommand (const char *command) {
  int status = 0XFF;
  FILE *stream = popen(command, "r");

  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                      "executing host command: %s", command);

  if (stream) {
    char buffer[0X1000];

    while (fgets(buffer, sizeof(buffer), stream)) {
      size_t length = strlen(buffer);

      while (length > 0) {
        size_t index = length - 1;

        if (!isspace(buffer[index])) break;
        buffer[(length = index)] = 0;
      }

      __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,
                          "host command output: %s", buffer);
    }

    if ((status = pclose(stream)) != 0) {
      __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                          "host command failed: %d: %s", status, command);
    }
  } else {
    logSystemError("popen");
  }

  return status;
}

int
isWritable (const char *path) {
  if (access(path,  W_OK) != -1) return 1;
  logSystemError("access[writable]");
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
