import sys
from transformers import pipeline

if len(sys.argv) != 3:
    print("Usage: python summarize.py input.txt output.txt")
    sys.exit(1)

input_file = sys.argv[1]
output_file = sys.argv[2]

with open(input_file, "r", encoding="utf-8") as f:
    lines = f.readlines()

# Eliminar primera linea(contiene el idioma)
text_to_summarize = "".join(lines[1:])

summarizer = pipeline("summarization", model="sshleifer/distilbart-cnn-12-6")
summary = summarizer(text_to_summarize, max_length=130, min_length=30, do_sample=False)[0]['summary_text']

with open(output_file, "w", encoding="utf-8") as f:
    f.write(summary)
