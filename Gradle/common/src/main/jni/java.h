#include <jni.h>

#define JAVA_METHOD(object, name, type, ...) \
  JNIEXPORT type JNICALL Java_ ## object ## _ ## name \
  (JNIEnv *env, ## __VA_ARGS__)

#define JAVA_INSTANCE_METHOD(object, name, type, ...) \
  JAVA_METHOD(object, name, type, jobject this, ## __VA_ARGS__)

#define JAVA_STATIC_METHOD(object, name, type, ...) \
  JAVA_METHOD(object, name, type, jclass class, ## __VA_ARGS__)

extern jobject toGlobalReference (JNIEnv *env, jobject local);

extern jclass findClass (JNIEnv *env, jclass *class, const char *name);
extern jclass findStringClass (JNIEnv *env);

extern void throwErrnoException (JNIEnv *env, int error, const char *function, ...);
