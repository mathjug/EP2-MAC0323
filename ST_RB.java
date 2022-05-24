public class ST_RB<Key extends Comparable<Key>, Item> implements ST<Key, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de uma Árvore Rubro Negra.
    */
    private NodeRB<Key, Item> raiz;

    public ST_RB() {
        raiz = null;
    }

    private int size(NodeRB<Key, Item> no) {
        if (no == null) {
            return 0;
        }
        return no.n_nos;
    }

    private void mudaCor(NodeRB<Key, Item> no) {
        if (no.eh_vermelho) {
            no.eh_vermelho = false;
            return;
        }
        no.eh_vermelho = true;
    }

    @Override
    public NodeRB<Key, Item> search(Key key) {
        /*
        Busca a chave dada como argumento em uma árvore rubro-negra. Se encontrar, retorna o nó.
        Caso contrário, retorna aquele que, se a chave estivesse na árvore, seria seu pai.
        */
        NodeRB<Key, Item> no_atual = raiz;
        NodeRB<Key, Item> anterior = null;
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

    private void update_N_nos(NodeRB<Key, Item> no) {
        /*
        Recebido um nó, incrementa em 1 a quantidade de nós na sub-árvore enraizada nele. Faz isso
        iterativamente, para todos acima e no caminho desse nó, até a raiz da árvore principal
        */
        while (no != null) {
            no.n_nos++;
            no = no.pai;
        }
    }

    private void rodaEsq(NodeRB<Key, Item> no) {
        /*
        Dado um nó, faz o movimento de rotacioná-lo para a esquerda. Deve mudar os ponteiros e n_nos.
        */
        NodeRB<Key, Item> d = no.direita;
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
    }

    private void rodaDir(NodeRB<Key, Item> no) {
        /*
        Dado um nó, faz o movimento de rotacioná-lo para a direita. Deve mudar os ponteiros e n_nos.
        */
        NodeRB<Key, Item> e = no.esquerda;
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
    }

    private NodeRB<Key, Item> addBST(Key key, Item val) {
        /*
        Insere, na tabela de símbolos, chave e valor dados como argumentos, pelo método de inserção
        em Árvores de Busca Binária. Colore o elemento inserido de vermelho. Retorna esse elemento
        (como nó).
        */
        if (raiz == null) { // TS está vazia
            raiz = new NodeRB<Key, Item>(key, val);
            return raiz;
        }
        NodeRB<Key, Item> no = search(key); // retorna o próprio nó ou o seu possível pai
        int cmp = key.compareTo(no.key);
        if (cmp > 0) {
            no.direita = new NodeRB<Key, Item>(key, val);
            no.direita.pai = no;
            update_N_nos(no);
            return no.direita;
        }
        else if (cmp < 0) {
            no.esquerda = new NodeRB<Key, Item>(key, val);
            no.esquerda.pai = no;
            update_N_nos(no);
            return no.esquerda;
        }
        else
            no.value = val;
            return no;
    }

    @Override
    public void add(Key key, Item val) {
        NodeRB<Key, Item> no = addBST(key, val);
        while (no != null && no.pai != null && no.pai.eh_vermelho) { // enquanto não alcançar a raiz e o pai for vermelho
            NodeRB<Key, Item> pai = no.pai; // não é necessário se preocupar com o pai ser raiz, pois, se fosse, seria preto
            NodeRB<Key, Item> avo = no.pai.pai; // é preto, pois pelo menos um de seus filhos é vermelho
            if (pai == avo.direita) { // pai está à direita do avô
                NodeRB<Key, Item> tio = avo.esquerda;
                if (tio != null && tio.eh_vermelho) { // tio vermelho
                    mudaCor(pai); mudaCor(avo); mudaCor(tio);
                }
                else if (no == pai.esquerda) { // tio preto (ou null) e nó, pai e avô em triângulo
                    rodaDir(pai);
                    no = no.direita; // o novo nó a ser analisado é o novo filho do nó anterior, após rodar
                    continue;
                }
                else { // tio preto (ou null) e nó, pai e avô em linha
                    rodaEsq(avo);
                    mudaCor(pai); mudaCor(avo);
                }
            }
            else { // pai está à esquerda do avô
                NodeRB<Key, Item> tio = avo.direita;
                if (tio != null && tio.eh_vermelho) {
                    mudaCor(pai); mudaCor(avo); mudaCor(tio);
                }
                else if (no == pai.direita) {
                    rodaEsq(pai);
                    no = no.esquerda;
                    continue;
                }
                else {
                    rodaDir(avo);
                    mudaCor(pai); mudaCor(avo);
                }
            }
            no = no.pai.pai; // o novo nó a ser analisado, agora, é o avô
        }
        if (no!= null && no.pai == null)
            no.eh_vermelho = false; // se chegou aqui, o nó analisado é raiz
    }

    @Override
    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não existir na árvore, retorna null.
        */
        NodeRB<Key, Item> no = search(key); // retorna o próprio nó ou o seu possível pai (que pode ser null)
        if (no != null)
            if (key.compareTo(no.key) == 0)
                return no.value;
        return null;
    }

    @Override
    public int rank(Key key) {
        /*
        Retorna o número de chaves menores do que a chave dada como argumento, por meio da busca binária.
        */
        return rank(raiz, key);
    }

    private int rank(NodeRB<Key, Item> no, Key key) {
        /*
        Método auxiliar para que rank possa fazer chamadas recursivas, tendo como raiz outros nós da
        árvore rubro-negra.
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
        Dado o valor do rank de um elemento da TS, retorna a sua chave. Caso o valor do rank supere
        a quantidade de nós na árvore, retorna null.
        */
        if (k >= size(raiz) || k < 0)
            return null;
        return select(raiz, k);
    }

    private Key select(NodeRB<Key, Item> no, int k) {
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
        ST_RB<Integer, Integer> st = new ST_RB<>();
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