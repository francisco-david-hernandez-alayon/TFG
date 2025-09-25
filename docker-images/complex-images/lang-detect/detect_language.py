import sys
from langdetect import detect, LangDetectException

if len(sys.argv) != 3:
    print("Usage: python detect_language.py input.txt output.txt")
    sys.exit(1)

input_file = sys.argv[1]
output_file = sys.argv[2]

with open(input_file, "r", encoding="utf-8") as f:
    text = f.read()

try:
    lang = detect(text)
except LangDetectException:
    lang = "unknown"

with open(output_file, "w", encoding="utf-8") as f:
    f.write(f"language={lang}\n")
    f.write(text)
