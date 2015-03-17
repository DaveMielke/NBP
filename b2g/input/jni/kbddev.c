#include "utils.h"

#include <linux/input.h>

MAKE_FILE_LOG_TAG;

typedef struct {
  const char *name;
  int value;
} ScanCodeEntry;

#define SCAN_CODE(NAME) { .name=#NAME, .value=KEY_##NAME }

static const ScanCodeEntry scanCodeTable[] = {
  SCAN_CODE(UP),
  SCAN_CODE(DOWN),
  SCAN_CODE(LEFT),
  SCAN_CODE(RIGHT),

  SCAN_CODE(POWER),

  SCAN_CODE(RESERVED)
};

JNIEXPORT void JNICALL
Java_org_nbp_b2g_input_KeyboardDevice_defineScanCodes (
  JNIEnv *env, jclass class
) {
  const char *methodName = "defineScanCode";
  const char *methodSignature = "(Ljava/lang/String;I)V";
  jmethodID method = (*env)->GetStaticMethodID(env, class, methodName, methodSignature);

  if (method) {
    const ScanCodeEntry *sc = scanCodeTable;

    while (sc->value != KEY_RESERVED) {
      jstring name = (*env)->NewStringUTF(env, sc->name);
      if (!name) break;

      (*env)->CallStaticVoidMethod(env, class, method, name, sc->value);
      (*env)->DeleteLocalRef(env, name);
      if (checkException(env)) break;

      sc += 1;
    }
  } else {
    LOG(ERROR, "method not found: %s %s", methodName, methodSignature);
  }

  checkException(env);
}
