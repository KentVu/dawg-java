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

JavaVM *gJvm;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
  	JNIEnv *env;

    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    // Find your class. JNI_OnLoad is called from the correct class loader context for this to work.
    jclass c = env->FindClass("kentvu/dawgjava/DawgTrie");
    if (c == nullptr) return JNI_ERR;

    // Register your class' native methods.
    static const JNINativeMethod methods[] = {
        {"buildDawg", "(Ljava/lang/String;Lkentvu/dawgjava/WordSequence;)V", reinterpret_cast<void*>(Java_kentvu_dawgjava_DawgTrie_buildDawg)}
    };
    int rc = env->RegisterNatives(c, methods, sizeof(methods)/sizeof(JNINativeMethod));
    if (rc != JNI_OK) return rc;

    return JNI_VERSION_1_6;
}

/*
 * Class:     HelloJNI
 * Method:    sayHello
 * Signature: (Ljava/lang/String;)V
 */
void Java_kentvu_dawgjava_DawgTrie_buildDawg
  (JNIEnv *env, jobject thisObj, jstring filenamej, jobject seed)
{
    string filename = ToString(env, filenamej);
    cout << "Reading " << filename << endl;
    std::ifstream infile(filename);
    dawgdic::DawgBuilder dawg_builder;
    std::string line;
    while (std::getline(infile, line))
    {
        // Inserts keys into a simple dawg.
        dawg_builder.Insert(line.c_str());
    }

    // Finishes building a simple dawg.
    dawgdic::Dawg dawg;
    dawg_builder.Finish(&dawg);

    // Builds a dictionary from a simple dawg.
    dawgdic::Dictionary dic;
    dawgdic::DictionaryBuilder::Build(dawg, &dic);

    // Writes a dictionary into a file "test.dic".
    std::ofstream dic_file("test.dic", ios::binary);
    dic.Write(&dic_file);
}

