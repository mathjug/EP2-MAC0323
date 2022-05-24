import java.util.*; // para utilizar a biblioteca Random

public class NodeTreap<Key extends Comparable<Key>, Item> extends Node<Key, Item> {
    public int prioridade;
    public NodeTreap<Key, Item> direita;
    public NodeTreap<Key, Item> esquerda;
    public NodeTreap<Key, Item> pai;

    public NodeTreap(Key key, Item value, int n_palavras) {
        super(key, value);
        prioridade = new Random().nextInt(2 * n_palavras);
        direita = null;
        esquerda = null;
        pai = null;
    }
}