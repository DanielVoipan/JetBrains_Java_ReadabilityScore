package readability;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

enum grades {

    Kindergarten(1,"5-6"),
    firstGrade(2,"6-7"),
    secondGrade(3, "7-8"),
    thirdGrade(4,"8-9"),
    fourthGrade(5,"9-19"),
    fifthGrade(6,"10-11"),
    sixthGrade(7,"11-12"),
    seventhGrade(8,"12-13"),
    eighthGrade(9,"13-14"),
    ninthGrade(10,"14-15"),
    tenthGrade(11,"15-16"),
    eleventhGrade(12,"16-17"),
    twelfthGrade(13,"17-18"),
    collegeStudent(14,"18-22");
    private int score;
    private String age;
    grades(int score, String age) {
        this.age = age;
        this.score = score;
    }

    String getAge() {
        return age;
    }

    int getScore() {
        return score;
    }
}

public class Main {
    public static void main(String[] args) {
        File file = new File(args[0]);
        String line;
        // read file from parameter
        try (Scanner scanner = new Scanner(file)) {
            line = scanner.nextLine();
            fileScan(line);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void fileScan(String line){
        // split line by . or ! or ? to get sentences
        String[] splitLine = line.split("(\\.\\s|!\\s|\\?\\s)");

        double totalWords = 0;
        double nrOfSentences = 0;
        int nrOfCharacters = 0;
        int nrOfSyllables = 0;
        int nrOfPolysyllables = 0;
        double avgCharPerWords = 0;
        double avgSentperWords = 0;

        for (String s : splitLine) {
            String str = s.trim();
            String[] words = str.split("\\s");
            for (String w : words) {
                totalWords++;
                // count syllable
                int syllables = countSyllable(w);
                nrOfSyllables = syllables + nrOfSyllables;
                if (syllables > 2) {
                    // count polysyllables
                    nrOfPolysyllables++;
                }
            }
            // count sentences
            nrOfSentences++;
        }
        for (String s : line.split("")) {
            if (s.matches("^\\s")) {
                continue;
            }
            // count characters
            nrOfCharacters++;
        }
        // avg of chars per 100 words
        avgCharPerWords = (nrOfCharacters * 100.0) / totalWords;
        // avg of sentences per 100 words
        avgSentperWords = (nrOfSentences * 100.0) / totalWords;

        String automatedReadabilityIndexAge = null;
        String fleschKincaidReadabilityAge = null;
        String GobbledygookAge = null;
        String colemanLiauIndexAge = null;

        // get methods values
        double automatedReadabilityIndex = automatedReadabilityIndex(nrOfCharacters, nrOfSentences, totalWords);
        double fleschKincaidReadability = fleschKincaidReadability(nrOfSyllables, nrOfSentences, totalWords);
        double Gobbledygook = Gobbledygook(nrOfPolysyllables, nrOfSentences);
        double colemanLiauIndex = colemanLiauIndex(avgCharPerWords, avgSentperWords);

        // get age for each method

        for (grades g : grades.values()) {
            if (g.getScore() == automatedReadabilityIndex) {
                automatedReadabilityIndexAge = g.getAge();
            }
            if (g.getScore() == fleschKincaidReadability) {
                fleschKincaidReadabilityAge = g.getAge();
            }
            if (g.getScore() == Gobbledygook) {
                GobbledygookAge = g.getAge();
            }
            if (g.getScore() == (int) colemanLiauIndex) {
                colemanLiauIndexAge = g.getAge();
            }
        }
        Scanner scanner = new Scanner(System.in);

        System.out.printf("Words: %s\n", totalWords);
        System.out.printf("Sentences: %s\n", nrOfSentences);
        System.out.printf("Characters: %s\n", nrOfCharacters);
        System.out.printf("Syllables: %d\n",nrOfSyllables);
        System.out.printf("Polysyllables: %d\n", nrOfPolysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String type = scanner.next();
        System.out.println();
        switch (type.toLowerCase()) {
            case "all" :
                automatedReadabilityIndexShow(automatedReadabilityIndex, automatedReadabilityIndexAge);
                fleschKincaidReadabilityShow(fleschKincaidReadability, fleschKincaidReadabilityAge);
                GobbledygookShow(Gobbledygook, GobbledygookAge);
                colemanLiauIndexShow(colemanLiauIndex, colemanLiauIndexAge);
                break;
            case "ari" :
                automatedReadabilityIndexShow(automatedReadabilityIndex, automatedReadabilityIndexAge);
                break;
            case "fk" :
                fleschKincaidReadabilityShow(fleschKincaidReadability, fleschKincaidReadabilityAge);
                break;
            case "smog" :
                GobbledygookShow(Gobbledygook, GobbledygookAge);
                break;
            case "cl" :
                colemanLiauIndexShow(colemanLiauIndex, colemanLiauIndexAge);
                break;
        }
    }

    static double automatedReadabilityIndex(int nrOfCharacters, double nrOfSentences, double totalWords) {
        double score = 4.71 * (nrOfCharacters/totalWords) + 0.5 * (totalWords/nrOfSentences) - 21.43;
        return Math.ceil(score);
    }

    static double fleschKincaidReadability(int nrOfSyllables, double nrOfSentences, double totalWords) {
        double score = 0.39 * (totalWords/nrOfSentences) + 11.8 * (nrOfSyllables/totalWords) - 15.59;
        return Math.ceil(score);
    }

    static double Gobbledygook(double polysyllables, double sentences) {
        double score = 1.043 * Math.sqrt(polysyllables * (30 / sentences)) + 3.1291;
        return Math.ceil(score);
    }

    static double colemanLiauIndex(double l, double s) {
        double score = 0.0588 * l - 0.296 * s - 15.8;
        return score;
    }

    static void colemanLiauIndexShow(double colemanLiauIndex, String age) {
        String[] splitAge = age.split("-");
        System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).\n", colemanLiauIndex, splitAge[1]);
    }

    static void GobbledygookShow(double Gobbledygook, String age) {
        String[] splitAge = age.split("-");
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).\n", Gobbledygook, splitAge[1]);
    }

    static void automatedReadabilityIndexShow(double automatedReadabilityIndex, String age) {
        String[] splitAge = age.split("-");
        System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n", automatedReadabilityIndex, splitAge[1]);
    }
    static void fleschKincaidReadabilityShow(double fleschKincaidReadability, String age) {
        String[] splitAge = age.split("-");
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).\n", fleschKincaidReadability, splitAge[1]);
    }

    // count syllables method for each word
    static int countSyllable(String word) {
        // we get rid of , from words
        word = word.replaceAll(",", "");
        // split by "" to get the chars
        String[] wordSplit = word.split("");
        int number = 0;
        // if has y or Y at the end we count syllables
        // because y or Y at the end is a vowel
        if (word.matches(".*?[yY]$")) {
            number++;
        }
        // next we parse the word from start to end - 1
        for (int i = 0; i < wordSplit.length; i++) {
            if (i < wordSplit.length - 1) {
                // if there are 2 vowels one to each other
                // we increment once
                if (wordSplit[i].matches("[aieouAIEOU]") && wordSplit[i + 1].matches("[aieouAIEOU]")) {
                    number++;
                    // we jump to the next iteration
                    // to get rid of the two vowels
                    i++;
                } else if (wordSplit[i].matches("[aieouAIEOU]")) {
                    // if we find any other vowel, we increment
                    // we don't increment if the vowel e is at the end
                    number++;
                }
            }
        }// if we don't find vowel, we consider 1 syllable
        if (number == 0) {
            number = 1;
        }
        return number;
    }
}
