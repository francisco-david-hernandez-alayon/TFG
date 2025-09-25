FROM alpine:latest

WORKDIR /app

COPY word_count.sh .

RUN chmod +x word_count.sh

ENTRYPOINT ["./word_count.sh"]
