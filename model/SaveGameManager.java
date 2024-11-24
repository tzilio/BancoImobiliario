package model;

import java.io.*;
import java.util.List;

public class SaveGameManager {

    public static void saveGame(String fileName, Bank bank, List<Player> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(bank);   // Salva o estado do Banco
            oos.writeObject(players); // Salva a lista de jogadores
            System.out.println("Jogo salvo com sucesso em " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao salvar o jogo.");
        }
    }

    public static Object[] loadGame(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Bank bank = (Bank) ois.readObject();

            Object playersObject = ois.readObject();
            if (!(playersObject instanceof List<?>)) {
                throw new ClassNotFoundException("O arquivo não contém uma lista de jogadores válida.");
            }

            List<?> rawList = (List<?>) playersObject;
            for (Object obj : rawList) {
                if (!(obj instanceof Player)) {
                    throw new ClassNotFoundException("A lista contém elementos que não são do tipo Player.");
                }
            }

            @SuppressWarnings("unchecked")
            List<Player> players = (List<Player>) rawList;

            System.out.println("Jogo carregado com sucesso de " + fileName);
            return new Object[]{bank, players};
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o jogo.");
            return null;
        }
    }
}
