package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Input {
    final Scanner keyboardInput = new Scanner(System.in);
    private String[] allWords;

    public Input() {
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
        int lineLength;
        int spacesToFill;
        int longestWordLength = 13; //this works for file from task, will be universal later
        while (fileReader.hasNextLine()) {
            StringBuilder line = new StringBuilder(fileReader.nextLine());
            lineLength = line.length();
            spacesToFill = longestWordLength - lineLength;
            line.append(" ".repeat(Math.max(0, spacesToFill + 1)));
            line.append("|");
            this.allWords = Arrays.copyOf(this.allWords, this.allWords.length + 1);
            this.allWords[wordsArrayIndex] = line.toString();
            ++wordsArrayIndex;
        }

        fileReader.close();
    }

    public int chooseBetweenTwoOptions() {
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
        return chooseBetweenTwoOptions();
    }

    public String provideCoordinates(String[] validCoordinatesArray) {
        boolean validCoordinatesProvided = false;
        String coordinates = "";

        while (!validCoordinatesProvided) {
            System.out.println("Valid options " + Arrays.toString(validCoordinatesArray));
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

    public String username() {
        System.out.print("Type your name and press ENTER: ");
        return keyboardInput.nextLine();
    }

    public void updateScore(String username, long guessingTime, int guessingTries) {
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(date);
        String lineToWrite = username + " | " + formattedDate + " | " + guessingTime + " | " + guessingTries;

        try {
            File scores = new File("Scores.txt");
            if (scores.createNewFile()) {
                System.out.println("File created: " + scores.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter("Scores.txt");
            fileWriter.write("name | date | guessing_time | guessing_tries");
            fileWriter.write(System.lineSeparator());
            fileWriter.write(lineToWrite);
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
