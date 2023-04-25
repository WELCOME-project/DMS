# DMS - Document Management System

## Introduction

DMS is a document management system implemented as a MAVEN project in JAVA (JDK). The DMS software consists of 4 submodules, namely DMS Commons, DMS Core, DMS Client, and DMS Service. These submodules are publicly available on GitHub at https://github.com/WELCOME-project/DMS.

## Installation, Deployment, and Execution

### Installation and Deployment

#### Software Requirements

The WELCOME DMS installation requires the use of Docker and Docker Compose. Docker is used to package DMS as an image that can be run in a container. The `.yml` configuration file and Docker Compose are used to instantiate Docker images as containers. The `.yml` file specifies the virtual network details and other necessary configurations.

#### Hardware Requirements

The DMS image requires a maximum of 4 GB of memory to run. It can utilize multiple threads, so having several CPU cores available is preferred. In the example configuration provided below, up to 4 CPU cores are used.

#### Deployment

The configuration of the DMS deployment is specified in the `.yml` file. One can use an existing image by specifying its name and tag in the `.yml` file to run the DMS container.

To create the image from scratch, access the project directory and run the command:
```
docker build -f Dockerfile -t name_of_the_image:tag .
```
Then upload it to a Maven repository with the command:
```
docker push name_of_the_image:tag
```
Use this image inside the `.yml` file.

#### Example `compose.yml` for Deployment

Here is an example `compose.yml` file that can be used for deployment. Adjust the image tag and port mapping as required:

```
version: '3.2'
dms:
  image: registry.gitlab.com/talnupf/welcome/dms:2023-02-01
  deploy:
    replicas: 1
    resources:
      limits:
        cpus: "4"
        memory: 4GB
    ports:
      - "8080:8080"
```

### Execution

The deployed DMS component can run in the cloud infrastructure of the installed WELCOME platform or be deployed externally. Make the corresponding adjustments in the dispatcher configuration to point to the appropriate service URL.

The service is accessible through a REST-like API at `http://<base_url>/dms-service/` where `<base_url>` corresponds to the location of the deployment. For example, `http://dms:8080/dms-service/` or `https://welcome-project.upf.edu/dms-service/`. Swagger documentation is available at that endpoint.