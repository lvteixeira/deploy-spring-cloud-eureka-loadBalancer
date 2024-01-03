FROM bellsoft/liberica-runtime-container:jdk-17-slim-musl

# Set the working directory in the container
WORKDIR /app

RUN java --version

# Copy the Gradle wrapper files
RUN ls -al
COPY gradlew /app
COPY gradle /app/gradle

# Copy the build file and settings
COPY build.gradle /app
COPY settings.gradle /app

# Copy the source code
COPY src /app/src
RUN ls -al

RUN pwd
# Make the Gradle wrapper executable
RUN tr -d '\r' < gradlew > gradlew_temp
RUN mv gradlew_temp gradlew
RUN chmod +x gradlew
RUN ./gradlew -version

# Debugging: Print the contents of the Gradle wrapper directory
RUN ls -al
RUN ls -al /app/gradle


# Build the application
RUN ./gradlew build

# Expose the port that the application will run on
EXPOSE 8888

# Set the entry point to run the application
CMD ["/app/gradlew", "bootRun"]
