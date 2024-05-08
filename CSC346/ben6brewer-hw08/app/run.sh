#!/bin/bash -ex

docker run -i --rm \
    --name nginx \
    -p 8080:80 \
    -v "$PWD/html:/usr/share/nginx/html/" \
    nginx:1.25

