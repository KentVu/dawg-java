#include "StringHelper.h"

const string ToString(JNIEnv *env, jstring jstr)
{
    jstring_deleter deleter(env, jstr);     // using a function object
    unique_ptr<const char, jstring_deleter> pcstr(
            env->GetStringUTFChars(jstr, JNI_FALSE),
            deleter );

    return string( pcstr.get() );
}


shared_ptr<const char> ToStringPtr(JNIEnv *env, jstring jstr)
{
    function<void(const char*)> deleter =   // using a lambda
        [env, jstr](const char *cstr) -> void
        {
            cout << "[DEBUG] Releasing " << cstr << endl;
            env->ReleaseStringUTFChars(jstr, cstr);
        };

    return shared_ptr<const char>(
            env->GetStringUTFChars(jstr, JNI_FALSE),
            deleter );
}
