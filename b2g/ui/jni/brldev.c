#include "utils.h"
MAKE_FILE_LOG_TAG;

#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include "metec_flat20_ioctl.h"

#define BRAILLE_DEVICE_PATH "/dev/braille0"
#define BRAILLE_CELL_COUNT 20
static int brailleDevice = -1;

static int
isOpen (void) {
  if (brailleDevice != -1) return 1;
  LOG(WARN, "braille device not open");
  return 0;
}

static int
openDevice (void) {
  if (brailleDevice == -1) {
    if ((brailleDevice = open(BRAILLE_DEVICE_PATH, O_WRONLY)) == -1) {
      logSystemError(LOG_TAG, "open[braille]");
      return 0;
    }

    LOG(DEBUG, "braille device opened: %s fd=%d", BRAILLE_DEVICE_PATH, brailleDevice);
  }

  return 1;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, openDevice, jboolean
) {
  return openDevice()? JNI_TRUE: JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, closeDevice, void
) {
  if (isOpen()) {
    close(brailleDevice);
    brailleDevice = -1;
  }
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, enableDevice, jboolean
) {
  if (isOpen()) {
    if (ioctl(brailleDevice, METEC_FLAT20_DISPLAY_CONTROL, DISPLAY_ENABLE) != -1) {
      return JNI_TRUE;
    } else {
      logSystemError(LOG_TAG, "METEC_FLAT20_DISPLAY_CONTROL[DISPLAY_ENABLE]");
    }
  }

  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, disableDevice, jboolean
) {
  if (isOpen()) {
    if (ioctl(brailleDevice, METEC_FLAT20_DISPLAY_CONTROL, DISPLAY_DISABLE) != -1) {
      return JNI_TRUE;
    } else {
      logSystemError(LOG_TAG, "METEC_FLAT20_DISPLAY_CONTROL[DISPLAY_DISABLE]");
    }
  }

  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, getDriverVersion, jstring
) {
  if (isOpen()) {
    char buffer[10];
    memset(buffer, 0, sizeof(buffer));

    if (ioctl(brailleDevice, METEC_FLAT20_GET_DRIVER_VERSION, buffer) != -1) {
      jstring string = (*env)->NewStringUTF(env, buffer);

      if (!checkException(env)) {
        if (string) {
          return string;
        } else {
          logMallocError(LOG_TAG);
        }
      }
    } else {
      logSystemError(LOG_TAG, "ioctl[METEC_FLAT20_GET_DRIVER_VERSION]");
    }
  }

  return NULL;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, getCellCount, jint
) {
  if (isOpen()) {
    return BRAILLE_CELL_COUNT;
  }

  return 0;
}

static const unsigned char firmnessSettings[] = {
  UOUT_155V_CONFIG_VALUE,
  UOUT_162V_CONFIG_VALUE,
  UOUT_168V_CONFIG_VALUE,
  UOUT_174V_CONFIG_VALUE,
  UOUT_177V_CONFIG_VALUE,
  UOUT_184V_CONFIG_VALUE,
  UOUT_191V_CONFIG_VALUE,
  UOUT_199V_CONFIG_VALUE
};

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, setCellFirmness, jboolean,
  jint firmness
) {
  LOG(DEBUG, "setting firmness: %d", firmness);

  if ((firmness >= 0) && (firmness < ARRAY_COUNT(firmnessSettings))) {
    if (isOpen()) {
      if (ioctl(brailleDevice, METEC_FLAT20_SET_DOT_STRENGTH, firmnessSettings[firmness]) != -1) {
        return JNI_TRUE;
      } else {
        logSystemError(LOG_TAG, "ioctl[METEC_FLAT20_SET_DOT_STRENGTH]");
      }
    }
  } else {
    LOG(WARN, "invalid firmness: %d", firmness);
  }

  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, clearCells, jboolean
) {
  if (isOpen()) {
    if (ioctl(brailleDevice, METEC_FLAT20_CLEAR_DISPLAY, 0) != -1) {
      return JNI_TRUE;
    } else {
      logSystemError(LOG_TAG, "ioctl[METEC_FLAT20_CLEAR_DISPLAY]");
    }
  }

  return JNI_FALSE;
}

JAVA_METHOD(
  org_nbp_b2g_ui_BrailleDevice, writeCells, jboolean,
  jbyteArray jCells
) {
  if (isOpen()) {
    jboolean isCopy;
    jbyte *cCells = (*env)->GetByteArrayElements(env, jCells, &isCopy);

    if (!checkException(env)) {
      unsigned char cells[BRAILLE_CELL_COUNT];

      {
        unsigned int index = 0;
        jsize length = (*env)->GetArrayLength(env, jCells);
        jsize count = length;

        if (BRAILLE_CELL_COUNT < count) count = BRAILLE_CELL_COUNT;

        while (index < count) {
          cells[index] = cCells[index];
          index += 1;
        }

        while (index < BRAILLE_CELL_COUNT) cells[index++] = 0;
        (*env)->ReleaseByteArrayElements(env, jCells, cCells, JNI_ABORT);
      }

      {
        ssize_t result = write(brailleDevice, cells, sizeof(cells));

        if (result != -1) {
          return JNI_TRUE;
        } else {
          logSystemError(LOG_TAG, "write[braille]");
        }
      }
    }
  }

  return JNI_FALSE;
}
