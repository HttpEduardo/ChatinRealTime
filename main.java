// Servidor de chat
import java.net.*;
import java.io.*;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Servidor de chat iniciado na porta 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nova conexão estabelecida: " + clientSocket);

            // Cria um thread para lidar com a conexão do cliente
            Thread t = new ClientThread(clientSocket);
            t.start();
        }
    }
}

class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            // Cria as streams de entrada e saída para a conexão com o cliente
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Lê o nome do cliente
            String name = in.readLine();
            System.out.println("Novo cliente conectado: " + name);

            // Envia uma mensagem de boas-vindas para o cliente
            out.println("Bem-vindo ao chat, " + name + "!");

            // Envia as mensagens recebidas para todos os clientes conectados
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(name + ": " + message);
                out.println(name + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Erro na conexão com o cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar a conexão com o cliente: " + e.getMessage());
            }
        }
    }
}

// Cliente de chat
import java.net.*;
import java.io.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        // Lê o nome do cliente
        System.out.print("Digite seu nome: ");
        String name = stdin.readLine();
        out.println(name);

        // Lê as mensagens enviadas pelo servidor e imprime na tela
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println(message);
        }

        // Lê as mensagens digitadas pelo usuário e envia para o servidor
        while ((message = stdin.readLine()) != null) {
            out.println(message);
        }
    }
}
