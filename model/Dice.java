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

    public static Dice getInstance() {
        if (instance == null) {
            instance = new Dice();
        }
        return instance;
    }

    public int roll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
        return dice1 + dice2;
    }

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
        return dice1+dice2;
    }
}
