
#ifndef __CP430_CORE_IOCTL_H__
#define __CP430_CORE_IOCTL_H__

#define CP430_CORE_IOC_MAGIC				0xe1

#define PACKET_LOGGING_NONE					0
#define PACKET_LOGGING_PARTIAL				1
#define PACKET_LOGGING_FULL					2

#define CP430_CORE_IOC_MAXNR  				0x12

/* cp430_core ioctls */
#define CP430_CORE_GET_DRIVER_VERSION		_IOC(_IOC_READ,  CP430_CORE_IOC_MAGIC, 0x11, 4)
#define CP430_CORE_GET_STATUS				_IOC(_IOC_READ,  CP430_CORE_IOC_MAGIC, 0x12, 4)























#endif
