#include "utils.h"
MAKE_FILE_LOG_TAG;

#include <errno.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/ioctl.h>

#include "cp430_ioctl.h"

#define DEVICE_PATH "/dev/cp430_core"

static char firmwareVersion[] = {0, 0};

static void
getFirmwareVersion (void) {
  if (!firmwareVersion[0]) {
    int device;
    firmwareVersion[0] = 1;

    if ((device = open(DEVICE_PATH, O_RDONLY)) != -1) {
      if (ioctl(device, CP430_CORE_GET_STATUS, firmwareVersion) == -1) {
        logSystemError(LOG_TAG, "ioctl[CP430_CORE_GET_STATUS]");
      }

      close(device);
    } else {
      LOG(WARN, "open error: %s: %s", DEVICE_PATH, strerror(errno));
    }
  }
}

JAVA_METHOD(
  org_nbp_b2g_ui_FirmwareVersion, getMajor, jbyte
) {
  getFirmwareVersion();
  return firmwareVersion[0];
}

JAVA_METHOD(
  org_nbp_b2g_ui_FirmwareVersion, getMinor, jbyte
) {
  getFirmwareVersion();
  return firmwareVersion[1];
}
