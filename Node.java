public class Node<Key extends Comparable<Key>, Item> {
    /*
    Classe que implementa um nó genérico, com um único par chave-valor.
    */
    public Key key;
    public Item value;
    public int n_nos; // guarda a quantidade de nós em uma (sub-)árvore, contando com a raiz

    public Node(Key key, Item value) {
        this.key = key;
        this.value = value;
        n_nos = 1;
    }
}