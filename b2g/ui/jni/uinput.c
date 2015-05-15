#define USE_MULTI_TOUCH_INTERFACE 0
#define UINPUT_TOUCH_KEY BTN_LEFT

#include "utils.h"

#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include <linux/input.h>

#ifndef KEY_CNT
#define KEY_CNT	(KEY_MAX + 1)
#endif /* KEY_CNT */

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
enableUInputEventCodes (
  int device, const InputEventCode *codes, InputEventCode end,
  int (*enableType) (int device),
  int (*enableCode) (int device, InputEventCode code)
) {
  if (*codes != end) {
    const InputEventCode *code = codes;

    if (!enableType(device)) return 0;

    do {
      if (!enableCode(device, *code)) return 0;
    } while (*(code += 1) != end);
  }

  return 1;
}

static int
writeInputEvent (int device, InputEventType type, InputEventCode code, InputEventValue value) {
  struct input_event event;

  memset(&event, 0, sizeof(event));
  gettimeofday(&event.time, NULL);

  event.type = type;
  event.code = code;
  event.value = value;

  LOG(VERBOSE,
    "sending input event: Type:%d Code:%d Value:%d",
    type, code, value
  );

  if (write(device, &event, sizeof(event)) != -1) return 1;
  logSystemError(LOG_TAG, "write[input_event]");
  return 0;
}

static int
writeSynReport (int device) {
  return writeInputEvent(device, EV_SYN, SYN_REPORT, 0);
}

static int
enableUInputKeyEvents (int device) {
  return enableUInputEventType(device, EV_KEY);
}

static int
enableUInputKeyCode (int device, InputEventCode code) {
  if (ioctl(device, UI_SET_KEYBIT, code) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_KEYBIT]");
  LOG(DEBUG, "failing key code: %d", code);
  return 0;
}

static int
enableUInputKeyCodes (int device, const InputEventCode *codes) {
  return enableUInputEventCodes(device, codes, KEY_CNT,
                                enableUInputKeyEvents, enableUInputKeyCode);
}

static int
enableUInputAbsEvents (int device) {
  return enableUInputEventType(device, EV_ABS);
}

static int
enableUInputAbsCode (int device, InputEventCode code) {
  if (ioctl(device, UI_SET_ABSBIT, code) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_ABSBIT]");
  LOG(DEBUG, "failing abs code: %d", code);
  return 0;
}

static int
enableUInputAbsCodes (int device, const InputEventCode *codes) {
  return enableUInputEventCodes(device, codes, ABS_CNT,
                                enableUInputAbsEvents, enableUInputAbsCode);
}

static int
writeKeyEvent (int device, InputEventCode key, InputEventValue press) {
  if (!writeInputEvent(device, EV_KEY, key, (press? 1: 0))) return 0;
  if (!writeSynReport(device)) return 0;
  return 1;
}

static int
writeAbsEvent (int device, InputEventCode action, InputEventValue value) {
  return writeInputEvent(device, EV_ABS, action, value);
}

typedef struct {
  const char *path;
  int device;
  struct uinput_user_dev properties;
} UinputDescriptor;

#define UINPUT_DESCRIPTOR UinputDescriptor *ui = (*env)->GetDirectBufferAddress(env, uinput)

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, openDevice, jobject,
  jstring jName
) {
  UinputDescriptor *ui;

  if ((ui = malloc(sizeof(*ui)))) {
    memset(ui, 0, sizeof(*ui));
    ui->path = "/dev/uinput";

    if ((ui->device = open(ui->path, O_WRONLY)) != -1) {
      {
        jboolean isCopy;
        const char *cName = (*env)->GetStringUTFChars(env, jName, &isCopy);

        snprintf(ui->properties.name, sizeof(ui->properties.name), "%s", cName);
        (*env)->ReleaseStringUTFChars(env, jName, cName);
      }

      {
        char topology[0X40];
        snprintf(topology, sizeof(topology), "pid-%d", getpid());

        if (ioctl(ui->device, UI_SET_PHYS, topology) == -1) {
          logSystemError(LOG_TAG, "ioctl[UI_SET_PHYS]");
        }
      }

      if (enableUInputEventType(ui->device, EV_SYN)) {
        return (*env)->NewDirectByteBuffer(env, ui, sizeof(*ui));
      }

      close(ui->device);
    } else {
      logSystemError(LOG_TAG, "open[uinput]");
    }

    free(ui);
  } else {
    logMallocError(LOG_TAG);
  }

  return NULL;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, createDevice, jboolean,
  jobject uinput
) {
  UINPUT_DESCRIPTOR;

  if (write(ui->device, &ui->properties, sizeof(ui->properties)) != -1) {
    if (ioctl(ui->device, UI_DEV_CREATE) != -1) {
      return JNI_TRUE;
    } else {
      logSystemError(LOG_TAG, "ioctl[UI_DEV_CREATE]");
    }
  } else {
    logSystemError(LOG_TAG, "write[uinput_user_dev]");
  }

  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_UInputDevice, closeDevice, void,
  jobject uinput
) {
  UINPUT_DESCRIPTOR;

  if (close(ui->device) == -1) logSystemError(LOG_TAG, "close[uinput]");
}

JAVA_METHOD(
  org_nbp_b2g_ui_KeyboardDevice, enableKeyEvents, jboolean,
  jobject uinput
) {
  UINPUT_DESCRIPTOR;

  return enableUInputKeyEvents(ui->device)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_KeyboardDevice, enableKey, jboolean,
  jobject uinput, jint key
) {
  UINPUT_DESCRIPTOR;

  return enableUInputKeyCode(ui->device, key)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_KeyboardDevice, pressKey, jboolean,
  jobject uinput, jint key
) {
  UINPUT_DESCRIPTOR;

  return writeKeyEvent(ui->device, key, 1)? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_KeyboardDevice, releaseKey, jboolean,
  jobject uinput, jint key
) {
  UINPUT_DESCRIPTOR;

  return writeKeyEvent(ui->device, key, 0)? JNI_TRUE: JNI_FALSE;
}

