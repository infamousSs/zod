FROM zod-base-platform-image

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-eureka-server-image"
LABEL DATE="FEB 10 2021"

ENV APP_FULL_NAME="Zod Eureka Server"
ENV APP_NAME=zod-eureka-server
ENV VERSION=1.0.0-SNAPSHOT

COPY ./target/$APP_NAME-$VERSION.jar $BASE_DIR/

ADD src/main/resources/bin/start-server.sh $BASE_DIR/bin/start-server.sh
RUN chmod 755 $BASE_DIR/bin/start-server.sh

ADD /src/main/resources/env.properties $BASE_DIR/
ADD /src/main/resources/java-opts.properties $BASE_DIR/
ADD /src/main/resources/start-app.sh $BASE_DIR/

RUN chmod 755  $BASE_DIR/*

WORKDIR $BASE_DIR/