package model;

import java.util.Random;

public class Dice {
    private Random random;
    private int dice1;
    private int dice2;

    private static Dice instance;

    private Dice() {
        random = new Random();
    }

    // Singleton com thread safety usando Holder
    private static class DiceHolder {
        private static final Dice INSTANCE = new Dice();
    }

    public static Dice getInstance() {
        return DiceHolder.INSTANCE;
    }

    // Método para rolar ambos os dados
    public int roll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
        return getDiceSum();
    }

    // Método para rolar apenas um dado
    public int rollSingle() {
        return random.nextInt(6) + 1;
    }

    // Verifica se os dois dados têm valores iguais
    public boolean isDouble() {
        return dice1 == dice2;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public int getDiceSum() {
        return dice1 + dice2;
    }

    // Define uma semente para resultados reprodutíveis (útil para testes)
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    @Override
    public String toString() {
        return "Dice 1: " + dice1 + ", Dice 2: " + dice2 + ", Sum: " + getDiceSum();
    }
}
