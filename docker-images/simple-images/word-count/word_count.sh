#!/bin/sh
# $1: input file, $2: output file

if [ "$#" -ne 2 ]; then
  echo "Usage: ./word_count.sh input.txt output.txt"
  exit 1
fi

count=$(wc -w < "$1")
echo "words count = $count" > "$2"
