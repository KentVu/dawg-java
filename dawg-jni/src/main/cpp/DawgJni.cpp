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

/*
 * Class:     HelloJNI
 * Method:    sayHello
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_kentvu_dawgjava_JniDawg_saveDawg
  (JNIEnv *env, jobject thisObj, jstring arg)
{
    string filename = ToString(env, arg);
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

