FROM oracle/graalvm-ce:19.0.0 as graalvm
WORKDIR /home/app/mybatis-graalvm-demo
COPY ./target/mybatis-graalvm-demo.jar .
RUN gu install native-image
RUN native-image --no-server --verbose -jar ./mybatis-graalvm-demo.jar

FROM postgres
COPY --from=graalvm /home/app/mybatis-graalvm-demo/mybatis-graalvm-demo .
