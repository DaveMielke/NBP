#include "utils.h"
MAKE_FILE_LOG_TAG;

#include <linux/input.h>

#ifndef KEY_BRL_DOT9
#define KEY_BRL_DOT9 0X1F9
#endif /* KEY_BRL_DOT9 */

typedef struct {
  const char *name;
  int value;
} ScanCodeEntry;

#define SCAN_CODE(NAME,CODE) { .name=NAME, .value=(CODE) }
#define LINUX_KEY(NAME) SCAN_CODE(#NAME, KEY_##NAME)

static const ScanCodeEntry scanCodeTable[] = {
  LINUX_KEY(POWER),
  LINUX_KEY(VOLUMEDOWN),
  LINUX_KEY(VOLUMEUP),

  LINUX_KEY(BRL_DOT1),
  LINUX_KEY(BRL_DOT2),
  LINUX_KEY(BRL_DOT3),
  LINUX_KEY(BRL_DOT4),
  LINUX_KEY(BRL_DOT5),
  LINUX_KEY(BRL_DOT6),
  LINUX_KEY(BRL_DOT7),
  LINUX_KEY(BRL_DOT8),
  LINUX_KEY(BRL_DOT9),

  LINUX_KEY(NEXT),
  LINUX_KEY(PREVIOUS),

  LINUX_KEY(OK),
  LINUX_KEY(UP),
  LINUX_KEY(DOWN),
  LINUX_KEY(LEFT),
  LINUX_KEY(RIGHT),

  SCAN_CODE("B2G_WAKEUP",  0X1FB),
  SCAN_CODE("B2G_CURSOR_0",  0X2D0),
  SCAN_CODE("B2G_CURSOR_1",  0X2D1),
  SCAN_CODE("B2G_CURSOR_2",  0X2D2),
  SCAN_CODE("B2G_CURSOR_3",  0X2D3),
  SCAN_CODE("B2G_CURSOR_4",  0X2D4),
  SCAN_CODE("B2G_CURSOR_5",  0X2D5),
  SCAN_CODE("B2G_CURSOR_6",  0X2D6),
  SCAN_CODE("B2G_CURSOR_7",  0X2D7),
  SCAN_CODE("B2G_CURSOR_8",  0X2D8),
  SCAN_CODE("B2G_CURSOR_9",  0X2D9),
  SCAN_CODE("B2G_CURSOR_10", 0X2DA),
  SCAN_CODE("B2G_CURSOR_11", 0X2DB),
  SCAN_CODE("B2G_CURSOR_12", 0X2DC),
  SCAN_CODE("B2G_CURSOR_13", 0X2DD),
  SCAN_CODE("B2G_CURSOR_14", 0X2DE),
  SCAN_CODE("B2G_CURSOR_15", 0X2DF),
  SCAN_CODE("B2G_CURSOR_16", 0X2E0),
  SCAN_CODE("B2G_CURSOR_17", 0X2E1),
  SCAN_CODE("B2G_CURSOR_18", 0X2E2),
  SCAN_CODE("B2G_CURSOR_19", 0X2E3),
  SCAN_CODE("B2G_CURSOR_20", 0X2E4),
  SCAN_CODE("B2G_CURSOR_21", 0X2E5),
  SCAN_CODE("B2G_CURSOR_22", 0X2E6),
  SCAN_CODE("B2G_CURSOR_23", 0X2E7),
  SCAN_CODE("B2G_CURSOR_24", 0X2E8),
  SCAN_CODE("B2G_CURSOR_25", 0X2E9),
  SCAN_CODE("B2G_CURSOR_26", 0X2EA),
  SCAN_CODE("B2G_CURSOR_27", 0X2EB),
  SCAN_CODE("B2G_CURSOR_28", 0X2EC),
  SCAN_CODE("B2G_CURSOR_29", 0X2ED),
  SCAN_CODE("B2G_CURSOR_30", 0X2EE),
  SCAN_CODE("B2G_CURSOR_31", 0X2EF),
  SCAN_CODE("B2G_CURSOR_32", 0X2F0),
  SCAN_CODE("B2G_CURSOR_33", 0X2F1),
  SCAN_CODE("B2G_CURSOR_34", 0X2F2),
  SCAN_CODE("B2G_CURSOR_35", 0X2F3),
  SCAN_CODE("B2G_CURSOR_36", 0X2F4),
  SCAN_CODE("B2G_CURSOR_37", 0X2F5),
  SCAN_CODE("B2G_CURSOR_38", 0X2F6),
  SCAN_CODE("B2G_CURSOR_39", 0X2F7),

  LINUX_KEY(ENTER),
  LINUX_KEY(TAB),
  LINUX_KEY(BACKSPACE),

  LINUX_KEY(PAGEUP),
  LINUX_KEY(PAGEDOWN),
  LINUX_KEY(HOME),
  LINUX_KEY(END),
  LINUX_KEY(INSERT),
  LINUX_KEY(DELETE),

  LINUX_KEY(LEFTSHIFT),
  LINUX_KEY(RIGHTSHIFT),
  LINUX_KEY(LEFTCTRL),
  LINUX_KEY(RIGHTCTRL),
  LINUX_KEY(LEFTALT),
  LINUX_KEY(RIGHTALT),
  LINUX_KEY(LEFTMETA),
  LINUX_KEY(RIGHTMETA),
  LINUX_KEY(COMPOSE),

  LINUX_KEY(MENU),
  LINUX_KEY(SEARCH),

  LINUX_KEY(RESERVED)
};

JAVA_METHOD(
  org_nbp_b2g_ui_Keyboard, defineScanCodes, void
) {
  jclass class = this;
  const char *methodName = "defineScanCode";
  const char *methodSignature = "(Ljava/lang/String;I)V";
  jmethodID method = (*env)->GetStaticMethodID(env, class, methodName, methodSignature);

  if (method) {
    const ScanCodeEntry *sc = scanCodeTable;

    do {
      jstring name = (*env)->NewStringUTF(env, sc->name);
      if (!name) break;

      (*env)->CallStaticVoidMethod(env, class, method, name, sc->value);
      (*env)->DeleteLocalRef(env, name);
      if (checkException(env)) break;

      sc += 1;
    } while (sc->value != KEY_RESERVED);
  } else {
    LOG(ERROR, "method not found: %s %s", methodName, methodSignature);
  }

  checkException(env);
}
