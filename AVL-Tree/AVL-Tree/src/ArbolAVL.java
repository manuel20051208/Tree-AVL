import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArbolAVL implements Arbol<NodoAVL> {

    private NodoAVL raiz;

    @Override
    public NodoAVL getRaiz() {
        return raiz;
    }

    @Override
    public boolean isEmpty() {
        return raiz == null;
    }

    @Override
    public void recorrer(Enum<Recorrido> tipo) {
        System.out.printf("─── %s ───%n", tipo);
        switch (tipo) {
            case Recorrido.PREORDEN -> preorden(raiz);
            case Recorrido.INORDEN -> inorden(raiz);
            case Recorrido.POSORDEN -> posorden(raiz);
            default -> JOptionPane.showMessageDialog(null, "Opción invalida");
        }
    }

    @Override
    public boolean insertar(int valor) {
        var insertado = new AtomicBoolean(false);
        raiz = insertar(raiz, valor, insertado);
        return insertado.get();
    }

    @Override
    public Optional<NodoAVL> buscar(int valor) {
        return buscar(raiz, valor);
    }

    @Override
    public Optional<NodoAVL> buscar(NodoAVL actual, int valor) {
        if (actual == null) return Optional.empty();

        return switch (Integer.compare(valor, actual.getValor())) {
            case 0 -> Optional.of(actual);
            case -1 -> buscar(actual.izquierda, valor);
            default -> buscar(actual.derecha, valor);
        };
    }

    public Optional<NodoAVL> eliminar(int valor) {
        return buscar(valor).map(nodoAVL -> {
            raiz = eliminar(raiz, valor);
            return nodoAVL;
        });
    }

    public NodoAVL insertar(NodoAVL actual, int valor, AtomicBoolean insertado) {
        if (actual == null) {
            insertado.set(true);
            return new NodoAVL(valor);
        }

        if (valor < actual.getValor()) actual.izquierda = insertar(actual.izquierda, valor, insertado);
        else if (valor > actual.getValor()) actual.derecha = insertar(actual.derecha, valor, insertado);
        else return actual;

        actualizarMeta(actual);
        return balancear(actual);
    }

    public NodoAVL eliminar(NodoAVL actual, int valor) {
        if (actual == null) return null;

        if (valor < actual.getValor()) actual.izquierda = eliminar(actual.izquierda, valor);
        else if (valor > actual.getValor()) actual.derecha = eliminar(actual.derecha, valor);
        else {
            // Caso hoja o un solo hijo
            if (actual.izquierda == null || actual.derecha == null)
                return (actual.izquierda != null) ? actual.izquierda : actual.derecha;

            // Dos hijos: reemplazar con sucesor in-order
            var sucesor = sucesor(actual.derecha);
            actual.setValor(sucesor.getValor());
            actual.derecha = eliminar(actual.derecha, sucesor.getValor());
        }

        actualizarMeta(actual);
        return balancear(actual);
    }

    @Override
    public NodoAVL predecesor(NodoAVL raiz_actual) {
        return null;
    }

    @Override
    public NodoAVL sucesor(NodoAVL raiz_actual) {
        return null;
    }

    private NodoAVL balancear(NodoAVL p) {
        if (p.fb > 1) return (p.izquierda.fb >= 0) ? rotarDerecha(p) : rotarDobleDerecha(p);
        if (p.fb < -1) return (p.derecha.fb <= 0) ? rotarIzquierda(p) : rotarDobleIzquierda(p);
        return p;
    }

    private NodoAVL rotarDerecha(NodoAVL p) {
        var q = p.izquierda;
        p.izquierda = q.derecha;
        q.derecha = p;
        actualizarMeta(p);
        actualizarMeta(q);
        return q;
    }

    private NodoAVL rotarIzquierda(NodoAVL p) {
        var q = p.derecha;
        p.derecha = q.izquierda;
        q.izquierda = p;
        actualizarMeta(p);
        actualizarMeta(q);
        return q;
    }

    private NodoAVL rotarDobleDerecha(NodoAVL p) {
        p.izquierda = rotarIzquierda(p.izquierda);
        return rotarDerecha(p);
    }

    private NodoAVL rotarDobleIzquierda(NodoAVL p) {
        p.derecha = rotarDerecha(p.derecha);
        return rotarIzquierda(p);
    }

    public void actualizarMeta(NodoAVL nodoAVL) {
        nodoAVL.altura = 1 + Math.max(altura(nodoAVL.izquierda), altura(nodoAVL.derecha));
        nodoAVL.fb = altura(nodoAVL.izquierda) - altura(nodoAVL.derecha);
    }

    @Override
    public int altura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    @Override
    public void preorden(NodoAVL actual) {
        if (actual == null) return;
        System.out.println(actual);
        preorden(actual.izquierda);
        preorden(actual.derecha);
    }

    @Override
    public void inorden(NodoAVL actual) {
        if (actual == null) return;
        inorden(actual.izquierda);
        System.out.println(actual);
        inorden(actual.derecha);
    }

    @Override
    public void posorden(NodoAVL actual) {
        if (actual == null) return;
        posorden(actual.izquierda);
        posorden(actual.derecha);
        System.out.println(actual);
    }
}