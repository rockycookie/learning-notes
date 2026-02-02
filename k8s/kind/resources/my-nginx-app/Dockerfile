# Use the official NGINX image as the base
FROM nginx:alpine

# Install iproute2 which provides the 'ip' command, and iptables for network debugging
RUN apk update && apk add iproute2 bash iptables

# Keep NGINX running in the foreground as is standard for Docker containers
CMD ["nginx", "-g", "daemon off;"]