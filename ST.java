public interface ST<Key extends Comparable<Key>, Item> {
    /*
    Classe que implementa um Tabela de Símbolos genérica.
    */

    Node<Key, Item> search(Key key);
    void add(Key key, Item val);
    Item value(Key key);
    int rank(Key key);
    Key select(int k);
}