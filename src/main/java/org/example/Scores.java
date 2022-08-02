package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Scores {

    private final String scoresFilePath;
    private final int scoresToRemember;
    private final String scoresHeader = "name | date | guessing_time | guessing_tries";

    public Scores() {
        this.scoresFilePath = "Scores.txt";
        this.scoresToRemember = 10;
    }

    public void updateScore(String username, long guessingTime, int guessingTries) {
        String[] oldScores = getScoreRecords();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(date);
        String newRecord = username + " | " + formattedDate + " | " + guessingTime + " | " + guessingTries;
        int newScoresCount = oldScores.length + 1;
        if (newScoresCount >= this.scoresToRemember) {
            newScoresCount = scoresToRemember;
        }
        String[] newScores = new String[newScoresCount];
        newScores[0] = newRecord;
        System.arraycopy(oldScores, 0, newScores, 1, newScoresCount - 1);

        try {
            FileWriter fileWriter = new FileWriter("Scores.txt");
            fileWriter.write(this.scoresHeader);
            for (String record :
                    newScores) {
                fileWriter.write(System.lineSeparator());
                fileWriter.write(record);
            }

            fileWriter.close();
            System.out.println("Successfully added scores to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void createScoresFile() {
        try {
            File scores = new File(this.scoresFilePath);
            if (scores.exists()) {
                System.out.println(scores.getName() + " file already exists.");
                countLinesInScore(scores);
            } else {
                if (scores.createNewFile()) {
                    System.out.println("File created: " + scores.getName());
                }

            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void countLinesInScore(File scores) {
        int linesCount = -1; // because the first line is the header
        try {
            Scanner linesCounter = new Scanner(scores);
            while (linesCounter.hasNextLine()) {
                linesCounter.nextLine();
                linesCount++;
            }
            linesCounter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Scores.txt file missing! ");
        }
        System.out.println("There are " + linesCount + " score records in this file.");
    }

    public String[] provideScoresTable() {
        String[] scoreRecords = getScoreRecords();
        String[] scoresTable = new String[scoreRecords.length + 1];
        // adding header
        scoresTable[0] = this.scoresHeader;
        System.arraycopy(scoreRecords, 0, scoresTable, 1, scoresTable.length - 1);
        return scoresTable;
    }

    private String[] getScoreRecords() {
        String[] lines = new String[0];
        File scoresFile = new File(this.scoresFilePath);
        Scanner fileReader = null;

        while (fileReader == null) {
            try {
                fileReader = new Scanner(scoresFile);
            } catch (FileNotFoundException e) {
                System.out.println("There is no Scores.txt file!");
                createScoresFile();
            }
        }

        int fileLinesCount = 0;

        while (fileReader.hasNextLine()) {
            lines = Arrays.copyOf(lines, lines.length + 1);
            lines[fileLinesCount] = fileReader.nextLine();
            ++fileLinesCount;
        }

        fileReader.close();

        String[] scoreRecords;
        if (fileLinesCount == 0) {
            scoreRecords = new String[0];
        } else {
            // removing header
            scoreRecords = new String[fileLinesCount - 1];
            System.arraycopy(lines, 1, scoreRecords, 0, fileLinesCount - 1);
        }

        return scoreRecords;

    }

}
