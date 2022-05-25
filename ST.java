public interface ST<Key extends Comparable<Key>, Item> {
    /*
    Interface de uma Tabela de Símbolos genérica.
    */
    Item search(Key key);
    void add(Key key, Item val);
    Item value(Key key);
    int rank(Key key);
    Key select(int k);
}