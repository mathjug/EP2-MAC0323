// REVISAR DESCRIÇÕES DOS MÉTODOS DE TODOS OS ARQUIVOS
// TERMINAR MAIN: Factory Method Pattern (https://stackoverflow.com/questions/34519878/decide-what-constructor-call-based-on-user-input)

import java.util.Scanner;
import java.io.*;

public class EP2_MAC0323 {
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
        switch (tipo_ST) {
            case "VO":
                ST_Array<String, Integer> ts_array = new ST_Array<>();
            case "ABB":
                ST_BST<String, Integer> ts_ABB = new ST_BST<>();
            case "TR":
                ST_Treap<String, Integer> ts_TR = new ST_Treap<>(n_palavras);
            case "A23":
                ST_23<String, Integer> ts_23 = new ST_23<>();
            case "ARN":
                ST_RB<String, Integer> ts_RN = new ST_RB<>();
        }

        int n_comandos = Integer.parseInt(input_comandos.nextLine());
        int lidas = 0;
        while (lidas < n_comandos) {
            String comando = input_comandos.nextLine();
            int num_comando = comando.charAt(0) - '0';
            String s = comando.substring(3);
            int k;
            switch (num_comando) {
                case 1:
                    k = Integer.parseInt(s);
                case 2:
                    //ts.value(s);
                case 3:
                    //ts.rank(s);
                case 4:
                    k = Integer.parseInt(s);
                    //ts.select(k);
            }
            lidas++;
        }

        // EXIBIR AS SAÍDAS
        

        input_comandos.close();
    }
}