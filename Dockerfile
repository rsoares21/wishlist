# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged WAR file into the container at /app
COPY target/wishlist-0.0.1-SNAPSHOT.war /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the WAR file when the container launches
CMD ["java", "-jar", "wishlist-0.0.1-SNAPSHOT.war"]
