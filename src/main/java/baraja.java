import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Carta {
    String palo;
    String valor;
    int puntos;

    Carta(String palo, String valor, int puntos) {
        this.palo = palo;
        this.valor = valor;
        this.puntos = puntos;
    }

    public String toString() {
        return valor + " de " + palo;
    }
}

class Mazo {
    private ArrayList<Carta> cartas;

    Mazo() {
        String[] palos = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] puntos = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        cartas = new ArrayList<>();

        for (String palo : palos) {
            for (int i = 0; i < valores.length; i++) {
                cartas.add(new Carta(palo, valores[i], puntos[i]));
            }
        }

        Collections.shuffle(cartas);
    }

    public Carta repartirCarta() {
        return cartas.remove(0);
    }
}

class Jugador {
    private ArrayList<Carta> mano;
    private int puntos;

    Jugador() {
        mano = new ArrayList<>();
        puntos = 0;
    }

    public void recibirCarta(Carta carta) {
        mano.add(carta);
        puntos += carta.puntos;
    }

    public int getPuntos() {
        int puntos = this.puntos;
        int numAses = (int) mano.stream().filter(carta -> carta.valor.equals("A")).count();

        while (puntos > 21 && numAses > 0) {
            puntos -= 10;
            numAses--;
        }

        return puntos;
    }

    public String mostrarMano() {
        return mano.toString();
    }
}

class Blackjack {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Mazo mazo = new Mazo();
        Jugador jugador = new Jugador();
        Jugador crupier = new Jugador();

        // Mano inicial
        jugador.recibirCarta(mazo.repartirCarta());
        jugador.recibirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta());

        System.out.println("Tu mano: " + jugador.mostrarMano() + " - Puntos: " + jugador.getPuntos());
        System.out.println("Mano del crupier: [" + crupier.mostrarMano().split(",")[0] + ", Carta Oculta]");

        // Turno del jugador
        while (jugador.getPuntos() < 21) {
            System.out.print("¿Quieres 'hit' (pedir carta) o 'stand' (plantarte)? ");
            String decision = scanner.nextLine();

            if (decision.equalsIgnoreCase("hit")) {
                jugador.recibirCarta(mazo.repartirCarta());
                System.out.println("Tu mano: " + jugador.mostrarMano() + " - Puntos: " + jugador.getPuntos());
            } else if (decision.equalsIgnoreCase("stand")) {
                break;
            } else {
                System.out.println("Opción inválida. Elige 'hit' o 'stand'.");
            }
        }

        // Verificar si el jugador se pasa de 21
        if (jugador.getPuntos() > 21) {
            System.out.println("¡Te pasaste! Puntos: " + jugador.getPuntos());
            System.out.println("El crupier gana.");
            return;
        }

        // Turno del crupier
        System.out.println("Mano del crupier: " + crupier.mostrarMano() + " - Puntos: " + crupier.getPuntos());
        while (crupier.getPuntos() < 17) {
            crupier.recibirCarta(mazo.repartirCarta());
            System.out.println("El crupier pide carta. Mano del crupier: " + crupier.mostrarMano() + " - Puntos: " + crupier.getPuntos());
        }

        // Determinar el ganador
        if (crupier.getPuntos() > 21 || jugador.getPuntos() > crupier.getPuntos()) {
            System.out.println("¡Ganaste! Puntos del jugador: " + jugador.getPuntos() + " vs. Crupier: " + crupier.getPuntos());
        } else if (jugador.getPuntos() < crupier.getPuntos()) {
            System.out.println("El crupier gana. Puntos del jugador: " + jugador.getPuntos() + " vs. Crupier: " + crupier.getPuntos());
        } else {
            System.out.println("¡Empate!");
        }

        scanner.close();
    }
}
