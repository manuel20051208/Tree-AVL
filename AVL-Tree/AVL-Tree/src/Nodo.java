public class Nodo {
    private int valor;
    Nodo izquierda;
    Nodo derecha;
    int altura;
    int fb;

    public Nodo(int valor) {
        this.valor = valor;
        this.altura = 1;
    }

    public int getValor() { return valor; }

    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Nodo(%d | h=%d | fb=%d)".formatted(valor, altura, fb);
    }
}