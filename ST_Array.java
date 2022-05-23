// TS = tabela de símbolos

public class ST_Array<Key extends Comparable<Key>, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de um vetor dinâmico ordenado.
    */
    private Item[] items;
    private Key[] keys;
    private int top; // guarda o índice (sendo que o primeiro é zero) do último elemento da tabela de símbolos (maior)
    private int size; // guarda o tamanho do vetor dinâmico

    public ST_Array() {
        items = (Item[]) new Object[1];
        keys = (Key[]) new Comparable[1];
        top = -1;
        size = 1;
    }

    public void add(Key key, Item val) {
        /*
        Insere, na tabela de símbolos, chave e valor dados como argumentos.
        */
        if (top == -1) { // caso em que a TS está vazia
            items[++top] = val;
            keys[top] = key;
            return;
        }

        // ----- BUSCA PELA POSIÇÃO DO ELEMENTO A SER ADICIONADO -----
        int indice = 0; // guarda o índice do primeiro elemento maior que o valor (será o índice do novo valor)
        boolean encontrou = false;
        for (int i = 0; i <= top; i++) {
            if (keys[i].compareTo(key) == 0) { // chave já existe na TS
                items[i] = val;
                return;
            }
            if (keys[i].compareTo(key) > 0) { // encontrou o primeiro elemento maior que o valor
                indice = i;
                encontrou = true;
                break;
            }
        }

        if (size <= top + 1) { // tamanho do array não suporta novos elementos
            resize(2*size);
        }
        if (!encontrou) { // caso em que todos os elementos são menores ou iguais
            items[++top] = val;
            keys[top] = key;
            return;
        }
        for (int j = top + 1; j > indice; j--) { // "empurrar" os elementos maiores que o novo item para a direita
            items[j] = items[j - 1];
            keys[j] = keys[j - 1];
        }
        items[indice] = val;
        keys[indice] = key;
        ++top;
    }

    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não estiver na TS, retorna null.
        */
        Item item = items[rank(key)];
        Key chave = keys[rank(key)];
        if (key.compareTo(chave) == 0)
            return item;
        return null;
    }

    public int rank(Key key) {
        /*
        Retorna o número de chaves menores do que a chave dada como argumento, por meio da busca binária.
        */
        int baixo = 0, cima = top;
        while (baixo <= cima) {
            int meio = (baixo + cima) / 2;
            int comparacao = key.compareTo(keys[meio]);
            if (comparacao < 0)
                cima = meio - 1;
            else if (comparacao > 0)
                baixo = meio + 1;
            else
                return meio;
        }
        return baixo;
    }

    public Key select(int k) {
        /*
        Dado o valor do rank de um elemento da TS, retorna a sua chave. Caso o rank seja maior ou igual à
        quantidade de elementos na TS, retorna null.
        */
        if (k <= top)
            return keys[k];
        return null;
    }

    private void resize(int new_size) {
        /*
        Função auxiliar que realiza o processo de expansão do tamanho da TS (pela criação de um novo array
        de novo tamanho e cópia dos elementos do antigo), sobretudo quando o número de elementos excede o ta-
        manho atual do array que representa a TS.
        */
        size = new_size;
        Item[] new_items = (Item[]) new Object[new_size];
        Key[] new_keys = (Key[]) new Comparable[new_size];
        for (int i = 0; i <= top; i++) {
            new_items[i] = items[i];
            new_keys[i] = keys[i];
        }
        items = new_items;
        keys = new_keys;
    }

    public static void main (String[] args) {
        ST_Array<Integer, Integer> st = new ST_Array<>();
        st.add(8, 1);
        st.add(12, 1);
        st.add(17, 1);
        st.add(11, 1);
        st.add(15, -8);
        st.add(4, 1);
        st.add(1, 12);
        st.add(17, 3);
        System.out.println(st.value(15));
        System.out.println(st.value(1));
        System.out.println(st.value(2));
        System.out.println(st.rank(15));
        System.out.println(st.rank(1));
        System.out.println(st.rank(12));
        System.out.println(st.rank(16));
        System.out.println(st.select(5));
        System.out.println(st.select(0));
        System.out.println(st.select(4));
        System.out.println(st.select(6));
        System.out.println(st.select(7));
    }
}