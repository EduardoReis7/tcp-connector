package br.com.ed.tcpconnector;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class TcpConnectorMain {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite a url: ");
        String url = scanner.next();

        URL urlObj = new URL(url);

        String hostname = urlObj.getHost();
        System.out.println("Carregando conteúdo do site: " + hostname);
        InetAddress ia = InetAddress.getByName(hostname);
        String ip = ia.getHostAddress();
        System.out.println(ip + " é o ip do site " + hostname);
        String path = urlObj.getPath();
        System.out.println("Requested Path on the server: " + path);

        if (urlObj.getProtocol().equals("https")) {
            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(ip, 443);

            // Cria as entradas e saídas para trocar informações com o servidor
            PrintStream out = new PrintStream(sslSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            // Monta a requisição
            out.println("GET / HTTP/1.0");
            out.println("Host: " + hostname);

            // Monta os cabeçalhos
            out.println("User-Agent: Java/13.0.2");
            out.println("Accept-Language: en-us");
            out.println("Accept: */*");
            out.println("Connection: keep-alive");
            out.println("Accept-Encoding: gzip, deflate, br");

            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }
            // Close our streams
            in.close();
            out.close();
            sslSocket.close();

        } else {
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
}
