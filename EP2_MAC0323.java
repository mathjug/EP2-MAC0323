import java.util.Scanner;
import java.io.*;

public class EP2_MAC0323 {
    /*
    Arquivo principal do Exercício-Programa, o qual lê o arquivo de input (cujo endereço é solicitado ao usuário),
    com o texto e os comandos, cria objetos das classes necessárias e faz as chamadas dos métodos solicitados.
    */

    public static ST<String, Integer> createInstance(String type, int n_palavras) {
        /*
        Possibilita implementar aquilo que é conhecido como Factory Method Pattern: cria-se uma tabela de símbolos
        de tipo genérico e, de acordo com a escolha do usuário, altera o tipo dela.
        */
        ST<String, Integer> st = null;
        switch(type) {
            case "VO":
                st = new ST_Array<String, Integer>();
                break;
            case "ABB":
                st = new ST_BST<String, Integer>();
                break;
            case "TR":
                st = new ST_Treap<String, Integer>(n_palavras);
                break;
            case "A23":
                st = new ST_23<String, Integer>();
                break;
            case "ARN":
                st = new ST_RB<String, Integer>();
                break;
            default:
                System.out.println("Tipo de Tabela de Símbolos inválida!");
                System.exit(1);
            }
        return st;
    }

    public static void main (String[] args) throws IOException {
        Scanner input_usuarios = new Scanner(System.in); 
        System.out.print("Nome do arquivo de comandos: ");  
        String end_comandos = input_usuarios.nextLine(); // contém o endereço do arquivo de input
        input_usuarios.close();

        File arquivo_comandos = new File(end_comandos);
        Scanner input_comandos = new Scanner(arquivo_comandos);
        
        String tipo_ST = input_comandos.nextLine(); // recebe qual deverá ser o tipo de ST
        
        int n_palavras = Integer.parseInt(input_comandos.nextLine()); // recebe o número de palavras no texto de input
        String[] palavras = new String[n_palavras]; 

        int contador = 0;
        while (contador < n_palavras) { // guarda todas as palavras do texto em um array
            String palavra = input_comandos.next(); // recebe a próxima palavra
            palavra = palavra.replaceAll("[^a-zA-Z ]", ""); // tira pontuação
            palavras[contador++] = palavra;
        }

        ST<String, Integer> ts = createInstance(tipo_ST, n_palavras);

        input_comandos.nextLine();
        int n_comandos = Integer.parseInt(input_comandos.nextLine()); // guarda o número de comandos a serem lidos
        int lidas = 0;
        contador = 0;

        long start = System.currentTimeMillis();
        while (lidas < n_comandos) { // lê todos os comandos
            String comando = input_comandos.nextLine();
            int num_comando = comando.charAt(0) - '0'; // guarda o número do comando
            String s = comando.substring(2); // guarda o complemento do comando
            int k;
            switch (num_comando) {
                case 1: // adicionar as próximas k palavras na TS
                    k = Integer.parseInt(s);
                    for (int i = 0; i < k; i++) {
                        String nova_palavra = palavras[contador++];
                        Integer recorrencia = ts.search(nova_palavra);
                        if (recorrencia == null)
                            recorrencia = 0;
                        ts.add(nova_palavra, recorrencia + 1);
                    }
                    break;
                case 2: // quantas vezes s apareceu no texto até agora
                    System.out.println(ts.value(s));
                    break;
                case 3: // quantas palavras são menores que s na TS
                    System.out.println(ts.rank(s));
                    break;
                case 4: // qual a k-ésima chave da TS
                    k = Integer.parseInt(s);
                    System.out.println(ts.select(k));
                    break;
            }
            lidas++;
        }
        long end = System.currentTimeMillis();
        input_comandos.close();
        long tempo_execucao = end - start;
        System.out.printf("\nTempo de execução: %d milissegundos.\n", tempo_execucao);
    }
}