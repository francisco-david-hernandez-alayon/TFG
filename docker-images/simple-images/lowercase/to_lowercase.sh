#!/bin/sh
# $1: input file, $2: output file

if [ "$#" -ne 2 ]; then
  echo "Usage: ./to_lowercase.sh input.txt output.txt"
  exit 1
fi

cat "$1" | tr '[:upper:]' '[:lower:]' > "$2"
