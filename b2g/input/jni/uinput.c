#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/stat.h>

#include <linux/input.h>
#define ABS_CNT			(ABS_MAX+1)
#include "linux/uinput.h"

#include <android/log.h>

static int
enableUInputEventType (int device, int type) {
  if (ioctl(device, UI_SET_EVBIT, type) != -1) return 1;
  return 0;
}

static int
enableUInputKey (int device, int key) {
  if (ioctl(device, UI_SET_KEYBIT, key) != -1) return 1;
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

JNIEXPORT jint JNICALL
Java_org_nbp_b2g_input_UInputDevice_openDevice (
  JNIEnv *env, jobject this
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
      ioctl(device, UI_SET_PHYS, topology);
    }

    if (write(device, &description, sizeof(description)) != -1) {
      enableUInputEventType(device, EV_KEY);
      enableUInputKey(device, KEY_POWER);

      if (ioctl(device, UI_DEV_CREATE) != -1) {
        return device;
      } else {
      }
    } else {
    }

    close(device);
  } else{
  }

  return device;
}

JNIEXPORT void JNICALL
Java_org_nbp_b2g_input_UInputDevice_closeDevice (
  JNIEnv *env, jobject this,
  jint device
) {
  close(device);
}

JNIEXPORT jboolean JNICALL
Java_org_nbp_b2g_input_UInputDevice_pressKey (
  JNIEnv *env, jobject this,
  jint device
) {
  return writeKeyEvent(device, KEY_POWER, 1)? JNI_TRUE: JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_org_nbp_b2g_input_UInputDevice_releaseKey (
  JNIEnv *env, jobject this,
  jint device
) {
  return writeKeyEvent(device, KEY_POWER, 0)? JNI_TRUE: JNI_FALSE;
}
