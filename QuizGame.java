bimport java.util.Scanner;

public class QuizGame {

    // Helper class to structure our questions
    static class Question {
        String prompt;
        String answer;

        public Question(String prompt, String answer) {
            this.prompt = prompt;
            this.answer = answer;
        }
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            // 1. Minimum 5 multiple-choice questions
            Question[] questions = {
                new Question("What is the capital of France?\n(a) Berlin\n(b) Madrid\n(c) Paris\n", "c"),
                new Question("Which planet is known as the Red Planet?\n(a) Venus\n(b) Mars\n(c) Jupiter\n", "b"),
                new Question("What is the largest ocean on Earth?\n(a) Atlantic\n(b) Indian\n(c) Pacific\n", "c"),
                new Question("Who wrote 'Romeo and Juliet'?\n(a) Dickens\n(b) Shakespeare\n(c) Twain\n", "b"),
                new Question("What is 5 + 7?\n(a) 10\n(b) 12\n(c) 14\n", "b")
            };

            int score = 0; // 4. Maintain score during quiz

            System.out.println("--- Welcome to the Quiz Game! ---");

            for (int i = 0; i < questions.length; i++) {
                System.out.println("\nQuestion " + (i + 1) + ":");
                System.out.print(questions[i].prompt);
                
                // 2. Accept user answers from keyboard
                System.out.print("Your answer: ");
                String userAnswer = keyboard.nextLine().toLowerCase().trim();

                // 3. Check correct/incorrect answers
                if (userAnswer.equals(questions[i].answer)) {
                    System.out.println(">> Correct!");
                    score++;
                } else {
                    System.out.println(">> Incorrect! The right answer was (" + questions[i].answer + ").");
                }
            }

            // 5. Display final result at end
            System.out.println("\n---------------------------");
            System.out.println("Quiz Over! Your Final Score: " + score + "/" + questions.length);
            System.out.println("---------------------------");

            // 6. Option to restart or exit
            System.out.print("Do you want to play again? (yes/no): ");
            String choice = keyboard.nextLine().toLowerCase().trim();
            if (!choice.equals("yes")) {
                playAgain = false;
                System.out.println("Thank you for playing!");
            }
        }
        keyboard.close();
    }
}