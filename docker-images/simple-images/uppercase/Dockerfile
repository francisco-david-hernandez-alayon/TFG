FROM alpine:latest

WORKDIR /app

COPY to_uppercase.sh .

RUN chmod +x to_uppercase.sh

ENTRYPOINT ["./to_uppercase.sh"]
