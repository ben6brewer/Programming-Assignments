#!/bin/bash -ex

docker run \
    -it \
    --rm \
    --name hw03 \
    -p 8080:80 \
    hw03:latest