static int
writeTouchDown (int device) {
#if USE_MULTI_TOUCH_INTERFACE
  static uint16_t identifier = 0;
  if (!writeAbsEvent(device, ABS_MT_SLOT, 0)) return 0;
  if (!writeAbsEvent(device, ABS_MT_TRACKING_ID, identifier++)) return 0;
#else /* USE_MULTI_TOUCH_INTERFACE */
  if (!writeKeyEvent(device, UINPUT_TOUCH_KEY, 1)) return 0;
#endif /* USE_MULTI_TOUCH_INTERFACE */

  return 1;
}

static int
writeTouchUp (int device) {
#if USE_MULTI_TOUCH_INTERFACE
  if (!writeAbsEvent(device, ABS_MT_TRACKING_ID, -1)) return 0;
  return writeSynReport(device);
#else /* USE_MULTI_TOUCH_INTERFACE */
  return writeKeyEvent(device, UINPUT_TOUCH_KEY, 0);
#endif /* USE_MULTI_TOUCH_INTERFACE */
}

static int
writeTouchX (int device, InputEventValue x) {
#if USE_MULTI_TOUCH_INTERFACE
  return writeAbsEvent(device, ABS_MT_POSITION_X, x);
#else /* USE_MULTI_TOUCH_INTERFACE */
  return writeAbsEvent(device, ABS_X, x);
#endif /* USE_MULTI_TOUCH_INTERFACE */
}

static int
writeTouchY (int device, InputEventValue y) {
#if USE_MULTI_TOUCH_INTERFACE
  return writeAbsEvent(device, ABS_MT_POSITION_Y, y);
#else /* USE_MULTI_TOUCH_INTERFACE */
  return writeAbsEvent(device, ABS_Y, y);
#endif /* USE_MULTI_TOUCH_INTERFACE */
}

static int
writeTouchLocation (int device, InputEventValue x, InputEventValue y) {
  if (!writeTouchX(device, x)) return 0;
  if (!writeTouchY(device, y)) return 0;

#if USE_MULTI_TOUCH_INTERFACE
  return writeSynReport(device);
#else /* USE_MULTI_TOUCH_INTERFACE */
  return 1;
#endif /* USE_MULTI_TOUCH_INTERFACE */
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, enableTouchEvents, jboolean,
  jobject uinput, jint width, jint height
) {
  UINPUT_DESCRIPTOR;

  static const InputEventCode keyCodes[] = {
#if USE_MULTI_TOUCH_INTERFACE
#else /* USE_MULTI_TOUCH_INTERFACE */
    UINPUT_TOUCH_KEY,
#endif /* USE_MULTI_TOUCH_INTERFACE */
    KEY_CNT
  };

  static const InputEventCode absCodes[] = {
#if USE_MULTI_TOUCH_INTERFACE
    ABS_MT_SLOT,
    ABS_MT_TRACKING_ID,
    ABS_MT_POSITION_X,
    ABS_MT_POSITION_Y,
#else /* USE_MULTI_TOUCH_INTERFACE */
    ABS_X,
    ABS_Y,
#endif /* USE_MULTI_TOUCH_INTERFACE */
    ABS_CNT
  };

  if (!enableUInputKeyCodes(ui->device, keyCodes)) return JNI_FALSE;
  if (!enableUInputAbsCodes(ui->device, absCodes)) return JNI_FALSE;

#if USE_MULTI_TOUCH_INTERFACE
  ui->properties.absmin[ABS_MT_SLOT] = 0;
  ui->properties.absmax[ABS_MT_SLOT] = 9;

  ui->properties.absmin[ABS_MT_TRACKING_ID] = 0;
  ui->properties.absmax[ABS_MT_TRACKING_ID] = UINT16_MAX;

  ui->properties.absmin[ABS_MT_POSITION_X] = 0;
  ui->properties.absmax[ABS_MT_POSITION_X] = width - 1;

  ui->properties.absmin[ABS_MT_POSITION_Y] = 0;
  ui->properties.absmax[ABS_MT_POSITION_Y] = height - 1;
#else /* USE_MULTI_TOUCH_INTERFACE 1 */
  ui->properties.id.bustype = BUS_USB;

  ui->properties.absmin[ABS_X] = 0;
  ui->properties.absmax[ABS_X] = width - 1;

  ui->properties.absmin[ABS_Y] = 0;
  ui->properties.absmax[ABS_Y] = height - 1;
#endif /* USE_MULTI_TOUCH_INTERFACE 1 */

  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, touchBegin, jboolean,
  jobject uinput, int x, int y
) {
  UINPUT_DESCRIPTOR;

#if USE_MULTI_TOUCH_INTERFACE
  if (!writeTouchDown(ui->device)) return JNI_FALSE;
  if (!writeTouchLocation(ui->device, x, y)) return JNI_FALSE;
#else /* USE_MULTI_TOUCH_INTERFACE */
  if (!writeTouchLocation(ui->device, x, y)) return JNI_FALSE;
  if (!writeTouchDown(ui->device)) return JNI_FALSE;
#endif /* USE_MULTI_TOUCH_INTERFACE */

  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, touchEnd, jboolean,
  jobject uinput
) {
  UINPUT_DESCRIPTOR;

  if (!writeTouchUp(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, touchLocation, jboolean,
  jobject uinput, jint x, jint y
) {
  UINPUT_DESCRIPTOR;

  if (!writeTouchLocation(ui->device, x, y)) return JNI_FALSE;
  if (!writeSynReport(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}
