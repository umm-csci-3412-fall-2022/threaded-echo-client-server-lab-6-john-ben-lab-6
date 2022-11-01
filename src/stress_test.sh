#!/bin/bash

    porcelain=1

    #Handle possible flag
    while getopts ":p" flags; do
      case $flags in
        p) porcelain=0;;
        esac
    done
    #Throw away flags in argument list
    shift $((OPTIND-1))

    numCalls="$1"
    inputFile="$2"

    for (( i=0; i<$numCalls; i++ ))
    do
        if [ "$porcelain" != 0 ]; then
          echo "Doing run $i"
        fi
        java echoserver.EchoClient < $inputFile > /dev/null &
    done

    if [ "$porcelain" != 0 ]; then
      echo "Now waiting for all the processes to terminate"
    fi

    # `date` will output the date *and time* so you can see how long
    # you had to wait for all the processes to finish.
    startTime=$(date +%s%3N)
    wait

    if [ "$porcelain" != 0 ]; then
      echo "Done waiting; all processes are finished"
    fi

    endTime=$(date +%s%3N)
    runTime=$((endTime-startTime))

    if [ "$porcelain" != 0 ]; then
      echo "Ran in $runTime ms."
    else
      echo "$numCalls,$runTime"
    fi
