public class ST_Treap<Key extends Comparable<Key>, Item> implements ST<Key, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de uma Treap. Os seus métodos value, rank e select
    são os mesmos de uma implementação que utiliza Árvores de Busca Binária não aleatorizadas.
    */
    private NodeTreap<Key, Item> raiz;
    private int n_palavras;

    public ST_Treap(int n_palavras) {
        raiz = null;
        this.n_palavras = n_palavras;
    }

    private int size(NodeTreap<Key, Item> no) {
        if (no == null) {
            return 0;
        }
        return no.n_nos;
    }

    private NodeTreap<Key, Item> searchTR(Key key) {
        /*
        Busca, na Treap, a chave dada como argumento. Se encontrar, retorna o nó. Caso contrário,
        retorna aquele que, se a chave estivesse na árvore, seria seu pai.
        */
        NodeTreap<Key, Item> no_atual = raiz;
        NodeTreap<Key, Item> anterior = null;
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

    private void update_N_nos(NodeTreap<Key, Item> no) {
        /*
        Recebido um nó, incrementa em 1 a quantidade de nós na sub-árvore enraizada nele. Faz isso
        iterativamente, para todos acima e no caminho desse nó, até a raiz da árvore principal.
        */
        while (no != null) {
            no.n_nos++;
            no = no.pai;
        }
    }

    private NodeTreap<Key, Item> rodaEsq(NodeTreap<Key, Item> no) {
        /*
        Dado um nó, faz o movimento de rotacioná-lo para a esquerda. Deve mudar os ponteiros e n_nos.
        */
        NodeTreap<Key, Item> d = no.direita;
        if (d != null) {
            d.n_nos = no.n_nos;
            if (d.direita != null)
                no.n_nos = no.n_nos - d.direita.n_nos - 1;
            else
                no.n_nos -= 1;
            d.pai = no.pai;
            if (no.pai != null) {
                if (no.pai.direita == no)
                    no.pai.direita = d;
                else
                    no.pai.esquerda = d;
            }
            no.pai = d;
            no.direita = d.esquerda;
            if (d.esquerda != null)
                d.esquerda.pai = no;
            d.esquerda = no;
            if (no == raiz)
                raiz = d;
        }
        return d;
    }

    private NodeTreap<Key, Item> rodaDir(NodeTreap<Key, Item> no) {
        /*
        Dado um nó, faz o movimento de rotacioná-lo para a direita. Deve mudar os ponteiros e n_nos.
        */
        NodeTreap<Key, Item> e = no.esquerda;
        if (e != null) {
            e.n_nos = no.n_nos;
            if (e.esquerda != null)
                no.n_nos = no.n_nos - e.esquerda.n_nos - 1;
            else
                no.n_nos -= 1;
            e.pai = no.pai;
            if (no.pai != null) {
                if (no.pai.direita == no)
                    no.pai.direita = e;
                else
                    no.pai.esquerda = e;
            }
            no.pai = e;
            no.esquerda = e.direita;
            if (e.direita != null)
                e.direita.pai = no;
            e.direita = no;
            if (no == raiz)
                raiz = e;
        }
        return e;
    }

    private NodeTreap<Key, Item> addBST(Key key, Item val) {
        /*
        Executa o método de inserção, em uma árvore de busca binária, de chave e valor dados como argumentos.
        Retorna esse elemento (como nó).
        */
        if (raiz == null) { // TS está vazia
            raiz = new NodeTreap<Key, Item>(key, val, n_palavras);
            return raiz;
        }
        NodeTreap<Key, Item> no = searchTR(key); // retorna o próprio nó ou o seu possível pai
        int cmp = key.compareTo(no.key);
        if (cmp > 0) {
            no.direita = new NodeTreap<Key, Item>(key, val, n_palavras);
            no.direita.pai = no;
            update_N_nos(no);
            return no.direita;
        }
        else if (cmp < 0) {
            no.esquerda = new NodeTreap<Key, Item>(key, val, n_palavras);
            no.esquerda.pai = no;
            update_N_nos(no);
            return no.esquerda;
        }
        else
            no.value = val;
            return no;
    }

    @Override
    public Item search(Key key) {
        /*
        Busca a chave dada como argumento na Treap. Se encontrar, retorna o seu valor.
        Caso contrário, retorna null.
        */
        NodeTreap<Key, Item> no = searchTR(key);
        if (no != null && key.equals(no.key))
            return no.value;
        return null;
    }

    @Override
    public void add(Key key, Item val) {
        /*
        Insere, na Treap, chave e valor dados como argumentos.
        */
        NodeTreap<Key, Item> atual = addBST(key, val);
        while (atual != null && atual.pai != null) {
            if (atual == atual.pai.esquerda && atual.prioridade > atual.pai.prioridade)
                atual = rodaDir(atual.pai);
            else if (atual == atual.pai.direita && atual.prioridade > atual.pai.prioridade)
                atual = rodaEsq(atual.pai);
            else // aqui, filho tem prioridade não maior que a do pai
                return;
        }
    }

    @Override
    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não existir na árvore, retorna null.
        */
        NodeTreap<Key, Item> no = searchTR(key); // retorna o próprio nó ou o seu possível pai (que pode ser null)
        if (no != null)
            if (key.compareTo(no.key) == 0)
                return no.value;
        return null;
    }

    @Override
    public int rank(Key key) {
        /*
        Retorna o número de chaves menores do que a chave dada como argumento.
        */
        return rank(raiz, key);
    }

    private int rank(NodeTreap<Key, Item> no, Key key) {
        /*
        Método auxiliar para que rank possa fazer chamadas recursivas, tendo como raiz outros nós da
        Treap.
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

    @Override
    public Key select(int k) {
        /*
        Dado o valor do rank de um elemento da Treap, retorna a sua chave. Caso o valor do rank supere
        a quantidade de nós na árvore, retorna null.
        */
        if (k >= size(raiz) || k < 0)
            return null;
        return select(raiz, k);
    }

    private Key select(NodeTreap<Key, Item> no, int k) {
        /*
        Método auxiliar para que select possa fazer chamadas recursivas, tendo como raiz outros nós da
        Treap.
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
        ST_Treap<Integer, Integer> st = new ST_Treap<>(40);
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