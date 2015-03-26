#include "utils.h"

#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/stat.h>

#include <linux/input.h>

#ifndef ABS_CNT
#define ABS_CNT			(ABS_MAX+1)
#endif /* ABS_CNT */

#include "linux/uinput.h"

MAKE_FILE_LOG_TAG;

static int
enableUInputEventType (int device, int type) {
  if (ioctl(device, UI_SET_EVBIT, type) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_EVBIT]");
  return 0;
}

static int
enableUInputKey (int device, int key) {
  if (ioctl(device, UI_SET_KEYBIT, key) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_KEYBIT]");
  return 0;
}

static int
writeInputEvent (int device, int type, int code, int value) {
  struct input_event event;

  memset(&event, 0, sizeof(event));
  gettimeofday(&event.time, NULL);

  event.type = type;
  event.code = code;
  event.value = value;

  if (write(device, &event, sizeof(event)) != -1) return 1;
  logSystemError(LOG_TAG, "write[input_event]");
  return 0;
}

static int
writeSynReport (int device) {
  return writeInputEvent(device, EV_SYN, SYN_REPORT, 0);
}

static int
writeKeyEvent (int device, int key, int press) {
  if (!writeInputEvent(device, EV_KEY, key, (press? 1: 0))) return 0;
  if (!writeSynReport(device)) return 0;
  return 1;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, openDevice, jint
) {
  const char *path = "/dev/uinput";
  int device = open(path, O_WRONLY);

  if (device != -1) {
    struct uinput_user_dev description;

    memset(&description, 0, sizeof(description));
    snprintf(description.name, sizeof(description.name), "B2G Input Service");

    {
      char topology[0X40];

      snprintf(topology, sizeof(topology), "%s", "org.nbp.b2g.input");

      if (ioctl(device, UI_SET_PHYS, topology) == -1) {
        logSystemError(LOG_TAG, "ioctl[UI_SET_PHYS]");
      }
    }

    if (write(device, &description, sizeof(description)) != -1) {
      return device;
    } else {
      logSystemError(LOG_TAG, "write[uinput_user_dev]");
    }

    close(device);
  } else {
    logSystemError(LOG_TAG, "open[uinput]");
  }

  return -1;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, createDevice, jboolean,
  jint device
) {
  if (ioctl(device, UI_DEV_CREATE) != -1) return JNI_TRUE;
  logSystemError(LOG_TAG, "ioctl[UI_DEV_CREATE]");
  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, closeDevice, void,
  jint device
) {
  if (close(device) == -1) logSystemError(LOG_TAG, "close[uinput]");
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, enableKeyEvents, jboolean,
  jint device
) {
  return enableUInputEventType(device, EV_KEY)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, enableKey, jboolean,
  jint device, jint key
) {
  return enableUInputKey(device, key)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, pressKey, jboolean,
  jint device, jint key
) {
  return writeKeyEvent(device, key, 1)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_input_UInputDevice, releaseKey, jboolean,
  jint device, jint key
) {
  return writeKeyEvent(device, key, 0)? JNI_TRUE: JNI_FALSE;
}
