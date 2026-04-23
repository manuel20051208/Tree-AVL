import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

interface Arbol<T extends Nodo> {

    enum Recorrido {
        PREORDEN,
        INORDEN,
        POSORDEN
    }

    Nodo getRaiz();

    boolean isEmpty();

    boolean insertar(int valor);

    void recorrer(Enum<Recorrido> tipo);

    T insertar(T actual, int valor, AtomicBoolean insertado);

    void actualizarMeta(T nodo);

    int altura(T nodo);

    void preorden(T actual);

    void inorden(T actual);

    void posorden(T actual);

    Optional<T> buscar(int valor);

    Optional<T> buscar(T actual, int valor);

    Optional<T> eliminar(int numero);

    T eliminar(T actual, int valor);

    T predecesor(T raiz_actual);

    T sucesor(T raiz_actual);
}