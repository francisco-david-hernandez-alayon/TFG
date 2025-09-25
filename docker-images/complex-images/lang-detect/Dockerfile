FROM python:3.11-alpine

WORKDIR /app

COPY detect_language.py .
RUN pip install langdetect

ENTRYPOINT ["python", "detect_language.py"]
