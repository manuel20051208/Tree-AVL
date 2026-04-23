public class Nodo {
    private int valor;
    Nodo izquierda;
    Nodo derecha;
    int altura;

    public Nodo(int valor) {
        this.valor = valor;
        this.altura = 1;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("Node(%d) -> [L: %s | R: %s]",
                valor,
                (izquierda != null ? izquierda.getValor() : "null"),
                (derecha != null ? derecha.getValor() : "null")
        );
    }
}