public class NodeRB<Key extends Comparable<Key>, Item> extends Node<Key, Item> {
    public boolean eh_vermelho;
    public NodeRB<Key, Item> direita;
    public NodeRB<Key, Item> esquerda;
    public NodeRB<Key, Item> pai;

    public NodeRB(Key key, Item value) {
        super(key, value);
        eh_vermelho = true;
        direita = null;
        esquerda = null;
        pai = null;
    }
}