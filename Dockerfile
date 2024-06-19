# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Clone the GitHub repository into the container
RUN apk --no-cache add git \
    && git clone https://github.com/rsoares21/wishlist.git /app \
    && cd /app \
    && ./mvnw package \
    && mv target/wishlist-0.0.1-SNAPSHOT.war /app/wishlist.war \
    && rm -rf /app/*

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the WAR file when the container launches
CMD ["java", "-jar", "wishlist.war"]