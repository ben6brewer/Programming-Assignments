docker run -i --rm --name httpd -p 8080:80 -v "%cd%/html:/usr/share/nginx/html/" nginx:1.25
