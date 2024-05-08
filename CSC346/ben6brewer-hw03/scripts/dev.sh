#!/bin/bash -ex

docker run \
    -it \
    --rm \
    --name hw03 \
    -p 8080:80 \
    -v "$PWD/html:/usr/share/nginx/html/" \
    nginx:1.25
