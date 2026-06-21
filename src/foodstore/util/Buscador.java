package foodstore.util;

import foodstore.interfaces.Identificable;
import java.util.Collection;
import java.util.Iterator;

public class Buscador<T extends Identificable<K>, K> {

    public T buscar(Collection<? extends T> elementos, K id) {
        if (elementos == null || id == null) {
            return null;
        }

        T encontrado = null;
        Iterator<? extends T> iterator = elementos.iterator();

        while (iterator.hasNext() && encontrado == null) {
            T actual = iterator.next();
            if (actual != null && actual.sameId(id)) {
                encontrado = actual;
            }
        }

        return encontrado;
    }
}
