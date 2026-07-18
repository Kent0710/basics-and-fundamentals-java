import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Student Grading System!");

        // Validate input (must be a positive integer, at least 1 student)
        int numberOfStudents;
        while (true) {
            System.out.print("Enter the number of students: ");

            if (!sc.hasNextInt()) {
                if (!sc.hasNext()) {
                    System.out.println("\nNo more input detected. Exiting program.");
                    sc.close();
                    return;
                }
                System.out.println("Please enter a valid integer.");
                sc.next(); // discard invalid input
                continue;
            }

            int input;
            try {
                input = sc.nextInt();
                sc.nextLine(); // consume the newline
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("\nNo more input detected. Exiting program.");
                sc.close();
                return;
            }

            if (input < 1 || input > 1000) {
                System.out.println("Invalid number of students. Please enter a positive integer between 1 and 1000.");
            } else {
                numberOfStudents = input;
                break;
            }
        }

        String[] studentNames = new String[numberOfStudents];
        int[] finalScores = new int[numberOfStudents];

        int topScore = -1;
        int countOfPassedStudents = 0;
        int countOfFailedStudents = 0;

        // Get the inputs (student name + final score)
        for (int i = 0; i < numberOfStudents; i++) {
            // Validate input (cannot be empty and cannot exceed 1000 characters)
            while (true) {
                System.out.print("Student Name: ");

                String studentName;
                try {
                    if (!sc.hasNextLine()) {
                        System.out.println("\nNo more input detected. Exiting program.");
                        sc.close();
                        return;
                    }
                    studentName = sc.nextLine().trim();
                } catch (NoSuchElementException | IllegalStateException e) {
                    // hasNextLine() can still race with a broken/closed stream (e.g. repeated
                    // Ctrl+D / Ctrl+Z at EOF), so we guard the actual read too.
                    System.out.println("\nNo more input detected. Exiting program.");
                    sc.close();
                    return;
                }

                if (studentName.isEmpty()) {
                    System.out.println("Please enter a valid name.");
                    continue;
                }

                if (studentName.length() > 1000) {
                    System.out.println(
                            "Maximum length of student name exceeded (1000 characters). Please enter a valid name.");
                    continue;
                }

                if (!studentName.matches("\\p{L}+([ .'-]\\p{L}+)*")) {
                    System.out.println(
                            "Please enter a valid name (letters only, with single spaces/periods/apostrophes/hyphens allowed only between letters).");
                    continue;
                }

                studentNames[i] = studentName;
                break;
            }

            // Validate input (must be an integer between 0 and 100)
            while (true) {
                System.out.print("Final Score: ");

                try {
                    if (!sc.hasNextInt()) {
                        if (!sc.hasNext()) {    
                            System.out.println("\nNo more input detected. Exiting program.");
                            sc.close();
                            return;
                        }
                        System.out.println("Please enter a valid integer.");
                        sc.next();
                        continue;
                    }

                    int score = sc.nextInt();
                    sc.nextLine(); // consume the newline

                    if (score >= 101 || score <= -1) {
                        System.out.println("Invalid Score. Please enter a score between 0 and 100.");
                    } else {
                        topScore = Math.max(topScore, score);

                        finalScores[i] = score;
                        break;
                    }
                } catch (NoSuchElementException | IllegalStateException e) {
                    System.out.println("\nNo more input detected. Exiting program.");
                    sc.close();
                    return;
                }
            }
            System.out.println();
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("STUDENT RESULTS:");
        System.out.println("----------------------------------------------------------");

        for (int i = 0; i < numberOfStudents; i++) {
            String letterGrade;
            int score = finalScores[i];

            if (score >= 98) {
                letterGrade = "A+";
            } else if (score >= 92) {
                letterGrade = "A";
            } else if (score >= 87) {
                letterGrade = "B+";
            } else if (score >= 81) {
                letterGrade = "B";
            } else if (score >= 77) {
                letterGrade = "C+";
            } else if (score >= 71) {
                letterGrade = "C";
            } else if (score >= 60) {
                letterGrade = "D";
            } else {
                letterGrade = "F";
            }

            String remarks;

            if (score >= 60) {
                countOfPassedStudents++;
                remarks = "Passed";
            } else {
                countOfFailedStudents++;
                remarks = "Failed";
            }

            System.out.println(remarks);
            System.out.println("\t > Student Name: " + studentNames[i]);
            System.out.println("\t > Final Score: " + score);
            System.out.println("\t > Letter Grade: " + letterGrade);
            System.out.println("----------------------------------------------------------");
        }

        System.out.println("STUDENT ANALYSIS:");
        System.out.println("----------------------------------------------------------");

        for (int i = 0; i < numberOfStudents; i++) {
            if (finalScores[i] == topScore) {
                System.out.println("Top Scorer: " + studentNames[i] + " with a score of " + topScore);
            }
        }

        double meanScore = 0;
        for (int score : finalScores) {
            meanScore += score;
        }
        meanScore /= numberOfStudents;

        System.out.println("\nMean Score: " + String.format("%.2f", meanScore));
        boolean isClassAverageBelow70 = meanScore < 70;
        System.out.println("Did the class average fall below 70? " + isClassAverageBelow70);
        System.out.println("\nNumber of Passed Students: " + countOfPassedStudents);
        System.out.println("Number of Failed Students: " + countOfFailedStudents);

        System.out.println("----------------------------------------------------------");

        sc.close();
    }
}