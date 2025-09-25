#!/bin/sh
# $1: input file, $2: output file

if [ "$#" -ne 2 ]; then
  echo "Usage: ./head10.sh input.txt output.txt"
  exit 1
fi

head -n 10 "$1" > "$2"
