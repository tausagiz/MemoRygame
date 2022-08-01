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

    public void printGameOver(boolean isGameWon, int guessingTries, long guessingTime) {
        clear();
        if (isGameWon) {
            System.out.println("You won!");
            System.out.println("You solved the memory game after " + guessingTries + " chances.");
            System.out.println("It took you " + guessingTime + " seconds.");
        } else {
            System.out.println("You loose!");
        }
    }

    public void printGameStatus(String difficultyLevel, int chances) {
        clear();
        drawLine(difficultyLevel);
        System.out.println("Level: " + difficultyLevel);
        System.out.println("Guess chances: " + chances);
    }

    public void drawMatrix(String difficultyLevel, String[] visibleFields) {
        int matrixWidth = visibleFields.length / 2;
        drawLine(difficultyLevel);
        System.out.print("  |");
        for (int i = 1; i <= matrixWidth; i++) {
            System.out.print("       " + i + "       |");
        }
        System.out.println();
        drawLine(difficultyLevel);
        System.out.print("A |");
        for (int i = 0; i < matrixWidth; i++) {
            System.out.print(" " + visibleFields[i]);
        }
        System.out.println();
        drawLine(difficultyLevel);
        System.out.print("B |");
        for (int i = matrixWidth; i < visibleFields.length; i++) {
            System.out.print(" " + visibleFields[i]);
        }
        System.out.println();
        drawLine(difficultyLevel);
    }

    private void drawLine(String difficultyLevel) {
        StringBuilder line = new StringBuilder();
        int lineLength;
        if (difficultyLevel.equals("easy")) {
            lineLength = 67;
        } else {
            lineLength = 67 + 64;
        }
        line.append("-".repeat(lineLength));
        System.out.println(line);
    }
}
