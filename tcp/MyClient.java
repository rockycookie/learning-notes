
// TCP simplex-talk CLIENT in java
import java.net.*;
import java.io.*;

public class MyClient {
    static public BufferedReader bin;
    static public int destport = 5431;

    static public void main(String args[]) throws IOException {
        String desthost = "localhost";
        if (args.length >= 1)
            desthost = args[0];
        bin = new BufferedReader(new InputStreamReader(System.in));
        InetAddress dest;
        System.err.print("Looking up address of " + desthost + "...");
        try {
            dest = InetAddress.getByName(desthost);
        } catch (UnknownHostException uhe) {
            System.err.println("unknown host: " + desthost);
            return;
        }
        System.err.println(" got it!");
        System.err.println("connecting to port " + destport);
        Socket s;
        try {
            s = new Socket(dest, destport);
        } catch (IOException ioe) {
            System.err.println("cannot connect to <" + desthost + "," + destport + ">");
            return;
        }
        OutputStream sout;
        try {
            sout = s.getOutputStream();
        } catch (IOException ioe) {
            System.err.println("I/O failure!");
            s.close();
            return;
        }
        // ============================================================
        while (true) {
            String buf;
            try {
                buf = bin.readLine();
            } catch (IOException ioe) {
                System.err.println("readLine() failed");
                s.close();
                return;
            }
            if (buf == null)
                break; // user typed EOF character
            buf = buf + "\n"; // protocol requires sender includes \n
            byte[] bbuf = buf.getBytes();
            try {
                sout.write(bbuf);
            } catch (IOException ioe) {
                System.err.println("write() failed");
                s.close();
                return;
            }
        } // while

        s.close();
    }
}
