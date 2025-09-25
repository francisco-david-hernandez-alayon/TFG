FROM alpine:latest

WORKDIR /app

COPY to_lowercase.sh .

RUN chmod +x to_lowercase.sh

ENTRYPOINT ["./to_lowercase.sh"]
