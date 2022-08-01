package org.example;

public class MainMemoryGame {
    final static Screen SCREEN = new Screen();
    final static Engine ENGINE = new Engine();
    final static Input INPUT = new Input();

    public static void main(String[] args) {
        String coordinates;

        while (ENGINE.isGameNotFinished()) {
            SCREEN.printWelcomeMessage();
            ENGINE.setDifficultyLevel(INPUT.playerProvidesDifficultyLevel());
            ENGINE.setParameters(INPUT.getAllWords());

            while (ENGINE.isGameNotFinished()) {
                for (int turn = 1; turn <= 2; turn++) {
                    SCREEN.printGameStatus(ENGINE.getDifficultyName(), ENGINE.getChancesLeft());
                    SCREEN.drawMatrix(ENGINE.getVisibleFields());
                    coordinates = INPUT.provideCoordinates(ENGINE.getValidCoordinates());
                    ENGINE.flipFields(turn, coordinates);
                }
            }

            SCREEN.printGameOver(ENGINE.isGameWon(), ENGINE.getGuessingTries(), ENGINE.getGuessingTimeInSeconds());

            if (INPUT.doPlayerWantsToTryAgain()) {
                ENGINE.resetGame();
            }
        }
    }
}