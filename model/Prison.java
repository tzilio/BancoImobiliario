package model;

public class Prison extends BoardPosition {
    private static final int BAIL_AMOUNT = 50;  
    private static Prison instance;  

    private Prison(int position) {
        super(position);
    }

    public static Prison getInstance(int position) {
        if (instance == null) {
            instance = new Prison(position);
        }
        return instance;
    }

    @Override
    public void onLand(Player player) {
        if (player.isInJail()) {
            System.out.println(player.getName() + " está preso.");
        } else {
            System.out.println(player.getName() + " está apenas visitando a prisão.");
        }
    }

    public void sendToJail(Player player) {
        player.setInJail(true);
        player.setPosition(this.getPosition()); 
    }

    public void payBail(Player player) {
        if (player.getBalance() >= BAIL_AMOUNT) {
            player.updateBalance(-BAIL_AMOUNT);
            player.setInJail(false);
            System.out.println(player.getName() + " pagou a fiança de " + BAIL_AMOUNT + " e saiu da prisão.");
        } else {
            System.out.println(player.getName() + " não tem dinheiro suficiente para pagar a fiança.");
        }
    }

    public void tryToEscapeWithDouble(Player player, int dice1, int dice2) {
        if (dice1 == dice2) {
            player.setInJail(false);
            System.out.println(player.getName() + " tirou um número duplo e saiu da prisão!");
        } else {
            System.out.println(player.getName() + " não conseguiu sair da prisão. Tente novamente no próximo turno.");
        }
    }
}
