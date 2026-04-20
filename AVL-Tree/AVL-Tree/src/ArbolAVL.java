import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArbolAVL {

    public enum Recorrido {PREORDEN, INORDEN, POSORDEN}

    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    public boolean isEmpty() {
        return raiz == null;
    }

    public boolean insertar(int valor) {
        var insertado = new AtomicBoolean(false);
        raiz = insertar(raiz, valor, insertado);
        return insertado.get();
    }

    public Optional<Nodo> buscar(int valor) {
        return buscar(raiz, valor);
    }

    public void recorrer(Recorrido tipo) {
        System.out.printf("─── %s ───%n", tipo);
        switch (tipo) {
            case PREORDEN -> preorden(raiz);
            case INORDEN -> inorden(raiz);
            case POSORDEN -> posorden(raiz);
        }
    }

    private Nodo insertar(Nodo actual, int valor, AtomicBoolean insertado) {
        if (actual == null) {
            insertado.set(true);
            return new Nodo(valor);
        }

        if (valor < actual.getValor()) {
            actual.izquierda = insertar(actual.izquierda, valor, insertado);
        } else if (valor > actual.getValor()) {
            actual.derecha = insertar(actual.derecha, valor, insertado);
        } else {
            return actual; // duplicado, no se inserta
        }

        actualizarMeta(actual);
        return balancear(actual);
    }

    private Nodo balancear(Nodo p) {
        if (p.fb > 1) {
            var q = p.izquierda;
            return (q.fb >= 0)
                    ? rotarDerecha(p)         // Simple derecha
                    : rotarDobleDerecha(p);   // Doble derecha
        }

        if (p.fb < -1) {
            var q = p.derecha;
            return (q.fb <= 0)
                    ? rotarIzquierda(p)       // Simple izquierda
                    : rotarDobleIzquierda(p); // Doble izquierda
        }

        return p;
    }

    private Nodo rotarDerecha(Nodo p) {
        var q = p.izquierda;
        p.izquierda = q.derecha;
        q.derecha = p;
        actualizarMeta(p);
        actualizarMeta(q);
        return q;
    }

    private Nodo rotarIzquierda(Nodo p) {
        var q = p.derecha;
        p.derecha = q.izquierda;
        q.izquierda = p;
        actualizarMeta(p);
        actualizarMeta(q);
        return q;
    }

    private Nodo rotarDobleDerecha(Nodo p) {
        p.izquierda = rotarIzquierda(p.izquierda);
        return rotarDerecha(p);
    }

    private Nodo rotarDobleIzquierda(Nodo p) {
        p.derecha = rotarDerecha(p.derecha);
        return rotarIzquierda(p);
    }

    private void actualizarMeta(Nodo nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izquierda), altura(nodo.derecha));
        nodo.fb = altura(nodo.izquierda) - altura(nodo.derecha);
    }

    private int altura(Nodo nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private void preorden(Nodo actual) {
        if (actual == null) return;
        System.out.println(actual);
        preorden(actual.izquierda);
        preorden(actual.derecha);
    }

    private void inorden(Nodo actual) {
        if (actual == null) return;
        inorden(actual.izquierda);
        System.out.println(actual);
        inorden(actual.derecha);
    }

    private void posorden(Nodo actual) {
        if (actual == null) return;
        posorden(actual.izquierda);
        posorden(actual.derecha);
        System.out.println(actual);
    }

    private Optional<Nodo> buscar(Nodo actual, int valor) {
        if (actual == null) return Optional.empty();

        return switch (Integer.compare(valor, actual.getValor())) {
            case 0 -> Optional.of(actual);
            case -1 -> buscar(actual.izquierda, valor);
            default -> buscar(actual.derecha, valor);
        };
    }

    public Optional<Nodo> eliminar(int numero) {
        Nodo nodoEliminar;
        if (buscar(numero).isPresent()) {
            nodoEliminar = buscar(numero).get();
            return Optional.of(eliminar(nodoEliminar, numero));
        }
        return Optional.empty();
    }

    private Nodo eliminar(Nodo actual, int valor) {
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

        if (actual == null) return null;

        actualizarMeta(actual);
        return balancear(actual);
    }

    public Nodo predecesor(Nodo raiz_actual) {
        if (raiz_actual.derecha == null) {
            return raiz_actual;
        }
        return predecesor(raiz_actual.derecha);
    }

    public Nodo sucesor(Nodo raiz_actual) {
        if (raiz_actual.izquierda == null) {
            return raiz_actual;
        }
        return sucesor(raiz_actual.izquierda);
    }
}