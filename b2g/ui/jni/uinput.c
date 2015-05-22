#include "utils.h"
MAKE_FILE_LOG_TAG;

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

#ifndef ABS_MT_PRESSURE
#define ABS_MT_PRESSURE 0X3A
#endif /* ABS_MT_PRESSURE */

#ifndef ABS_MT_TOUCH_MAJOR
#define ABS_MT_TOUCH_MAJOR 0X30
#endif /* ABS_MT_TOUCH_MAJOR */

#ifndef INPUT_PROP_POINTER
#define INPUT_PROP_POINTER 0X00
#endif /* INPUT_PROP_POINTER */

#ifndef INPUT_PROP_DIRECT
#define INPUT_PROP_DIRECT 0X01
#endif /* INPUT_PROP_DIRECT */

#include "linux/uinput.h"

typedef uint16_t InputEventType;
typedef uint16_t InputEventCode;
typedef int32_t InputEventValue;

static int
enableUInputEventType (int device, InputEventType type) {
  if (ioctl(device, UI_SET_EVBIT, type) != -1) return 1;
  logSystemError(LOG_TAG, "ioctl[UI_SET_EVBIT]");
  LOG(DEBUG, "failing event type: %d", type);
  return 0;
}

static int
enableUInputEventCodes (
  int device, const InputEventCode *codes, InputEventCode end,
  int (*enableType) (int device),
  int (*enableCode) (int device, InputEventCode code)
) {
  if (codes) {
    const InputEventCode *code = codes;

    while (*code != end) {
      if (code == codes) {
        if (!enableType(device)) {
          return 0;
        }
      }

      if (!enableCode(device, *code)) return 0;
      code += 1;
    }
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
writeKeyEvent (int device, InputEventCode key, int press) {
  return writeInputEvent(device, EV_KEY, key, (press? 1: 0));
}

static int
writeAbsEvent (int device, InputEventCode action, InputEventValue value) {
  return writeInputEvent(device, EV_ABS, action, value);
}

typedef struct {
  const char *path;
  int device;
  struct uinput_user_dev properties;

  struct {
    int (*writeDown) (int device, InputEventValue x, InputEventValue y);
    int (*writeUp) (int device);
    int (*writeLocation) (int device, InputEventValue x, InputEventValue y);
  } touch;
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

  if (!writeKeyEvent(ui->device, key, 1)) return JNI_FALSE;
  if (!writeSynReport(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_KeyboardDevice, releaseKey, jboolean,
  jobject uinput, jint key
) {
  UINPUT_DESCRIPTOR;

  if (!writeKeyEvent(ui->device, key, 0)) return JNI_FALSE;
  if (!writeSynReport(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}

static int
writeSingleTouchX (int device, InputEventValue x) {
  return writeAbsEvent(device, ABS_X, x);
}

static int
writeSingleTouchY (int device, InputEventValue y) {
  return writeAbsEvent(device, ABS_Y, y);
}

static int
writeSingleTouchLocation (int device, InputEventValue x, InputEventValue y) {
  if (!writeSingleTouchX(device, x)) return 0;
  if (!writeSingleTouchY(device, y)) return 0;
  return 1;
}

static int
writeSingleTouchDown (int device, InputEventValue x, InputEventValue y) {
  if (!writeSingleTouchLocation(device, x, y)) return 0;
  if (!writeKeyEvent(device, BTN_TOUCH, 1)) return 0;
  if (!writeKeyEvent(device, BTN_TOOL_FINGER, 1)) return 0;
  return writeSynReport(device);
}

static int
writeSingleTouchUp (int device) {
  if (!writeKeyEvent(device, BTN_TOOL_FINGER, 0)) return 0;
  if (!writeKeyEvent(device, BTN_TOUCH, 0)) return 0;
  return writeSynReport(device);
}

static int
writeMultiTouchX (int device, InputEventValue x) {
  return writeAbsEvent(device, ABS_MT_POSITION_X, x);
}

static int
writeMultiTouchY (int device, InputEventValue y) {
  return writeAbsEvent(device, ABS_MT_POSITION_Y, y);
}

static int
writeMultiTouchLocation (int device, InputEventValue x, InputEventValue y) {
  if (!writeMultiTouchX(device, x)) return 0;
  if (!writeMultiTouchY(device, y)) return 0;
  return writeSynReport(device);
}

static int
writeMultiTouchDown (int device, InputEventValue x, InputEventValue y) {
  static uint16_t identifier = 0;
  if (!writeAbsEvent(device, ABS_MT_SLOT, 0)) return 0;
  if (!writeAbsEvent(device, ABS_MT_TRACKING_ID, identifier++)) return 0;
  return writeMultiTouchLocation(device, x, y);
}

static int
writeMultiTouchUp (int device) {
  if (!writeAbsEvent(device, ABS_MT_TRACKING_ID, -1)) return 0;
  return writeSynReport(device);
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, touchEnable, jboolean,
  jobject uinput, jint width, jint height
) {
  UINPUT_DESCRIPTOR;

  const InputEventCode *keyCodes = NULL;
  const InputEventCode *absCodes = NULL;

  if (ioctl(ui->device, UI_SET_PROPBIT, INPUT_PROP_DIRECT) != -1) {
    LOG(DEBUG, "using multi-touch protocol");

    ui->touch.writeDown = writeMultiTouchDown;
    ui->touch.writeUp = writeMultiTouchUp;
    ui->touch.writeLocation = writeMultiTouchLocation;

    ui->properties.id.bustype = BUS_USB;
    ui->properties.id.version = 0X0200;

    ui->properties.absmin[ABS_MT_SLOT] = 0;
    ui->properties.absmax[ABS_MT_SLOT] = 9;

    ui->properties.absmin[ABS_MT_TRACKING_ID] = 0;
    ui->properties.absmax[ABS_MT_TRACKING_ID] = UINT16_MAX;

    ui->properties.absmin[ABS_MT_POSITION_X] = 0;
    ui->properties.absmax[ABS_MT_POSITION_X] = width - 1;

    ui->properties.absmin[ABS_MT_POSITION_Y] = 0;
    ui->properties.absmax[ABS_MT_POSITION_Y] = height - 1;

    {
      static const InputEventCode codes[] = {
        ABS_MT_SLOT,
        ABS_MT_TRACKING_ID,
        ABS_MT_POSITION_X,
        ABS_MT_POSITION_Y,
        ABS_CNT
      };

      absCodes = codes;
    }
  } else {
    logSystemError(LOG_TAG, "ioctl[UI_SET_PROPBIT,INPUT_PROP_DIRECT]");
    LOG(DEBUG, "using single-touch protocol");

    ui->touch.writeDown = writeSingleTouchDown;
    ui->touch.writeUp = writeSingleTouchUp;
    ui->touch.writeLocation = writeSingleTouchLocation;

    ui->properties.id.bustype = BUS_USB;
    ui->properties.id.version = 0X0100;

    ui->properties.absmin[ABS_X] = 0;
    ui->properties.absmax[ABS_X] = width - 1;

    ui->properties.absmin[ABS_Y] = 0;
    ui->properties.absmax[ABS_Y] = height - 1;

    {
      static const InputEventCode codes[] = {
        BTN_TOUCH,
        BTN_TOOL_FINGER,
        BTN_TOOL_DOUBLETAP,
        BTN_TOOL_TRIPLETAP,
        KEY_CNT
      };

      keyCodes = codes;
    }

    {
      static const InputEventCode codes[] = {
        ABS_X,
        ABS_Y,
        ABS_CNT
      };

      absCodes = codes;
    }
  }

  if (!enableUInputKeyCodes(ui->device, keyCodes)) return JNI_FALSE;
  if (!enableUInputAbsCodes(ui->device, absCodes)) return JNI_FALSE;

  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, gestureBegin, jboolean,
  jobject uinput, jint x, jint y
) {
  UINPUT_DESCRIPTOR;

  if (!ui->touch.writeDown(ui->device, x, y)) return JNI_FALSE;
  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, gestureEnd, jboolean,
  jobject uinput
) {
  UINPUT_DESCRIPTOR;

  if (!ui->touch.writeUp(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_TouchDevice, gestureLocation, jboolean,
  jobject uinput, jint x, jint y
) {
  UINPUT_DESCRIPTOR;

  if (!ui->touch.writeLocation(ui->device, x, y)) return JNI_FALSE;
  if (!writeSynReport(ui->device)) return JNI_FALSE;
  return JNI_TRUE;
}
