// REVISAR DESCRIÇÕES DOS MÉTODOS DE TODOS OS ARQUIVOS
// CRIAR MÉTODO SEARCH ARRAYS
// TERMINAR MAIN: Factory Method Pattern (https://www.geeksforgeeks.org/factory-method-design-pattern-in-java/)
// - adição de elementos na árvore

import java.util.Scanner;
import java.io.*;

public class EP2_MAC0323 {

    public static ST<String, Integer> createInstance(String type, int n_palavras) {
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
        String end_comandos = input_usuarios.nextLine();  
        input_usuarios.close();

        File arquivo_comandos = new File(end_comandos);
        Scanner input_comandos = new Scanner(arquivo_comandos);
        
        String tipo_ST = input_comandos.nextLine(); // recebe qual deverá ser o tipo de ST
        
        int n_palavras = Integer.parseInt(input_comandos.nextLine());
        String[] palavras = new String[n_palavras]; 

        int contador = 0;
        while (contador < n_palavras) {
            String palavra = input_comandos.next(); // recebe a próxima palavra, até encontrar espaço
            palavra = palavra.replaceAll("[^a-zA-Z ]", ""); // tira pontuação
            palavras[contador++] = palavra;
        }

        ST<String, Integer> ts = createInstance(tipo_ST, n_palavras);

        int n_comandos = Integer.parseInt(input_comandos.nextLine());
        int lidas = 0;
        while (lidas < n_comandos) {
            String comando = input_comandos.nextLine();
            int num_comando = comando.charAt(0) - '0';
            String s = comando.substring(3);
            int k;
            contador = 0;
            switch (num_comando) {
                case 1: // adicionar as próximas k palavras na TS
                    k = Integer.parseInt(s);
                    for (int i = 0; i < k; i++) {
                        String nova_palavra = palavras[contador++];
                        // ENCONTRAR A RECORRENCIA (criar novo método search, que já retorna a recorrência, se existir, senão, -1)
                        // ts.add(nova_palavra, recorrencia);
                    }
                    break;
                case 2: // quantas vezes s apareceu no texto até agora
                    System.out.println(ts.value(s));
                case 3: // quantas palavras são menores que s na TS
                    System.out.println(ts.rank(s));
                case 4: // qual a k-ésima chave da TS
                    k = Integer.parseInt(s);
                    System.out.println(ts.select(k));
            }
            lidas++;
        }
        input_comandos.close();
    }
}