FROM alpine:3.14

RUN apk update && apk add bash

COPY ./entrypoint.sh /opt/

RUN chmod +x /opt/*.sh

# ENTRYPOINT ["tail", "-f", "/dev/null"]

ENTRYPOINT ["/opt/entrypoint.sh"]