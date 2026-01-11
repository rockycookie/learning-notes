# Use the official NGINX image as the base
FROM nginx:alpine

# Install iproute2 which provides the 'ip' command
RUN apk update && apk add iproute2 bash

# Keep NGINX running in the foreground as is standard for Docker containers
CMD ["nginx", "-g", "daemon off;"]