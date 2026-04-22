import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArbolAVL extends ArbolBinario {

    private Nodo raiz;

    @Override
    public Nodo getRaiz() {
        return raiz;
    }

    public boolean isEmpty() {
        return raiz == null;
    }

    @Override
    public boolean insertar(int valor) {
        var insertado = new AtomicBoolean(false);
        raiz = insertar(raiz, valor, insertado);
        return insertado.get();
    }

    @Override
    public Optional<Nodo> buscar(int valor) {
        return buscar(raiz, valor);
    }

    public Optional<Nodo> eliminarAVL(int valor) {
        return buscar(valor).map(nodo -> {
            raiz = eliminarAVL(raiz, valor);
            return nodo;
        });
    }

    private Nodo insertar(Nodo actual, int valor, AtomicBoolean insertado) {
        if (actual == null) {
            insertado.set(true);
            return new Nodo(valor);
        }

        if (valor < actual.getValor()) actual.izquierda = insertar(actual.izquierda, valor, insertado);
        else if (valor > actual.getValor()) actual.derecha = insertar(actual.derecha, valor, insertado);
        else return actual;

        actualizarMeta(actual);
        return balancear(actual);
    }

    private Nodo eliminarAVL(Nodo actual, int valor) {
        if (actual == null) return null;

        if (valor < actual.getValor()) actual.izquierda = eliminarAVL(actual.izquierda, valor);
        else if (valor > actual.getValor()) actual.derecha = eliminarAVL(actual.derecha, valor);
        else {
            // Caso hoja o un solo hijo
            if (actual.izquierda == null || actual.derecha == null)
                return (actual.izquierda != null) ? actual.izquierda : actual.derecha;

            // Dos hijos: reemplazar con sucesor in-order
            var sucesor = sucesor(actual.derecha);
            actual.setValor(sucesor.getValor());
            actual.derecha = eliminarAVL(actual.derecha, sucesor.getValor());
        }

        actualizarMeta(actual);
        return balancear(actual);
    }

    private Nodo balancear(Nodo p) {
        if (p.fb > 1) return (p.izquierda.fb >= 0) ? rotarDerecha(p) : rotarDobleDerecha(p);
        if (p.fb < -1) return (p.derecha.fb <= 0) ? rotarIzquierda(p) : rotarDobleIzquierda(p);
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

    private Optional<Nodo> buscar(Nodo actual, int valor) {
        if (actual == null) return Optional.empty();

        return switch (Integer.compare(valor, actual.getValor())) {
            case 0 -> Optional.of(actual);
            case -1 -> buscar(actual.izquierda, valor);
            default -> buscar(actual.derecha, valor);
        };
    }
}