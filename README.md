# laa-ccms-caab-api

This API is made up of multiple projects:
* caab-api
* caab-service

## caab-api

The caab-api project is a lightweight api interface that is generated using the open-api generator.
The [open-api-specification.yml](./caab-api/open-api-specification.yml) need to be kept up to date. 

In order to generate the interface and models a build can be run on the overall project due to the gradle task dependency setup.

## caab-service

the caab-service implements the api interface generated in the caab-api subproject.
This service directly interacts with the Transient Data Store in EBS.