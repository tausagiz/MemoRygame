package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Input {
    final Scanner keyboardInput = new Scanner(System.in);
    private String[] allWords;

    public Input() {
        importWordsFromFile();
    }

    private void importWordsFromFile() {
        this.allWords = new String[0];
        File wordsFile = new File("Words.txt");
        Scanner fileReader = null;
        while (fileReader == null) {
            try {
                fileReader = new Scanner(wordsFile);
            } catch (FileNotFoundException e) {
                System.out.println("There is no Words.txt file! Provide missing file and try again.");
                doPlayerWantsToTryAgain();
            }
        }
        int wordsArrayIndex = 0;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            this.allWords = Arrays.copyOf(this.allWords, this.allWords.length + 1);
            this.allWords[wordsArrayIndex] = line;
            ++wordsArrayIndex;
        }
        fileReader.close();
    }

    public int chooseBetweenTwoOptions() {
        //TODO clean - did like this because of problems with .nextInt()
        int option = 0;
        String keys;
        while (option != 1 && option != 2) {
            System.out.print("Choose by typing 1 or 2, then press Enter: ");
            keys = keyboardInput.nextLine();
            if (keys.equals("1") || keys.equals("2")) {
                option = Integer.parseInt(keys);
            } else {
                System.out.print("Wrong value. ");
            }
        }
        return option;
    }

    public int playerProvidesDifficultyLevel() {
        System.out.println("There are two difficulty levels in this game:");
        System.out.println("1. EASY - 4 randomly selected word pairs to discover and 10 chances to reveal all memory");
        System.out.println("2. HARD - 8 randomly selected word pairs and 15 chances to reveal all word pairs ");
        return chooseBetweenTwoOptions(); //TODO try to use char
    }

    public String provideCoordinates(String[] validCoordinatesArray) {
        boolean validCoordinatesProvided = false;
        String coordinates = "";
        while (!validCoordinatesProvided) {
            System.out.println("Valid options: " + Arrays.toString(validCoordinatesArray));
            System.out.print("Type coordinates to check and press Enter: ");
            coordinates = keyboardInput.nextLine();
            for (String coordinate : validCoordinatesArray) {
                if (coordinate.equals(coordinates)) {
                    validCoordinatesProvided = true;
                    break;
                }
            }
            if (!validCoordinatesProvided) {
                System.out.print("Wrong value. ");
            }
        }
        return coordinates;
    }

    public boolean doPlayerWantsToTryAgain() {
        System.out.print("Do you want to try again? 1 for YES, 2 for NO. ");
        return chooseBetweenTwoOptions() == 1;
    }

    public String[] getAllWords() {
        return allWords;
    }

}