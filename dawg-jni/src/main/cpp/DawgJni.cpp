//
// Created by VuTran.Kien on 2020-02-19.
//

#include <fstream>
//#include <iostream>

#include "DawgJni.h"
#include "StringHelper.h"

#include "dawgdic/dawg-builder.h"
#include "dawgdic/dictionary-builder.h"

using std::ios;

// JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
// {
//   	JNIEnv *env;

//     if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
//         return JNI_ERR;
//     }
//     // Find your class. JNI_OnLoad is called from the correct class loader context for this to work.
//     jclass c = env->FindClass("kentvu/dawgjava/DawgTrie");
//     if (c == nullptr) return JNI_ERR;

//     // Register your class' native methods.
//     static const JNINativeMethod methods[] = {
//         {"dawgBuilder", "(Ljava/lang/String;Lkentvu/dawgjava/WordSequence;)V", reinterpret_cast<void*>(Java_kentvu_dawgjava_DawgTrie_dawgBuilder)}
//     };
//     int rc = env->RegisterNatives(c, methods, sizeof(methods)/sizeof(JNINativeMethod));
//     if (rc != JNI_OK) return rc;

//     return JNI_VERSION_1_6;
// }

string filename;

/*
 * Class:     HelloJNI
 * Method:    sayHello
 * Signature: (Ljava/lang/String;)V
 */
jlong Java_kentvu_dawgjava_DawgTrie_dawgBuilder
  (JNIEnv *env, jobject thisObj, jstring filenamej)
{
    filename = ToString(env, filenamej);
    dawgdic::DawgBuilder dawg_builder;
    return reinterpret_cast<jlong>(&dawg_builder);
}

void Java_kentvu_dawgjava_DawgTrie_dawgBuilderInsert
  (JNIEnv *env, jobject thisObj, jlong builderPtr, jstring wordj)
{
    dawgdic::DawgBuilder *dawg_builder = (dawgdic::DawgBuilder*)builderPtr;
    dawg_builder->Insert(env->GetStringUTFChars(wordj, JNI_FALSE));
}

void Java_kentvu_dawgjava_DawgTrie_dawgBuilderFinish
  (JNIEnv *env, jobject thisObj, jlong builderPtr)
{
    dawgdic::DawgBuilder *dawg_builder = (dawgdic::DawgBuilder*)builderPtr;
    // Finishes building a simple dawg.
    dawgdic::Dawg dawg;
    dawg_builder->Finish(&dawg);

    // Builds a dictionary from a simple dawg.
    dawgdic::Dictionary dic;
    dawgdic::DictionaryBuilder::Build(dawg, &dic);

    // Writes a dictionary into a file "test.dic".
    std::ofstream dic_file(filename, ios::binary);
    dic.Write(&dic_file);
}
