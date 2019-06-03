AP_NAME=mybatis-graalvm-demo
docker stop ${AP_NAME}
docker rm ${AP_NAME}
./mvnw clean package
docker build . -t ${AP_NAME}
docker run --name ${AP_NAME} -d ${AP_NAME}
docker exec -it ${AP_NAME} bash
