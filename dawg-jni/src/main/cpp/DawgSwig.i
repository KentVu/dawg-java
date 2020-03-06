/* File: dawg.i */
%module DawgSwigMdl
%include <std_string.i>
%inline %{
//#include "DawgJni.h"
#include <fstream>
#include <map>
#include <string>
#include "dawgdic/dawg-builder.h"
#include "dawgdic/dictionary-builder.h"
#include "dawgdic/guide-builder.h"
#include "dawgdic/ranked-completer.h"
#include "dawgdic/ranked-guide-builder.h"

class DawgSwig
{
    std::string filename;
    dawgdic::DawgBuilder dawg_builder;
    dawgdic::Dictionary dic;
    dawgdic::RankedGuide guide;

public:
    DawgSwig(std::string filename): filename(filename) {
        //string tmp
        //DawgSwig::filename(filename);
        printf("new DawgSwig for %s\n", filename.c_str());

    }
    void Insert(char* word) {
        //printf("Insert %s\n", word);
        // Inserts keys into a simple dawg.
        dawg_builder.Insert(word);
    }
    void Finish() {
        printf("Finish()\n");
        // Finishes building a simple dawg.
        dawgdic::Dawg dawg;
        dawg_builder.Finish(&dawg);

        // Builds a dictionary from a simple dawg.
        dawgdic::DictionaryBuilder::Build(dawg, &dic);

        // Writes a dictionary into a file "test.dic".
        std::ofstream dic_stream(filename, std::ios::binary);
        dic.Write(&dic_stream);
        dawgdic::RankedGuideBuilder::Build(dawg, dic, &guide);
        guide.Write(&dic_stream);
    }
    bool Contains(std::string key) {
        printf("Contains() %s\n", key.c_str());
        return dic.Contains(key.c_str());
    }
    std::map<std::string, int> Search(std::string prefix) {
        printf("Search() for %s\n", prefix.c_str());
        std::map<std::string, int> result;
        dawgdic::RankedCompleter completer(dic, guide);
        dawgdic::BaseType index = dic.root();
        if (dic.Follow(prefix.c_str(), prefix.length(), &index)) {
            completer.Start(index);
            while (completer.Next()) {
                //result.insert(std::make_pair(completer.key(), completer.value()));
                result[prefix + completer.key()] = completer.value();
                std::cout << ' ' << prefix << completer.key()
                        << " = " << completer.value() << std::endl;
            }
        }
        return result;
    }
};
%}
%include "std_map.i"
%template(String_Int_Map) std::map<std::string, int>;
//%immutable;
//dawgdic::DawgBuilder newDawgBuilder();
