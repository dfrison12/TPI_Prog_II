package foodstore.interfaces;

public interface Identificable<K> {

    K getId();

    boolean sameId(K id);
}
