#!/bin/bash
if ! which swig ; then
	echo '[ERROR] Need SWIG!'
	exit 1
fi
infile=dawg-jni/src/main/cpp/DawgSwig.i
out_cpp_file=dawg-jni/src/main/cpp/DawgSWIG.cpp
outdir=dawg-java/src/main/java
package=dawgswig

if [[ "$1" = "cleanup" ]]; then
    echo 'cleanup!'
    #rm -v $outdir/$package/{DawgSwig.java,DawgSwigJNI.java,SWIGTYPE_p_dawgdic__DawgBuilder.java}
    #rm -v $outdir/$package/SWIGTYPE_p_*.java
    rm -v $outdir/$package/*.java
    rm -v $out_cpp_file
    exit
fi

mkdir -pv $outdir/$package
swig -v -c++ -java -o $out_cpp_file -outdir $outdir/$package -package $package $infile

