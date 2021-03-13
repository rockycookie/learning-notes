## Problem Desciption
Opened Ubuntu 20.4.0 several monthes no-use. Wifi can not find anything.
```
ifconfig
```
gives only
```
lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 801  bytes 90573 (90.5 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 801  bytes 90573 (90.5 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

<br/>

But fortunately it can still see the Wifi hardwire.
```
lspci | grep -i network
```
gives
```
02:00.0 Network controller: Broadcom Inc. and subsidiaries BCM4352 802.11ac Wireless Network Adapter (rev 03)
```
and 
```
sudo lshw -C network
```
gives
```
  *-network UNCLAIMED
       description: Wireless interface
       product: BCM4352 802.11ac Wireless Network Adapter
       vendor: Broadcom Inc. and subsidiaries
       ...
```

<br/>

## Fix Procedure
Install the new bcmwl driver
1. Remove the current bcmwl driver
    ```
    sudo apt purge -y bcmwl-kernel-source
    ```
    after which
    ```
    dpkg -l | grep bcmwl
    ```
    should give nothing

2. Installed the new version
    1. Download from another laptop since the Ubuntu has no internet access from https://pkgs.org/download/bcmwl-kernel-source, and copy by USB
    2. Install and reboot
        ```
        sudo dpkg -i bcmwl-kernel-source_6.30.223.271+bdcom-0ubuntu7~20.04.1_amd64.deb && reboot
        ```
