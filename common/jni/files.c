#include <sys/types.h>
#include <sys/xattr.h>

typedef struct {
  ssize_t (*getExtendedAttributeNames) (const char *path, char *names, size_t size);
  ssize_t (*getExtendedAttributeValue) (const char *path, const char *name, void *value, size_t size);
  int (*setExtendedAttribute) (const char *path, const char *name, const void *value, size_t size, int flags);
  int (*removeExtendedAttribute) (const char *path, const char *name);
} FileOperationMethods;

static const FileOperationMethods fileOperationMethods = {
  .getExtendedAttributeNames = listxattr,
  .getExtendedAttributeValue = getxattr,
  .setExtendedAttribute = setxattr,
  .removeExtendedAttribute = removexattr
};

static const FileOperationMethods symlinkOperationMethods = {
  .getExtendedAttributeNames = llistxattr,
  .getExtendedAttributeValue = lgetxattr,
  .setExtendedAttribute = lsetxattr,
  .removeExtendedAttribute = lremovexattr
};
