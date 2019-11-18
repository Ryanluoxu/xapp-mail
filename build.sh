#!/usr/bin/env bash

DIR="$(pwd -P)"
PROJECT_NAME="xapp-mail"
APPLICATION_VERSION="1.0"
APP_NAME="app-backend"
DOCKER_GRADLE_VERSION="4.7-jdk8"
DOCKER_URL=""

build_jar(){
  CMD="gradle clean && gradle bootRepackage"
  bash -c "${CMD}"
}
build_image(){
  cp ${DIR}/docker/Dockerfile ${DIR}/application*.yml ${DIR}/logback-spring.xml ${DIR}/application/build/libs
  cp ${DIR}/.gitignore ${DIR}/application/build/libs/.dockerignore
  mv ${DIR}/application/build/libs/*.jar ${DIR}/application/build/libs/application.jar
  docker build --build-arg APPLICATION_VERSION=${APPLICATION_VERSION} -t ${IMAGE_NAME} ${DIR}/application/build/libs
  dokcer tag ${IMAGE_NAME} ${IMAGE_NAME_LATEST}
  docker push ${IMAGE_NAME}
  docker push ${IMAGE_NAME_LATEST}
}
