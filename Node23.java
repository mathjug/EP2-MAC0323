public class Node23<Key extends Comparable<Key>, Item> extends Node<Key, Item> {
    public Key key2;
    public Item value2;
    public boolean eh_2no;
    public Node23<Key, Item> direita;
    public Node23<Key, Item> esquerda;
    public Node23<Key, Item> centro;
    public Node23<Key, Item> pai;

    public Node23(Key key, Item value) { // gera 2-nó
        super(key, value);
        eh_2no = true;
        direita = null;
        esquerda = null;
        centro = null;
        pai = null;
    }

    public Node23(Key key1, Item value1, Key key2, Item value2) { // gera 3-nó
        super(key1, value1);
        this.key2 = key2;
        this.value2 = value2;
        eh_2no = false;
        direita = null;
        esquerda = null;
        centro = null;
        pai = null;
        n_nos = 2;
    }

    public Node23(Key key1, Item value1, Key key2, Item value2, Node23<Key, Item> esquerda, Node23<Key, Item> centro,
                    Node23<Key, Item> direita, Node23<Key, Item> pai, int n_nos) { // gera 3-nó
        super(key1, value1);
        this.key2 = key2;
        this.value2 = value2;
        eh_2no = false;
        this.direita = direita;
        this.esquerda = esquerda;
        this.centro = centro;
        this.pai = pai;
        this.n_nos = n_nos;
    }
}