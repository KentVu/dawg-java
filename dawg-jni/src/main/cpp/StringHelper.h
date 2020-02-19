//
// Created by VuTran.Kien on 2020-02-19.
//
#include <jni.h>
#include <memory>
#include <functional>
#include <iostream>

using std::string;
using std::function;
using std::unique_ptr;
using std::shared_ptr;
using std::cout;
using std::endl;

#ifndef DAWG_JNI_STRINGHELPER_H
#define DAWG_JNI_STRINGHELPER_H

class jstring_deleter
{
    JNIEnv *m_env;
    jstring m_jstr;

public:

    jstring_deleter(JNIEnv *env, jstring jstr)
        : m_env(env)
        , m_jstr(jstr)
    {
    }

    void operator()(const char *cstr)
    {
        cout << "[DEBUG] Releasing " << cstr << endl;
        m_env->ReleaseStringUTFChars(m_jstr, cstr);
    }

};

shared_ptr<const char> ToStringPtr(JNIEnv *env, jstring jstr);
const string ToString(JNIEnv *env, jstring jstr);

#endif //DAWG_JNI_STRINGHELPER_H
