package br.edu.ifpb.gugawag.so.sockets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClienteNFS {

    public static void main(String[] args) {
        System.out.println("== Cliente ==");

        try (Socket socket = new Socket("127.0.0.1", 7001);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                exibirMenu();

                int escolha = scanner.nextInt();
                dos.writeInt(escolha);
                dos.flush();

                switch (escolha) {
                    case 1:
                        listarArquivos(dis);
                        break;
                    case 2:
                        renomearArquivo(scanner, dos, dis);
                        break;
                    case 3:
                        criarArquivo(scanner, dos, dis);
                        break;
                    case 4:
                        removerArquivo(scanner, dos, dis);
                        break;
                    case 5:
                        System.out.println("Encerrando conexão.");
                        return;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exibirMenu() {
        System.out.println("\nEscolha a operação:");
        System.out.println("1. Listar arquivos");
        System.out.println("2. Renomear arquivo");
        System.out.println("3. Criar arquivo");
        System.out.println("4. Remover arquivo");
        System.out.println("5. Sair");
        System.out.print("Digite o número da operação desejada: ");
    }

    private static void listarArquivos(DataInputStream dis) throws IOException {
        String resposta;
        while (!(resposta = dis.readUTF()).isEmpty()) {
            System.out.println(resposta);
        }
    }

    private static void renomearArquivo(Scanner scanner, DataOutputStream dos, DataInputStream dis) throws IOException {
        System.out.println("Digite o nome do arquivo antigo:");
        String nomeAntigo = scanner.next();
        System.out.println("Digite o novo nome do arquivo:");
        String nomeNovo = scanner.next();

        dos.writeUTF(nomeAntigo);
        dos.writeUTF(nomeNovo);
        dos.flush();

        // Receber resposta do servidor após o renomeio
        String resposta = dis.readUTF();
        System.out.println(resposta);
    }

    private static void criarArquivo(Scanner scanner, DataOutputStream dos, DataInputStream dis) throws IOException {
        System.out.println("Digite o nome do novo arquivo:");
        String novoArquivo = scanner.next();

        dos.writeUTF(novoArquivo);
        dos.flush();

        // Receber resposta do servidor após a criação do arquivo
        String resposta = dis.readUTF();
        System.out.println(resposta);
    }

    private static void removerArquivo(Scanner scanner, DataOutputStream dos, DataInputStream dis) throws IOException {
        System.out.println("Digite o nome do arquivo a ser removido:");
        String arquivoRemover = scanner.next();

        dos.writeUTF(arquivoRemover);
        dos.flush();

        // Receber resposta do servidor após a remoção do arquivo
        String resposta = dis.readUTF();
        System.out.println(resposta);
    }
}
