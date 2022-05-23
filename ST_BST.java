public class ST_BST<Key extends Comparable<Key>, Item> extends ST<Key, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de uma Árvore de Busca Binária.
    */
    private Node raiz;

    private class Node {
        private Key key;
        private Item value;
        private Node direita;
        private Node esquerda;
        private Node pai;
        private int n_nos; // guarda a quantidade de nós em uma (sub-)árvore, contando com a raiz

        private Node(Key key, Item value) {
            this.key = key;
            this.value = value;
            direita = null;
            esquerda = null;
            pai = null;
            n_nos = 1;
        }
    }

    public ST_BST() {
        raiz = null;
    }

    private int size(Node no) {
        if (no == null) {
            return 0;
        }
        return no.n_nos;
    }

    private Node search(Key key) {
        /*
        Busca a chave dada como argumento em uma árvore de busca binária. Se encontrar, retorna o nó.
        Caso contrário, retorna aquele que, se a chave estivesse na árvore, seria seu pai.
        */
        Node no_atual = raiz;
        Node anterior = null;
        while (no_atual != null) {
            anterior = no_atual;
            int cmp = key.compareTo(no_atual.key);
            if (cmp > 0)
                no_atual = no_atual.direita;
            else if (cmp < 0)
                no_atual = no_atual.esquerda;
            else
                return no_atual;
        }
        return anterior; // chega aqui se a chave não existir na árvore
    }

    private void update_N_nos(Node no) {
        /*
        Recebido um nó, incrementa em 1 a quantidade de nós na sub-árvore enraizada nele. Faz isso
        iterativamente, para todos acima e no caminho desse nó, até a raiz da árvore principal
        */
        while (no != null) {
            no.n_nos++;
            no = no.pai;
        }
    }

    public void add(Key key, Item val) {
        /*
        Insere, na tabela de símbolos, chave e valor dados como argumentos.
        */
        if (raiz == null) { // TS está vazia
            raiz = new Node(key, val);
            return;
        }
        Node no = search(key); // retorna o próprio nó ou o seu possível pai
        int cmp = key.compareTo(no.key);
        if (cmp > 0) {
            no.direita = new Node(key, val);
            no.direita.pai = no;
            update_N_nos(no);
        }
        else if (cmp < 0) {
            no.esquerda = new Node(key, val);
            no.esquerda.pai = no;
            update_N_nos(no);
        }
        else
            no.value = val;
    }

    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não existir na árvore, retorna null.
        */
        Node no = search(key); // retorna o próprio nó ou o seu possível pai (que pode ser null)
        if (no != null)
            if (key.compareTo(no.key) == 0)
                return no.value;
        return null;
    }

    public int rank(Key key) {
        /*
        Retorna o número de chaves menores do que a chave dada como argumento, por meio da busca binária.
        */
        return rank(raiz, key);
    }

    private int rank(Node no, Key key) {
        /*
        Método auxiliar para que rank possa fazer chamadas recursivas, tendo como raiz outros nós da
        árvore binária.
        */
        if (no == null)
            return 0;
        int cmp = key.compareTo(no.key);
        if (cmp >= 0) {
            if (cmp > 0)
                return size(no.esquerda) + rank(no.direita, key) + 1;
            else
                return size(no.esquerda) + rank(no.direita, key);
        }
        else
            return rank(no.esquerda, key);
    }

    public Key select(int k) {
        /*
        Dado o valor do rank de um elemento da TS, retorna a sua chave. Caso o valor do rank supere
        a quantidade de nós na árvore, retorna null.
        */
        if (k >= size(raiz) || k < 0)
            return null;
        return select(raiz, k);
    }

    private Key select(Node no, int k) {
        /*
        Método auxiliar para que select possa fazer chamadas recursivas, tendo como raiz outros nós da
        árvore binária.
        */
        if (no == null)
            return null;
        int n = size(no.esquerda);
        if (n > k)
            return select(no.esquerda, k);
        else if (n < k)
            return select(no.direita, k - n - 1);
        else
            return no.key;
    }

    public static void main (String[] args) {
        ST_BST<Integer, Integer> st = new ST_BST<>();
        st.add(8, 1);
        st.add(12, 1);
        st.add(17, 1);
        st.add(11, 1);
        st.add(15, -8);
        st.add(4, 1);
        st.add(1, 12);
        st.add(17, 3);
        System.out.println(st.value(15)); // -8
        System.out.println(st.value(1)); // 12
        System.out.println(st.value(2)); // null
        System.out.println(st.value(17)); // 3
        System.out.println(st.rank(15)); // 5
        System.out.println(st.rank(1)); // 0
        System.out.println(st.rank(12)); // 4
        System.out.println(st.rank(16)); // 6
        System.out.println(st.select(5)); // 15
        System.out.println(st.select(0)); // 1
        System.out.println(st.select(4)); // 12
        System.out.println(st.select(6)); // 17
        System.out.println(st.select(7)); // null
    }
}
