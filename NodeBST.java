public class NodeBST<Key extends Comparable<Key>, Item> extends Node<Key, Item> {
    public NodeBST<Key, Item> direita;
    public NodeBST<Key, Item> esquerda;
    public NodeBST<Key, Item> pai;

    public NodeBST(Key key, Item value) {
        super(key, value);
        direita = null;
        esquerda = null;
        pai = null;
    }
}