#!/bin/bash
cpp_source_root=dawg-jni/src/main/cpp
infile=$cpp_source_root/DawgSwig.i
out_cpp_file=$cpp_source_root/DawgSWIG.cpp
outdir=dawg-java/src/main/java
package=dawgswig

if [[ "$1" = "cleanup" ]]; then
    echo 'cleanup!'
    rm -v $outdir/$package/*.java
    rm -v $out_cpp_file
    exit
elif [[ "$1" = "prepare-artifacts" ]]; then
    swig_generated=${2:-swig-generated}
    echo "prepare-artifacts!: $swig_generated"
    mkdir -pv $swig_generated/{java,cpp}
    cp -rv $outdir/$package $swig_generated/java/
    cp -v $out_cpp_file $swig_generated/cpp/
    exit
fi

if ! which swig ; then
	echo '[ERROR] Need SWIG!'
	exit 1
fi
swig -version

mkdir -pv $outdir/$package
swig -v -c++ -java -o $out_cpp_file -outdir $outdir/$package -package $package $infile

