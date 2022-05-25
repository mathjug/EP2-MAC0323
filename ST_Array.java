// TS = tabela de símbolos

public class ST_Array<Key extends Comparable<Key>, Item> implements ST<Key, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de um Vetor Dinâmico Ordenado.
    */
    private Item[] items;
    private Key[] keys;
    private int top; // guarda o índice (sendo que o primeiro é zero) do último elemento no vetor
    private int size; // guarda o tamanho do vetor dinâmico

    public ST_Array() {
        items = (Item[]) new Object[1];
        keys = (Key[]) new Comparable[1];
        top = -1;
        size = 1;
    }

    private int binarySearch(Key key, int lo, int hi) {
        /*
        Faz, recursivamente, a busca binária no array ordenado. Caso encontre a chave desejada, retorna
        o seu índice. Caso contrário, retorna o índice daquele que, no array atual, viria depois da chave,
        ao ser inserida.
        */
        if (lo > top) // chave é maior do que todas no array
            return top + 1;
        if (hi == -1) // chave é menor do que todas no array
            return 0;
        if (lo > hi) // não encontrou a chave e não está nas extremidades
            return hi + 1;
        int mid = (lo + hi) / 2;
        int cmp = key.compareTo(keys[mid]);
        if (cmp == 0)
            return mid;
        else if (cmp > 0) // busca recursiva na segunda metade
            return binarySearch(key, (mid + 1), hi);
        else // busca recursiva na primeira metade
            return binarySearch(key, lo, (mid - 1));
    }

    @Override
    public Item search(Key key) {
        /*
        Busca, no array ordenado, a chave dada como argumento. Se encontrar, retorna o seu valor.
        Caso contrário, retorna null.
        */
        int indice = binarySearch(key, 0, top);
        if (indice <= top && key.equals(keys[indice])) // chave está no array
            return items[indice];
        return null; // chave não está no array
    }

    @Override
    public void add(Key key, Item val) {
        /*
        Insere, no array ordenado, chave e valor dados como argumentos.
        */
        if (top == -1) { // caso em que a TS está vazia
            items[++top] = val;
            keys[top] = key;
            return;
        }
        int indice = binarySearch(key, 0, top); // guarda o índice do primeiro elemento maior que o valor (será o índice do novo valor)
        if (indice <= top && key.equals(keys[indice])) { // chave está no array
            items[indice] = val;
            return;
        }
        if (size <= top + 1) // tamanho do array não suporta novos elementos
            resize(2*size);
        if (indice > top) { // todas as chaves no array são menores
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

    @Override
    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não estiver no array ordenado, retorna null.
        */
        Item item = items[rank(key)];
        Key chave = keys[rank(key)];
        if (key.compareTo(chave) == 0)
            return item;
        return null;
    }

    @Override
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

    @Override
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
        Função auxiliar que realiza o processo de expansão do tamanho do array (pela criação de um novo array
        de novo tamanho e cópia dos elementos do antigo).
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