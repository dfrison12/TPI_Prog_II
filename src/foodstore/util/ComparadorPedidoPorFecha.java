package foodstore.util;

import foodstore.entities.Pedido;
import java.util.Comparator;

public class ComparadorPedidoPorFecha implements Comparator<Pedido> {

    @Override
    public int compare(Pedido primerPedido, Pedido segundoPedido) {
        if (primerPedido == null && segundoPedido == null) {
            return 0;
        }
        if (primerPedido == null) {
            return 1;
        }
        if (segundoPedido == null) {
            return -1;
        }
        return primerPedido.getFecha().compareTo(segundoPedido.getFecha());
    }
}
