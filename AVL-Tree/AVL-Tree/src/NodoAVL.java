public class NodoAVL extends Nodo {
    NodoAVL izquierda;
    NodoAVL derecha;
    int fb;
    int altura;

    public NodoAVL(int valor) {
        super(valor);
        this.altura = 1;
    }

    @Override
    public String toString() {
        return String.format("Nodo(%d) -> [ Alt: %d, FB: %d ] [ L: %s, R: %s ]",
                getValor(),
                altura,
                fb,
                (izquierda != null ? izquierda.getValor() : "null"),
                (derecha != null ? derecha.getValor() : "null")
        );
    }
}