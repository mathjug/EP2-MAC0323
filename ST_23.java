public class ST_23<Key extends Comparable<Key>, Item> implements ST<Key, Item> {
    /*
    Classe que implementa Tabela de Símbolos por meio de uma Árvore 2-3.
    */
    private Node23<Key, Item> raiz;

    private class FourNode extends Node23<Key, Item> {
        /*
        Classe que abstrai o conceito de um 4-nó, o qual contém 3 pares chave-valor e 4 filhos.
        */
        private Key key3;
        private Item value3;
        private Node23<Key, Item> centro2;

        private FourNode(Key key1, Key key2, Key key3, Item value1, Item value2, Item value3, Node23<Key, Item> filho1, 
                        Node23<Key, Item> filho2, Node23<Key, Item> filho3, Node23<Key, Item> filho4, Node23<Key, Item> pai, int n_nos) {
            super(key1, value1, key2, value2, filho1, filho2, filho4, pai, n_nos);
            this.key3 = key3;
            this.value3 = value3;
            centro2 = filho3;
        }
    }

    public ST_23() {
        raiz = null;
    }

    private int size(Node23<Key, Item> no) {
        if (no == null) {
            return 0;
        }
        return no.n_nos;
    }

    private void update_N_nos(Node23<Key, Item> no) {
        /*
        Recebido um nó, incrementa em 1 a quantidade de nós na sub-árvore enraizada nele. Faz isso
        iterativamente, para todos acima e no caminho desse nó, até a raiz da árvore principal.
        */
        while (no != null) {
            no.n_nos++;
            no = no.pai;
        }
    }

    private Node23<Key, Item> search23(Key key) {
        /*
        Busca a chave dada como argumento em uma árvore 2-3. Se encontrar, retorna o nó.
        Caso contrário, retorna o nó onde a chave deve ser inserida.
        */
        Node23<Key, Item> no_atual = raiz;
        Node23<Key, Item> anterior = null;
        while (no_atual != null) {
            anterior = no_atual;
            if (no_atual.eh_2no) {
                int cmp = key.compareTo(no_atual.key);
                if (cmp < 0) // chave a ser inserida é menor do que a chave atual
                    no_atual = no_atual.esquerda;
                else if (cmp > 0) // chave a ser inserida é maior do que a chave atual
                    no_atual = no_atual.direita;
                else // chave a ser inserida já está na árvore e foi encontrada
                    return no_atual;
            }
            else { // nó atual é 3-nó
                int cmp1 = key.compareTo(no_atual.key);
                if (cmp1 < 0) // chave a ser inserida é menor do que a primeira chave do nó atual 
                    no_atual = no_atual.esquerda;
                else if (cmp1 > 0) { // chave a ser inserida é maior do que a primeira
                    int cmp2 = key.compareTo(no_atual.key2);
                    if (cmp2 > 0) // chave a ser inserida é maior do que as duas chaves
                        no_atual = no_atual.direita;
                    else if (cmp2 < 0) // chave a ser inserida está no meio das duas
                        no_atual = no_atual.centro;
                    else // chave a ser inserida é igual à segunda do nó atual
                        return no_atual;
                }
                else // chave a ser inserida é igual à primeira do nó atual
                    return no_atual;
            }
        }
        return anterior; // chega aqui se a chave não existir na árvore
    }

    private Node23<Key, Item> split4node(FourNode no) {
        /*
        Dado um 4-nó temporário, executa o processo de fragmentação dele em três 2-nós. Retorna o
        nó de chave intermediária.
        */
        if (no == null)
            return null;
        
        Node23<Key, Item> menor = new Node23<Key, Item>(no.key, no.value);
        Node23<Key, Item> meio = new Node23<Key, Item>(no.key2, no.value2);
        Node23<Key, Item> maior = new Node23<Key, Item>(no.key3, no.value3);
        menor.n_nos = size(no.esquerda) + size(no.centro) + 1;
        maior.n_nos = size(no.centro2) + size(no.direita) + 1;
        meio.n_nos = size(menor) + size(maior) + 1;

        menor.pai = meio;
        maior.pai = meio;
        
        if (no.esquerda != null) {
            menor.esquerda = no.esquerda;
            no.esquerda.pai = menor;
        }
        if (no.centro != null) {
            menor.direita = no.centro;
            no.centro.pai = menor;
        }
        if (no.centro2 != null) {
            maior.esquerda = no.centro2;
            no.centro2.pai = maior;
        }
        if (no.direita != null) {
            maior.direita = no.direita;
            no.direita.pai = maior;
        }

        meio.esquerda = menor;
        meio.direita = maior;
        menor.pai = meio;
        maior.pai = meio;
        meio.pai = null; // trata quando o do meio se torna raiz
        return meio;
    }

    private void add(Node23<Key, Item> novo, Node23<Key, Item> velho) {
        /*
        Método privativo auxiliar para inserção na árvore 2-3. Atua de maneira recursiva, até que a
        inserção seja concluida com êxito e todas as chaves sejam posicionadas corretamente.
        */
        if (novo == null || velho == null)
            return;
        Key key = novo.key;
        Item val = novo.value;
        int cmp1 = key.compareTo(velho.key);
        if (velho.eh_2no) { // adicionando em um 2-nó
            if (cmp1 == 0) { // chave a ser inserida já está no nó
                velho.value = val;
                return;
            }
            else if (cmp1 < 0) { // chave a ser inserida é menor do que a que está no nó
                velho.key2 = velho.key;
                velho.value2 = velho.value;
                velho.key = key;
                velho.value = val;
                velho.eh_2no = false;
                if (novo.esquerda != null) {
                    velho.direita = velho.esquerda;
                    velho.esquerda = novo.esquerda;
                    velho.centro = novo.direita;
                }
                update_N_nos(velho);
                return;
            }
            else { // chave a ser inserida é maior do que a que está no nó
                velho.key2 = key;
                velho.value2 = val;
                velho.eh_2no = false;
                if (novo.esquerda != null) {
                    velho.centro = novo.esquerda;
                    velho.direita = novo.direita;
                }
                update_N_nos(velho);
                return;
            }
        }
        else { // adicionando em um 3-nó
            int cmp2 = key.compareTo(velho.key2);
            if (cmp1 < 0) { // chave a ser inserida é menor do que as duas
                FourNode temporario = new FourNode(key, velho.key, velho.key2, val, velho.value, velho.value2, 
                                                    novo.esquerda, novo.direita, velho.centro, velho.direita, velho.pai, velho.n_nos + 1);
                if (velho.pai == null) { // inserindo em raiz
                    raiz = split4node(temporario);
                    return;
                }
                else { // não está inserindo na raiz
                    if (velho == velho.pai.esquerda)
                        velho.pai.esquerda = temporario;
                    else if (velho == velho.pai.direita)
                        velho.pai.direita = temporario;
                    else
                        velho.pai.centro = temporario;
                    // filhos de temporario ainda não têm ele como pai, mas isso muda chamando-se split4node()
                    Node23<Key, Item> que_subiu = split4node(temporario);
                    add(que_subiu, velho.pai);
                }                    
            }
            else if (cmp2 > 0) { // chave a ser inserida é maior do que as duas
                FourNode temporario = new FourNode(velho.key, velho.key2, key, velho.value, velho.value2, val,
                                                    velho.esquerda, velho.centro, novo.esquerda, novo.direita, velho.pai, velho.n_nos + 1);
                if (velho.pai == null) { // nó é raiz
                    raiz = split4node(temporario);
                    return;
                }
                else { // nó não é raiz
                    if (velho == velho.pai.esquerda)
                            velho.pai.esquerda = temporario;
                    else if (velho == velho.pai.direita)
                        velho.pai.direita = temporario;
                    else
                        velho.pai.centro = temporario;
                    Node23<Key, Item> que_subiu = split4node(temporario);
                    add(que_subiu, velho.pai);
                }        
            }
            else { // chave a ser inserida está entre as duas ou é igual a alguma
                if (cmp1 == 0) { // chave a ser inserida é igual à primeira
                    velho.value = val;
                    return;
                }
                else if (cmp2 == 0) { // chave a ser inserida é igual à segunda
                    velho.value2 = val;
                    return;
                }
                else { // chave a ser inserida está entre as duas
                    FourNode temporario = new FourNode(velho.key, key, velho.key2, velho.value, val, velho.value2,
                                                    velho.esquerda, novo.esquerda, novo.direita, velho.direita, velho.pai, velho.n_nos + 1);
                    if (velho.pai == null) { // nó é raiz
                        raiz = split4node(temporario); // o nó retornado por split4node() tem pai == null
                        return;
                    }
                    else { // nó não é raiz
                        if (velho == velho.pai.esquerda)
                            velho.pai.esquerda = temporario;
                        else if (velho == velho.pai.direita)
                            velho.pai.direita = temporario;
                        else
                            velho.pai.centro = temporario;
                        Node23<Key, Item> que_subiu = split4node(temporario);
                        add(que_subiu, velho.pai);
                    }   
                }
            }
            
        }
    }

    @Override
    public Item search(Key key) {
        /*
        Busca a chave dada como argumento na Árvore 2-3. Se encontrar, retorna o seu valor.
        Caso contrário, retorna null.
        */
        Node23<Key, Item> no = search23(key);
        if (no != null) {
            if (key.equals(no.key))
                return no.value;
            if (key.equals(no.key2))
                return no.value2;
        }
        return null;
    }

    @Override
    public void add(Key key, Item val) {
        /*
        Insere, na árvore 2-3, chave e valor dados como argumentos.
        */
        Node23<Key, Item> novo = new Node23<Key, Item>(key, val);
        if (raiz == null) { // árvore está vazia
            raiz = novo;
            return;
        }
        Node23<Key, Item> velho = search23(key); // retorna nó onde já está a chave ou onde ela deve ser inserida
        add(novo, velho);
    }

    @Override
    public Item value(Key key) {
        /*
        Dada uma chave, retorna o valor correspondente a ela. Se ela não existir na árvore 2-3, retorna null.
        */
        Node23<Key, Item> no = search23(key); // retorna o próprio nó ou o seu possível pai (que pode ser null)
        if (no != null) {
            int cmp1 = key.compareTo(no.key);
            if (cmp1 == 0)
                return no.value;
            if (!no.eh_2no)
                if (key.compareTo(no.key2) == 0)
                    return no.value2;
        }
        return null;
    }

    @Override
    public int rank(Key key) {
        /*
        Retorna o número de chaves na árvore 2-3 menores do que a chave dada como argumento.
        */
        return rank(raiz, key);
    }

    private int rank(Node23<Key, Item> no, Key key) {
        /*
        Método auxiliar para que rank possa fazer chamadas recursivas, tendo como raiz outros nós da
        árvore 2-3.
        */
        if (no == null)
            return 0;
        if (no.eh_2no) {
            int cmp = key.compareTo(no.key);
            if (cmp < 0)
                return rank(no.esquerda, key);
            else if (cmp > 0)
                return size(no.esquerda) + 1 + rank(no.direita, key);
            else
                return size(no.esquerda);
        }
        else { // nó é 3-nó
            int cmp1 = key.compareTo(no.key);
            int cmp2 = key.compareTo(no.key2);
            if (cmp1 < 0) // chave é menor do que ambas as chaves do nó atual
                return rank(no.esquerda, key);
            else if (cmp1 > 0 && cmp2 < 0) // chave está entre as duas
                return size(no.esquerda) + rank(no.centro, key) + 1;
            else if (cmp2 > 0) // chave é maior que ambas
                return size(no.esquerda) + size(no.centro) + 2 + rank(no.direita, key);
            else {
                if (cmp1 == 0) // chave é igual à primeira
                    return size(no.esquerda);
                else // chave é igual à segunda
                    return size(no.esquerda) + size(no.centro) + 1;
            }
        }
    }

    @Override
    public Key select(int k) {
        /*
        Dado um valor de rank, retorna a sua chave. Caso o valor do rank supere a quantidade de nós
        na árvore, retorna null.
        */
        if (k >= raiz.n_nos || k < 0)
            return null;
        return select(raiz, k);
    }

    private Key select(Node23<Key, Item> no, int k) {
        /*
        Método auxiliar para que select possa fazer chamadas recursivas, tendo como raiz outros nós da
        árvore 2-3.
        */
        int tam_esquerda = size(no.esquerda);
        if (no.eh_2no) {
            if (tam_esquerda == k)
                return no.key;
            else if (tam_esquerda > k)
                return select(no.esquerda, k);
            else
                return select(no.direita, k - tam_esquerda - 1);
        }
        else {
            int tam_centro = size(no.centro);
            if (k < tam_esquerda)
                return select(no.esquerda, k);
            else if (k == tam_esquerda)
                return no.key;
            else if (k < tam_esquerda + tam_centro + 1)
                return select(no.centro, k - tam_esquerda - 1);
            else if (k == tam_esquerda + tam_centro + 1)
                return no.key2;
            else // k > tam_esquerda + tam_centro + 1
                return select(no.direita, k - tam_esquerda - tam_centro - 2);
        }
    }

    public static void main (String[] args) {
        ST_23<Integer, Integer> st = new ST_23<>();
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