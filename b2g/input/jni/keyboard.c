#include <jni.h>

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <dirent.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include <linux/input.h>

#include <android/log.h>

#include "utils.h"

MAKE_FILE_LOG_TAG;

#define KEYBOARD_DEVICE_NAME "cp430_keypad"
#define NO_DEVICE -1
static int keyboardDevice = NO_DEVICE;

static char *
findEventDevice (const char *deviceName) {
  char *devicePath = NULL;
  char directoryPath[0X80];
  DIR *directory;

  snprintf(directoryPath, sizeof(directoryPath),
           "/sys/devices/platform/%s/input", deviceName);

  if ((directory = opendir(directoryPath))) {
    struct dirent *entry;

    while ((entry = readdir(directory))) {
      unsigned int eventNumber;
      char extra;

      if (sscanf(entry->d_name, "input%u%c", &eventNumber, &extra) == 1) {
        char path[0X80];

        snprintf(path, sizeof(path), "/dev/input/event%u", eventNumber);
        if (!(devicePath = strdup(path))) logMallocError(LOG_TAG);
        break;
      }
    }

    closedir(directory);
  } else {
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,
                        "event device input directory open error: %s: %s",
                        directoryPath, strerror(errno));
  }

  return devicePath;
}

static int
openEventDevice (const char *deviceName) {
  char *devicePath = findEventDevice(deviceName);

  if (devicePath) {
    int deviceDescriptor = open(devicePath, O_RDONLY);

    if (deviceDescriptor != NO_DEVICE) {
      if (ioctl(deviceDescriptor, EVIOCGRAB, 1) != -1) {
        __android_log_print(ANDROID_LOG_INFO, LOG_TAG,
                            "Event Device Opened: %s: %s: fd=%d",
                            deviceName, devicePath, deviceDescriptor);

        free(devicePath);
        return 1;
      } else {
        logSystemError(LOG_TAG, "ioctl[EVIOCGRAB]");
      }

      close(deviceDescriptor);
    } else {
      __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,
                          "event device open error: %s: %s",
                          devicePath, strerror(errno));
    }

    free(devicePath);
  }

  return 0;
}

JNIEXPORT jboolean JNICALL
Java_org_nbp_b2g_input_Keyboard_open (
  JNIEnv *env, jobject this
) {
  static const char deviceName[] = KEYBOARD_DEVICE_NAME;
  int deviceDescriptor;

  if (keyboardDevice != NO_DEVICE) return JNI_TRUE;

  if ((deviceDescriptor = openEventDevice(deviceName)) != NO_DEVICE) {
    keyboardDevice = deviceDescriptor;
    return JNI_TRUE;
  }

  return JNI_FALSE;
}

JNIEXPORT void JNICALL
Java_org_nbp_b2g_input_Keyboard_close (
  JNIEnv *env, jobject this
) {
  if (keyboardDevice != NO_DEVICE) {
    close(keyboardDevice);
    keyboardDevice = NO_DEVICE;
  }
}
