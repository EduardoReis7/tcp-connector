package br.com.ed.tcpconnector;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class TcpConnectorMain {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a url: ");
        String url = scanner.next();

        URL urlObj = new URL(url);

        String hostname = urlObj.getHost();
        System.out.println("Carregando conteúdo do site: " + hostname);
        InetAddress ia = InetAddress.getByName(hostname);
        String ip = ia.getHostAddress();
        System.out.println(ip + " é o ip do site " + hostname);
        String path = urlObj.getPath();
        String validaRota = path.equals("") ? "/" : path;
        System.out.println("Rota solicitada: " + validaRota);

        if (urlObj.getProtocol().equals("https")) {

            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(ip, 443);

            // Cria as entradas e saídas para trocar informações com o servidor
            PrintStream out = new PrintStream(sslSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            // Monta a requisição
            out.println("GET " + validaRota + " HTTP/1.0");
            out.println("Host: " + hostname);

            // Monta os cabeçalhos
            out.println("User-Agent: Java/11.0.12");
            out.println("Accept: */*");
            out.println("Connection: keep-alive");

            // Pula linha
            out.println();

            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }

            // Fechamento do streaming de dados
            System.out.println("Iniciando fechamento das conexões.");
            in.close();
            out.close();
            sslSocket.close();

        } else {
            Socket socket = new Socket(ip, 80);

            // Cria as entradas e saídas para trocar informações com o servidor
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Monta a requisição
            out.println("GET " + validaRota + " HTTP/1.0");
            out.println("Host: " + hostname);

            // Monta os cabeçalhos
            out.println("User-Agent: Java/11.0.12");
            out.println("Accept: */*");
            out.println("Connection: keep-alive");

            // Pula linha
            out.println();

            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }

            // Fechamento do streaming de dados
            System.out.println("Iniciando fechamento das conexões.");
            in.close();
            out.close();
            socket.close();
        }
    }
}
