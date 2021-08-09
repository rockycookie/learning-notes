# TODO list of this repo/floder
1. Read and understand the Java server and client
2. Follow the PDF "An Introduction to Computer Networks":
    - Here are some things to try with THREADING=false in the server, What happens to the client? (It may take a couple message lines.)
        - start up two clients while the server is running. Type some message lines into both. Then exit the first client.
        - start up the client before the server.
        - start up the server, and then the client. Kill the server and then type some message lines to the client.
        - start the server, then the client. Kill the server and restart it. Now what happens to the client?
    - With THREADING=true, try connecting multiple clients simultaneously to the server. How does this behave differently from the first example above?
