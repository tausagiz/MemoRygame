package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine {
    private final Random randomGenerator = new Random();
    private boolean gameNotFinished;
    private String difficultyLevel;
    private int numberOfWordPairsToFind;
    private int chances;
    private int chancesLeft;
    private String[] solution;
    private String[] validCoordinates;
    private String[] validCoordinatesCache;
    private String[] visibleFields;
    private String firstUncoveredWord;
    private int fieldToCheckIndex;
    private String fieldToCheckCoordinates;
    private int firstFieldIndex;
    private int secondFieldIndex;
    private int firstFieldToHideIndex;
    private int secondFieldToHideIndex;
    private int pairsFound;
    private boolean areLastFieldsToHide;
    private boolean gameWon;
    private long startTime;
    private long endTime;

    public Engine() {
        resetGame();
    }

    public void resetGame() {
        this.difficultyLevel = "not set";
        this.gameNotFinished = true;
        this.gameWon = false;
    }

    public boolean isGameNotFinished() {
        int wordsLeft = this.numberOfWordPairsToFind - this.pairsFound;
        if (wordsLeft == 0) {
            this.gameWon = true;
            this.endTime = System.nanoTime();
        }
        if (!difficultyLevel.equals("not set") && (chancesLeft == 0 || gameWon)) {
            this.gameNotFinished = false;
        }
        return this.gameNotFinished;
    }

    public void setDifficultyLevel(int option) {
        if (option == 1) {
            this.difficultyLevel = "easy";
        } else {
            this.difficultyLevel = "hard";
        }
    }

    public void setParameters(String[] allWords) {
        // setting base parameters
        this.startTime = System.nanoTime();
        this.pairsFound = 0;
        this.gameWon = false;
        this.firstFieldIndex = 0;
        this.secondFieldIndex = 0;
        if (this.difficultyLevel.equals("easy")) {
            this.chances = 10;
            this.numberOfWordPairsToFind = 4;
        } else {
            this.chances = 15;
            this.numberOfWordPairsToFind = 8;
        }
        this.chancesLeft = chances;

        // generating random indexes without duplicates
        int randomIndex = 0;
        int[] randomIndexes = new int[numberOfWordPairsToFind];
        int resultIndex = 0;
        while (resultIndex < numberOfWordPairsToFind) {
            boolean unique = false;
            while (!unique) {
                randomIndex = randomGenerator.nextInt(allWords.length);
//                randomIndex = 5;
                for (int i = 0; i <= resultIndex; i++) {
                    if (randomIndexes[0] == randomIndex) {
                        break;
                    } else {
                        unique = true;
                    }
                }
            }
            randomIndexes[resultIndex] = randomIndex;
            resultIndex++;
        }

        // picking words to find
        String[] wordsToFind = new String[this.numberOfWordPairsToFind];
        for (int i = 0; i < wordsToFind.length; i++) {
            int index = randomIndexes[i];
            wordsToFind[i] = allWords[index];
        }

        // generating doubled words to find
        String[] doubledWordsToFind = new String[this.numberOfWordPairsToFind * 2];
        int index = 0;
        for (String word : wordsToFind) {
            for (int j = 0; j < 2; j++) {
                doubledWordsToFind[index] = word;
                index++;
            }
        }

        // generating shuffled doubled words to find - solution
        this.solution = new String[doubledWordsToFind.length];
        String[] doubledWordsToFindLeft = Arrays.copyOf(doubledWordsToFind, doubledWordsToFind.length);
        for (int i = 0; i < this.solution.length; i++) {
            randomIndex = this.randomGenerator.nextInt(doubledWordsToFindLeft.length);
            this.solution[i] = doubledWordsToFindLeft[randomIndex];
            String[] newDoubledWordsToFindLeft = new String[doubledWordsToFindLeft.length - 1];
            for (int j = 0; j < newDoubledWordsToFindLeft.length; j++) {
                if (j < randomIndex) {
                    newDoubledWordsToFindLeft[j] = doubledWordsToFindLeft[j];
                } else {
                    newDoubledWordsToFindLeft[j] = doubledWordsToFindLeft[j + 1];
                }
            }
            doubledWordsToFindLeft = Arrays.copyOf(newDoubledWordsToFindLeft, newDoubledWordsToFindLeft.length);
        }

        // generating and covering array to uncover
        this.visibleFields = new String[this.solution.length];
        Arrays.fill(this.visibleFields, "      X       |");

        // generating valid coordinates options
        this.validCoordinates = new String[doubledWordsToFind.length];
        for (int i = 0; i < this.numberOfWordPairsToFind; i++) {
            this.validCoordinates[i] = "A" + (i + 1);
        }
        for (int i = this.numberOfWordPairsToFind; i < this.validCoordinates.length; i++) {
            this.validCoordinates[i] = "B" + (i + 1 - this.numberOfWordPairsToFind);
        }
    }

    public String[] getValidCoordinates() {
        return this.validCoordinates;
    }

    public String getDifficultyLevel() {
        return this.difficultyLevel;
    }

    public int getChancesLeft() {
        return this.chancesLeft;
    }

    public String[] getVisibleFields() {
        return this.visibleFields;
    }

    public void uncoverField(String coordinates) {
        if (coordinates.charAt(0) == 'A') {
            this.fieldToCheckIndex = Character.getNumericValue(coordinates.charAt(1)) - 1;
        } else {
            this.fieldToCheckIndex = Character.getNumericValue(coordinates.charAt(1)) - 1 + numberOfWordPairsToFind;
        }
        this.visibleFields[fieldToCheckIndex] = this.solution[fieldToCheckIndex];
        this.fieldToCheckCoordinates = coordinates;
    }

    public void uncoverFirstField(String coordinates) {
        if (areLastFieldsToHide) {
            this.visibleFields[this.firstFieldToHideIndex] = "      X       |";
            this.visibleFields[this.secondFieldToHideIndex] = "      X       |";
        }
        uncoverField(coordinates);
        this.firstFieldIndex = this.fieldToCheckIndex;
        this.firstUncoveredWord = this.solution[firstFieldIndex];
        validCoordinatesCache = Arrays.copyOf(validCoordinates, validCoordinates.length);
        updateValidCoordinates();
    }

    public void uncoverSecondField(String coordinates) {
        uncoverField(coordinates);
        this.secondFieldIndex = this.fieldToCheckIndex;
        String secondUncoveredWord = this.solution[secondFieldIndex];
        updateValidCoordinates();
        if (secondUncoveredWord.equals(this.firstUncoveredWord)) {
            this.pairsFound++;
            this.areLastFieldsToHide = false;
        } else {
            this.areLastFieldsToHide = true;
            validCoordinates = Arrays.copyOf(validCoordinatesCache, validCoordinatesCache.length);
            this.firstFieldToHideIndex = firstFieldIndex;
            this.secondFieldToHideIndex = secondFieldIndex;
            //TODO quick fixes, try to optimise
            this.firstUncoveredWord = "";
        }
        --this.chancesLeft;
    }

    public void flipFields(int turn, String coordinates) {
        if (turn == 1) {
            uncoverFirstField(coordinates);
        } else {
            uncoverSecondField(coordinates);
        }
    }

    public void updateValidCoordinates() {
        int indexToSkip = 0;
        for (int i = 0; i < validCoordinates.length; i++) {
            if (validCoordinates[i].equals(fieldToCheckCoordinates)) {
                indexToSkip = i;
            }
        }
        String[] newValidCoordinates = new String[validCoordinates.length - 1];
        for (int i = 0; i < newValidCoordinates.length; i++) {
            if (i < indexToSkip) {
                newValidCoordinates[i] = validCoordinates[i];
            } else {
                newValidCoordinates[i] = validCoordinates[i + 1];
            }
        }
        validCoordinates = Arrays.copyOf(newValidCoordinates, newValidCoordinates.length);
    }

    public int getGuessingTries() {
        return chances - getChancesLeft();
    }

    public boolean isGameWon() {
        return this.gameWon;
    }

    public long getGuessingTimeInSeconds() {
        long guessingTime = this.endTime - this.startTime;
        return TimeUnit.SECONDS.convert(guessingTime, TimeUnit.NANOSECONDS);
    }

}

