# Used ubuntu to set user and its group in a different way

FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace/app

COPY .mvn .mvn
COPY pom.xml mvnw ./
COPY src src

RUN ./mvnw clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../ip-address-*.jar)

FROM eclipse-temurin:20-jre AS runner
VOLUME /tmp

RUN useradd --user-group --system --create-home --no-log-init spring-app
USER spring-app

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.glinboy.ip.address.IpAddressApplication"]