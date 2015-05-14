#include "utils.h"

#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include <linux/input.h>

#ifndef ABS_CNT
#define ABS_CNT	(ABS_MAX + 1)
#endif /* ABS_CNT */

#ifndef ABS_MT_SLOT
#define ABS_MT_SLOT 0X2F
#endif /* ABS_MT_SLOT */

#ifndef ABS_MT_TRACKING_ID
#define ABS_MT_TRACKING_ID 0X39
#endif /* ABS_MT_TRACKING_ID */

#ifndef ABS_MT_POSITION_X
#define ABS_MT_POSITION_X 0X35
#endif /* ABS_MT_POSITION_X */

#ifndef ABS_MT_POSITION_Y
#define ABS_MT_POSITION_Y 0X36
#endif /* ABS_MT_POSITION_Y */

#include "linux/uinput.h"

typedef uint16_t InputEventType;
typedef uint16_t InputEventCode;
typedef int32_t InputEventValue;

MAKE_FILE_LOG_TAG;

static int
enableUInputEventType (int device, InputEventType type) {
  if (ioctl(device, UI_SET_EVBIT, type) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_EVBIT]");
  return 0;
}

static int
enableUInputKey (int device, InputEventCode key) {
  if (ioctl(device, UI_SET_KEYBIT, key) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_KEYBIT]");
  return 0;
}

static int
writeInputEvent (int device, InputEventType type, InputEventCode code, InputEventValue value) {
  struct input_event event;

  memset(&event, 0, sizeof(event));
  gettimeofday(&event.time, NULL);

  event.type = type;
  event.code = code;
  event.value = value;

LOG(DEBUG, "t=%d c=%d v=%d", type, code, value);
  if (write(device, &event, sizeof(event)) != -1) return 1;
  logSystemError(LOG_TAG, "write[input_event]");
  return 0;
}

static int
writeSynReport (int device) {
  return writeInputEvent(device, EV_SYN, SYN_REPORT, 0);
}

static int
writeKeyEvent (int device, InputEventCode key, InputEventValue press) {
  if (!writeInputEvent(device, EV_KEY, key, (press? 1: 0))) return 0;
  if (!writeSynReport(device)) return 0;
  return 1;
}

static int
writeTouchEvent (int device, InputEventCode action, InputEventValue value) {
  return writeInputEvent(device, EV_ABS, action, value);
}

static int
writeTouchBegin (int device) {
  static InputEventValue identifier = 0;

  if (!writeTouchEvent(device, ABS_MT_SLOT, 0)) return 0;
  if (!writeTouchEvent(device, ABS_MT_TRACKING_ID, ++identifier)) return 0;
  return 1;
}

static int
writeTouchEnd (int device) {
  return writeTouchEvent(device, ABS_MT_TRACKING_ID, -1);
}

static int
writeTouchX (int device, InputEventValue x) {
  return writeTouchEvent(device, ABS_MT_POSITION_X, x);
}

static int
writeTouchY (int device, InputEventValue y) {
  return writeTouchEvent(device, ABS_MT_POSITION_Y, y);
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, openDevice, jint,
  jint width, jint height
) {
  const char *path = "/dev/uinput";
  int device = open(path, O_WRONLY);

  if (device != -1) {
    struct uinput_user_dev description;

    memset(&description, 0, sizeof(description));
    snprintf(description.name, sizeof(description.name), "B2G User Interface");

    {
      char topology[0X40];

      snprintf(topology, sizeof(topology), "%s", PACKAGE_PATH);

      if (ioctl(device, UI_SET_PHYS, topology) == -1) {
        logSystemError(LOG_TAG, "ioctl[UI_SET_PHYS]");
      }
    }

    description.absmin[ABS_MT_SLOT] = 0;
    description.absmax[ABS_MT_SLOT] = 1;

    description.absmin[ABS_MT_TRACKING_ID] = 0;
    description.absmax[ABS_MT_TRACKING_ID] = 1000000;

    description.absmin[ABS_MT_POSITION_X] = 0;
    description.absmax[ABS_MT_POSITION_X] = width - 1;

    description.absmin[ABS_MT_POSITION_Y] = 0;
    description.absmax[ABS_MT_POSITION_Y] = height - 1;

    if (write(device, &description, sizeof(description)) != -1) {
      if (enableUInputEventType(device, EV_SYN)) {
        return device;
      }
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
  org_nbp_b2g_ui_UInputDevice, createDevice, jboolean,
  jint device
) {
  if (ioctl(device, UI_DEV_CREATE) != -1) return JNI_TRUE;
  logSystemError(LOG_TAG, "ioctl[UI_DEV_CREATE]");
  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, closeDevice, void,
  jint device
) {
  if (close(device) == -1) logSystemError(LOG_TAG, "close[uinput]");
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, enableKeyEvents, jboolean,
  jint device
) {
  return enableUInputEventType(device, EV_KEY)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, enableKey, jboolean,
  jint device, jint key
) {
  return enableUInputKey(device, key)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, pressKey, jboolean,
  jint device, jint key
) {
  return writeKeyEvent(device, key, 1)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, releaseKey, jboolean,
  jint device, jint key
) {
  return writeKeyEvent(device, key, 0)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, enableTouchEvents, jboolean,
  jint device
) {
  static const InputEventCode codes[] = {
    ABS_MT_SLOT,
    ABS_MT_TRACKING_ID,
    ABS_MT_POSITION_X,
    ABS_MT_POSITION_Y,
    0
  };
  const InputEventCode *code = codes;

  if (!enableUInputEventType(device, EV_ABS)) return JNI_FALSE;

  while (*code) {
    if (ioctl(device, UI_SET_ABSBIT, *code) == -1) {
      logSystemError(LOG_TAG, "ioctl[UI_SET_ABSBIT]");
      return JNI_FALSE;
    }

    code += 1;
  }

  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, tap, jboolean,
  jint device,
  jint x, jint y
) {
  if (!writeTouchBegin(device)) return JNI_FALSE;

  if (!writeTouchX(device, x)) return JNI_FALSE;
  if (!writeTouchY(device, y)) return JNI_FALSE;
  if (!writeSynReport(device)) return JNI_FALSE;

  if (!writeTouchEnd(device)) return JNI_FALSE;
  if (!writeSynReport(device)) return JNI_FALSE;

  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, swipe, jboolean,
  jint device,
  jint x1, jint y1,
  jint x2, jint y2
) {
  if (!writeTouchBegin(device)) return JNI_FALSE;

  if (!writeTouchX(device, x1)) return JNI_FALSE;
  if (!writeTouchY(device, y1)) return JNI_FALSE;
  if (!writeSynReport(device)) return JNI_FALSE;

  if (!writeTouchX(device, x2)) return JNI_FALSE;
  if (!writeTouchY(device, y2)) return JNI_FALSE;
  if (!writeSynReport(device)) return JNI_FALSE;

  if (!writeTouchEnd(device)) return JNI_FALSE;
  if (!writeSynReport(device)) return JNI_FALSE;

  return JNI_TRUE;
}
