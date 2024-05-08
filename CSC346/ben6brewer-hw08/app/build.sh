#!/bin/bash -ex

docker build \
    --platform linux/amd64 \
    --tag hw08 \
    .

docker tag hw08:latest 851725597229.dkr.ecr.us-east-1.amazonaws.com/hw08:latest

