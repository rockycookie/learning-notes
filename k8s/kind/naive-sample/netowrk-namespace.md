# Network Namespaces

- Processes in different network namespaces cannot see or interact with each other’s network interfaces or IP addresses
- The default namespace is the host’s network stack
- Containers and pods typically run in their own network namespaces
- Network namespaces enable features like container networking, where each container or pod gets its own virtual network environment
