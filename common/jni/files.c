#include <sys/types.h>
#include <sys/xattr.h>

#include <string.h>
#include <errno.h>
#include "java.h"

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

static const FileOperationMethods *
getFileOperationMethods (jboolean follow) {
  if (follow == JNI_FALSE) return &fileOperationMethods;
  if (follow == JNI_TRUE) return &symlinkOperationMethods;
  return NULL;
}

static jclass
findClass (JNIEnv *env, jclass *class, const char *name) {
  if (!*class) *class = (*env)->FindClass(env, name);
  return *class;
}

static jclass
findStringClass (JNIEnv *env) {
  static jclass class = NULL;
  return findClass(env, &class, "java/lang/String");
}

static jobjectArray
getExtendedAttributeNames (JNIEnv *env, const FileOperationMethods *methods, const char *path, size_t size) {
  char buffer[size];
  ssize_t result = methods->getExtendedAttributeNames(path, buffer, size);
  if (result == -1) return NULL;

  jstring names[size];
  int count = 0;

  {
    const char *cName = buffer;
    const char *end = cName + result;

    while (cName < end) {
      jstring jName = (*env)->NewStringUTF(env, cName);
      if (!jName) return NULL;
      names[count++] = jName;
      cName += strlen(cName) + 1;
    }
  }

  jobjectArray array = (*env)->NewObjectArray(env, count, findStringClass(env), NULL);
  if (!array) return NULL;

  while (--count >= 0) {
    (*env)->SetObjectArrayElement(env, array, count, names[count]);
  }

  return array;
}

JAVA_INSTANCE_METHOD(
  org_nbp_common, getExtendedAttributeNames, jobjectArray,
  jstring jPath, jboolean follow
) {
  jobjectArray jNames = NULL;
  const FileOperationMethods *methods = getFileOperationMethods(follow);
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, NULL);

  if (cPath) {
    size_t size = 0X200;

    while (1) {
      jNames = getExtendedAttributeNames(env, methods, cPath, size);
      if (jNames) break;
      if (errno != ERANGE) break;

      ssize_t result = methods->getExtendedAttributeNames(cPath, NULL, 0);
      if (result == -1) break;
      size = result;
    }

    (*env)->ReleaseStringUTFChars(env, jPath, cPath);
  }

  return jNames;
}

static jstring
getExtendedAttributeValue (JNIEnv *env, const FileOperationMethods *methods, const char *path, const char *name, size_t size) {
  char value[size];
  ssize_t result = methods->getExtendedAttributeValue(path, name, value, size);
  if (result == -1) return NULL;
  return (*env)->NewStringUTF(env, value);
}

JAVA_INSTANCE_METHOD(
  org_nbp_common, getExtendedAttributeValue, jstring,
  jstring jPath, jstring jName, jboolean follow
) {
  jstring jValue = NULL;
  const FileOperationMethods *methods = getFileOperationMethods(follow);
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, NULL);

  if (cPath) {
    const char *cName = (*env)->GetStringUTFChars(env, jName, NULL);

    if (cName) {
      size_t size = 0X200;

      while (1) {
        jValue = getExtendedAttributeValue(env, methods, cPath, cName, size);
        if (jValue) break;
        if (errno != ERANGE) break;

        ssize_t result = methods->getExtendedAttributeValue(cPath, cName, NULL, 0);
        if (result == -1) break;
        size = result;
      }

      (*env)->ReleaseStringUTFChars(env, jName, cName);
    }

    (*env)->ReleaseStringUTFChars(env, jPath, cPath);
  }

  return jValue;
}

JAVA_INSTANCE_METHOD(
  org_nbp_common, setExtendedAttribute, void,
  jstring jPath, jstring jName, jstring jValue, jboolean follow
) {
  const FileOperationMethods *methods = getFileOperationMethods(follow);
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, NULL);

  if (cPath) {
    const char *cName = (*env)->GetStringUTFChars(env, jName, NULL);

    if (cName) {
      const char *cValue = (*env)->GetStringUTFChars(env, jValue, NULL);

      if (cValue) {
        methods->setExtendedAttribute(cPath, cName, cValue, strlen(cValue), 0);
        (*env)->ReleaseStringUTFChars(env, jValue, cValue);
      }

      (*env)->ReleaseStringUTFChars(env, jName, cName);
    }

    (*env)->ReleaseStringUTFChars(env, jPath, cPath);
  }
}

JAVA_INSTANCE_METHOD(
  org_nbp_common, removeExtendedAttribute, void,
  jstring jPath, jstring jName, jboolean follow
) {
  const FileOperationMethods *methods = getFileOperationMethods(follow);
  const char *cPath = (*env)->GetStringUTFChars(env, jPath, NULL);

  if (cPath) {
    const char *cName = (*env)->GetStringUTFChars(env, jName, NULL);

    if (cName) {
      methods->removeExtendedAttribute(cPath, cName);
      (*env)->ReleaseStringUTFChars(env, jName, cName);
    }

    (*env)->ReleaseStringUTFChars(env, jPath, cPath);
  }
}
