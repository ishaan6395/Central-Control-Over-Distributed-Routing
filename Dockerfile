# Download and open openjdk image from net
FROM openjdk
# Make project directory in image
CMD mkdir project
# Copy corrent folder to project directory
COPY . project
# Switch working directory to project
WORKDIR /project
# Run program
CMD java -cp "out:lib/gson-2.6.2.jar" $SYSTEM_TYPE
