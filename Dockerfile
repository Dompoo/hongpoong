FROM openjdk:21-slim

ARG PROFILE
ARG DB_URL_PROD
ARG DB_USERNAME_PROD
ARG DB_PASSWORD_PROD
ARG JWT_SECRET
ARG JWT_RESET_SECRET

COPY build/libs/Hongpoong-0.0.1-SNAPSHOT.jar app.jar

ENV PROFILE=${PROFILE}
ENV DB_URL_PROD=${DB_URL_PROD}
ENV DB_USERNAME_PROD=${DB_USERNAME_PROD}
ENV DB_PASSWORD_PROD=${DB_PASSWORD_PROD}
ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_RESET_SECRET=${JWT_RESET_SECRET}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]