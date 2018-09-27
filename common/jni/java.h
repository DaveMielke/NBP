#include <jni.h>

#define JAVA_METHOD(object, name, type, ...) \
  JNIEXPORT type JNICALL Java_ ## object ## _ ## name \
  (JNIEnv *env, ## __VA_ARGS__)

#define JAVA_INSTANCE_METHOD(object, name, type, ...) \
  JAVA_METHOD(object, name, type, jobject this, ## __VA_ARGS__)

#define JAVA_STATIC_METHOD(object, name, type, ...) \
  JAVA_METHOD(object, name, type, jclass class, ## __VA_ARGS__)

extern jobject javaGlobalReference (JNIEnv *env, jobject local);

extern jclass javaFindClass (JNIEnv *env, jclass *class, const char *name);
extern jclass javaFindStringClass (JNIEnv *env);
