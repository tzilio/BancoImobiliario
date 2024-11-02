package model;

import java.util.Random;

public class Dice {
    private Random random;
    private int dice1;
    private int dice2;

    public Dice() {
        random = new Random();
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

    public int getdice2() {
        return dice2;
    }
}
