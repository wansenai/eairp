# Docker Resources

The first step is to execute the maven packaging command in the root directory
of the project:

```bash
mvn clean package
```

Then switch to the Docker directory and execute the following command
to build the Docker image:

```bash
docker build . --tag <image-name>
```

Execute the following command on your machine to start the service container
You can replace port 8080 with any other port you want to use:
```bash
docker run -it -p 8080:8088 <image-name>:latest 
```

