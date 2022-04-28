package br.com.ed.tcpconnector;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpConnectorMain {

    public static void main(String[] args) throws IOException {

        // Recebe a url do usuário
        URL url = entradaDeDados();

        // Extrai da URL o hostname da página
        String hostname = extrairHostname(url);
        // Extrai e traduz o hostname da página em um endereço IP
        String ip = extrairIp(hostname);
        // Extrai da URL a rota desejada pelo usuário
        String rota = extrairRota(url);

        // Extrai da URL a informação de qual tipo de protocolo o site requisitado utiliza. (HTTP ou HTTPS)
        if (url.getProtocol().equals("https")) {
            conexaoHttps(ip, hostname, rota);
        } else {
            conexaoHttp(ip, hostname, rota);
        }
    }

    private static URL entradaDeDados() throws MalformedURLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite a url: ");
        String url = scanner.next();
        scanner.close();

        return new URL(url);
    }

    private static String extrairHostname(URL url) {
        String hostname = url.getHost();
        System.out.println("Carregando conteúdo do site: " + hostname);

        return hostname;
    }

    private static String extrairIp(String hostname) throws UnknownHostException {
        InetAddress ia = InetAddress.getByName(hostname);
        String ip = ia.getHostAddress();
        System.out.println(ip + " é o ip do site " + hostname);

        return ip;
    }

    private static String extrairRota(URL url) {
        String rota = url.getPath();
        String validaRota = rota.equals("") ? "/" : rota;
        System.out.println("Rota solicitada: " + validaRota);

        return rota;
    }

    private static void montarRequisicao(PrintStream out, String hostname, String caminho) {

        // Monta a requisição
        out.println("GET " + caminho + " HTTP/1.0");
        out.println("Host: " + hostname);

        // Monta os cabeçalhos
        out.println("User-Agent: Java/11.0.12");
        out.println("Accept: */*");
        out.println("Connection: keep-alive");

        // Pula linha
        out.println();
    }

    private static void lerConteudo(BufferedReader in) throws IOException {

        // Lê todas as linhas do html da página
        String line = in.readLine();
        while (line != null) {
            System.out.println(line);
            line = in.readLine();
        }
    }

    private static void fecharStreaming(BufferedReader in, PrintStream out, Socket socket) throws IOException {

        // Fechamento do streaming de dados
        System.out.println("Iniciando fechamento das conexões.");
        in.close();
        out.close();
        socket.close();
    }

    private static void conexaoHttp(String ip, String hostname, String rota) throws IOException {

        // Cria o socket de comunicação com o servidor
        Socket socket = new Socket(ip, 80);

        // Cria as entradas e saídas para trocar informações com o servidor
        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        montarRequisicao(out, hostname, rota);

        lerConteudo(in);

        fecharStreaming(in, out, socket);
    }

    private static void conexaoHttps(String ip, String hostname, String rota) throws IOException {

        // Cria o socket de comunicação com o servidor
        SocketFactory socketFactory = SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(ip, 443);

        // Cria as entradas e saídas para trocar informações com o servidor
        PrintStream out = new PrintStream(sslSocket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

        montarRequisicao(out, hostname, rota);

        lerConteudo(in);

        fecharStreaming(in, out, sslSocket);
    }
}
