#!/bin/bash

removeOutputFile=0
rFormat=0

while getopts ":dR" flags; do
  case $flags in
    d) removeOutputFile=1;;
    R) rFormat=1;;
  esac
done
shift $((OPTIND-1))

numIterations=$1
maxSize=$2
inputFile=$3
outputFile=$4

if [ $removeOutputFile != 0 ]; then
  rm $outputFile
fi

if [ $rFormat != 0 ]; then
  #This implicitly overwrites
  echo "NumConnections,Time" > $outputFile
fi

for (( i=1; i<=$maxSize; i++ )); do
  for (( j=0; j<$numIterations; j++)); do
    ./stress_test.sh -p $i $inputFile >> $outputFile
  done
done
