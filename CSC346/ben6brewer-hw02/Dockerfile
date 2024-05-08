FROM nginx:1.25-alpine

WORKDIR /usr/share/nginx/html

RUN curl -O https://www2.cs.arizona.edu/classes/cs346/spring24/homework/hw02-files.tar
RUN tar -xf hw02-files.tar
RUN mv hw02-files/* .
RUN rm hw02-files.tar

COPY ./default.conf /etc/nginx/conf.d/default.conf