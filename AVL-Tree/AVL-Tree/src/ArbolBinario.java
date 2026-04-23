import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArbolBinario implements Arbol<Nodo> {
    private Nodo raiz;

    @Override
    public Nodo getRaiz() {
        return raiz;
    }

    @Override
    public boolean isEmpty() {
        return raiz == null;
    }

    public boolean insertar(int valor) {
        var insertado = new AtomicBoolean(false);
        raiz = insertar(raiz, valor, insertado);
        return insertado.get();
    }

    @Override
    public void recorrer(Enum<Recorrido> tipo) {
        System.out.printf("─── %s ───%n", tipo);
        switch (tipo) {
            case Recorrido.PREORDEN -> preorden(raiz);
            case Recorrido.INORDEN -> inorden(raiz);
            case Recorrido.POSORDEN -> posorden(raiz);
            default -> JOptionPane.showMessageDialog(null, "Error, opcion invalida");
        }
    }

    @Override
    public Nodo insertar(Nodo actual, int valor, AtomicBoolean insertado) {
        if (actual == null) {
            insertado.set(true);
            return new Nodo(valor);
        }

        if (valor < actual.getValor()) {
            actual.izquierda = insertar(actual.izquierda, valor, insertado);
        } else if (valor > actual.getValor()) {
            actual.derecha = insertar(actual.derecha, valor, insertado);
        } else {
            return actual;
        }

        return actual;
    }

    @Override
    public void actualizarMeta(Nodo nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izquierda), altura(nodo.derecha));
    }

    @Override
    public int altura(Nodo nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    @Override
    public void preorden(Nodo actual) {
        if (actual == null) return;
        System.out.println(actual);
        preorden(actual.izquierda);
        preorden(actual.derecha);
    }

    @Override
    public void inorden(Nodo actual) {
        if (actual == null) return;
        inorden(actual.izquierda);
        System.out.println(actual);
        inorden(actual.derecha);
    }

    @Override
    public void posorden(Nodo actual) {
        if (actual == null) return;
        posorden(actual.izquierda);
        posorden(actual.derecha);
        System.out.println(actual);
    }

    public Optional<Nodo> buscar(int valor) {
        return buscar(raiz, valor);
    }

    @Override
    public Optional<Nodo> buscar(Nodo actual, int valor) {
        if (actual == null) return Optional.empty();

        return switch (Integer.compare(valor, actual.getValor())) {
            case 0 -> Optional.of(actual);
            case -1 -> buscar(actual.izquierda, valor);
            default -> buscar(actual.derecha, valor);
        };
    }

    @Override
    public Optional<Nodo> eliminar(int numero) {
        Nodo nodoEliminar;
        if (buscar(numero).isPresent()) {
            nodoEliminar = buscar(numero).get();
            return Optional.of(eliminar(nodoEliminar, numero));
        }
        return Optional.empty();
    }

    @Override
    public Nodo eliminar(Nodo actual, int valor) {
        if (actual == null) return null;

        if (valor < actual.getValor()) {
            actual.izquierda = eliminar(actual.izquierda, valor);
        } else if (valor > actual.getValor()) {
            actual.derecha = eliminar(actual.derecha, valor);
        } else {
            if (actual.izquierda == null || actual.derecha == null) {
                actual = (actual.izquierda != null) ? actual.izquierda : actual.derecha;
            } else {
                Nodo temp = sucesor(actual.derecha);
                actual.setValor(temp.getValor());
                actual.derecha = eliminar(actual.derecha, temp.getValor());
            }
        }

        return actual;
    }

    @Override
    public Nodo predecesor(Nodo raiz_actual) {
        if (raiz_actual.derecha == null) {
            return raiz_actual;
        }
        return predecesor(raiz_actual.derecha);
    }

    @Override
    public Nodo sucesor(Nodo raiz_actual) {
        if (raiz_actual.izquierda == null) {
            return raiz_actual;
        }
        return sucesor(raiz_actual.izquierda);
    }
}