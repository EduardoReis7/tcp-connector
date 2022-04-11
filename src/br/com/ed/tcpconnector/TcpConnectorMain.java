package br.com.ed.tcpconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class TcpConnectorMain {

    public static void main(String[] args) throws IOException {

        String url = "http://pudim.com.br";

        String hostname = new URL(url).getHost();
        System.out.println("Loading contents of Server: " + hostname);
        InetAddress ia = InetAddress.getByName(hostname);
        String ip = ia.getHostAddress();
        System.out.println(ip + " is IP Adress for  " + hostname);
        String path = new URL(url).getPath();
        System.out.println("Requested Path on the server: " + path);
        Socket socket = new Socket(ip, 80);
        // Create input and output streams to read from and write to the server
        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //Request Line
        out.println("GET / HTTP/1.0");
        out.println("Host: " + hostname);
        //Header Lines
        out.println("User-Agent: Java/13.0.2");
        out.println("Accept-Language: en-us");
        out.println("Accept: */*");
        out.println("Connection: keep-alive");
        out.println("Accept-Encoding: gzip, deflate, br");
        // Blank Line
        out.println();

        String line = in.readLine();
        while (line != null) {
            System.out.println(line);
            line = in.readLine();
        }
        // Close our streams
        in.close();
        out.close();
        socket.close();

    }
}
