package br.edu.ifpb.gugawag.so.sockets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorNFS {

    private static List<String> arquivos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("== Servidor NFS ==");

        try (ServerSocket serverSocket = new ServerSocket(7001)) {
            System.out.println("Servidor ativo. Aguardadndo por conexões...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                // Configurar os fluxos de entrada e saída
                try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                     DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                    // Loop de comunicação com o cliente
                    while (true) {
                        // Ler a operação enviada pelo cliente
                        int operacao = dis.readInt();

                        // Executar a operação correspondente
                        switch (operacao) {
                            case 1:
                                listarArquivos(dos);
                                break;
                            case 2:
                                String nomeAntigo = dis.readUTF();
                                String nomeNovo = dis.readUTF();
                                renomearArquivo(nomeAntigo, nomeNovo);
                                dos.writeUTF("Renomear arquivo de " + nomeAntigo + " para " + nomeNovo);
                                break;
                            case 3:
                                String novoArquivo = dis.readUTF();
                                criarArquivo(novoArquivo);
                                dos.writeUTF("Criando arquivo: " + novoArquivo);
                                break;
                            case 4:
                                String arquivoRemover = dis.readUTF();
                                removerArquivo(arquivoRemover);
                                dos.writeUTF("Removendo arquivo: " + arquivoRemover);
                                break;
                            default:
                                System.out.println("Operação inválida");
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listarArquivos(DataOutputStream dos) throws IOException {
        // Enviar lista de arquivos para o cliente
        for (String arquivo : arquivos) {
            dos.writeUTF(arquivo);
        }
        // Indicar fim da lista
        dos.writeUTF("");
    }

    private static void renomearArquivo(String nomeAntigo, String nomeNovo) {
        // Implementar a lógica para renomear o arquivo no sistema de arquivos real
        System.out.println("Renomear arquivo de " + nomeAntigo + " para " + nomeNovo);
        // Atualizar a lista de arquivos em memória
        int indice = arquivos.indexOf(nomeAntigo);
        if (indice != -1) {
            arquivos.set(indice, nomeNovo);
        }
    }

    private static void criarArquivo(String novoArquivo) {
        // Implementar a lógica para criar um novo arquivo no sistema de arquivos real
        System.out.println("Criar arquivo: " + novoArquivo);
        // Adicionar o novo arquivo à lista em memória
        arquivos.add(novoArquivo);
    }

    private static void removerArquivo(String arquivoRemover) {
        // Implementar a lógica para remover um arquivo no sistema de arquivos real
        System.out.println("Remover arquivo: " + arquivoRemover);
        // Remover o arquivo da lista em memória
        arquivos.remove(arquivoRemover);
    }
}
