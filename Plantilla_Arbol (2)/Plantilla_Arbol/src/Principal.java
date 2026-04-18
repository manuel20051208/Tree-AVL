import javax.swing.JOptionPane;
import java.util.function.IntPredicate;

public class Principal {

    private final ArbolAVL arbol = new ArbolAVL();

    public static void main(String[] args) {
        new Principal().probar();
        new Principal().iniciar();
    }

    private void probar() {
        arbol.insertar(10);   // antes: ingresar_nodo
        arbol.insertar(4);
        arbol.insertar(7);
        arbol.insertar(16);
        arbol.insertar(19);
        arbol.insertar(11);
    }

    private void iniciar() {
        int op;
        do {
            op = pedirOpcion("""
                    Tree search AVL
                    ─────────────────────────
                    1. Input element
                    2. Recorrer tree
                    3. Search for an element
                    4. Delete an element
                    5. Operation
                    6. Leave
                    """);

            switch (op) {
                case 1 -> insertarElemento();
                case 2 -> menuRecorridos();
                case 3 -> buscarElemento();
                case 4 -> eliminarElemento();
                case 5 -> mostrarOperaciones();
                case 6 -> mostrar("Saliendo...");
                default -> mostrar("Opción inválida.");
            }
        } while (op != 6);
    }

    private void insertarElemento() {
        int num = pedirNumero("Ingresa un valor:");
        var mensaje = arbol.insertar(num)           // ← antes: ingresar_nodo
                ? "Dato agregado: " + num
                : "El dato " + num + " ya existe.";
        mostrar(mensaje);
    }

    private void menuRecorridos() {
        // Mapeo int → enum para no tocar el menú
        var tipos = new ArbolAVL.Recorrido[]{
                null,
                ArbolAVL.Recorrido.PREORDEN,
                ArbolAVL.Recorrido.INORDEN,
                ArbolAVL.Recorrido.POSORDEN
        };

        int op;
        do {
            op = pedirOpcion("""
                    Tipo de recorrido
                    ─────────────────────
                    1. Preorden
                    2. Inorden
                    3. Posorden
                    4. Volver
                    """);

            if (op >= 1 && op <= 3) arbol.recorrer(tipos[op]);  // ← antes: recorridos(op)
        } while (op != 4);
    }

    private void buscarElemento() {
        int num = pedirNumero("Ingresa un valor a buscar:");
        var mensaje = arbol.buscar(num)             // ← ya devuelve Optional<Nodo> directo
                .map(n -> "El dato " + num + " SÍ existe.")
                .orElse("El dato " + num + " NO existe.");
        mostrar(mensaje);
    }

    private void eliminarElemento() {
        mostrar("Eliminación aún no implementada.");
    }

    private void mostrarOperaciones() {
        var positivos = contarPorCondicion(arbol.getRaiz(), n -> n > 0);
        var negativos = contarPorCondicion(arbol.getRaiz(), n -> n < 0);
        var pares     = contarPorCondicion(arbol.getRaiz(), n -> n % 2 == 0);

        mostrar("""
                Operaciones sobre el árbol
                ──────────────────────────────
                Positivos : %d
                Negativos : %d
                Pares     : %d
                """.formatted(positivos, negativos, pares));
    }

    private int contarPorCondicion(Nodo nodo, IntPredicate condicion) {
        if (nodo == null) return 0;
        int contador = condicion.test(nodo.getValor()) ? 1 : 0;  // ← antes: getNumero()
        return contador
                + contarPorCondicion(nodo.izquierda, condicion)
                + contarPorCondicion(nodo.derecha, condicion);
    }

    private int pedirOpcion(String mensaje) {
        return pedirNumero(mensaje);
    }

    private int pedirNumero(String mensaje) {
        return Integer.parseInt(JOptionPane.showInputDialog(null, mensaje));
    }

    private void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
}
/* 
    int contador_nega = 0;
    int contador_pos = 0;
    private void positivos(Nodo raiz_actual) {
        if (raiz_actual != null) {
            if(raiz_actual.getNumero()>0){
                contador_pos++;
            }
            positivos(raiz_actual.izquierda);
            positivos(raiz_actual.derecha);
            
        }
    }
    private int nega(Nodo raiz_actual) {
        if (raiz_actual != null) {
            if(raiz_actual.getNumero()>0){
                contador_nega++;
            }
            positivos(raiz_actual.izquierda);
            positivos(raiz_actual.derecha);
            
        }
       return contador_nega;
    }
*/
