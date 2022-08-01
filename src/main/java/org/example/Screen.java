package org.example;

public class Screen {

    public void clear() { //TODO after securing basic functionality try to use more elegant and robust solution
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    public void printWelcomeMessage() {
        clear();
        String welcomeMessage = "Welcome to this simple memory game!";
        System.out.println(welcomeMessage);
        //TODO maybe add rules of the game
    }

    public void printGameOver(boolean isGameWon) {
        clear();
        if (isGameWon) {
            System.out.println("You won!");
        } else {
            System.out.println("You loose!");
        }
    }

    public void printGameStatus(String difficultyName, int chances) {
        clear();
        System.out.println("-----------------");
        System.out.println("Level: " + difficultyName);
        System.out.println("Guess chances: " + chances);
        System.out.println();
    }

    public void drawMatrix(String[] visibleFields) {
        int matrixWidth = visibleFields.length / 2;
        System.out.print(" ");
        for (int i = 1; i <= matrixWidth; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        System.out.print("A");
        for (int i = 0; i < matrixWidth; i++) {
            System.out.print(" " + visibleFields[i]);
        }
        System.out.println();
        System.out.print("B");
        for (int i = matrixWidth; i < visibleFields.length; i++) {
            System.out.print(" " + visibleFields[i]);
        }
        System.out.println();
        System.out.println("-----------------");
    }
}
