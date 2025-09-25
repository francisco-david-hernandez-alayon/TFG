FROM python:3.11-slim

WORKDIR /app

COPY summarize.py .

RUN pip install torch transformers --quiet

ENTRYPOINT ["python", "summarize.py"]
