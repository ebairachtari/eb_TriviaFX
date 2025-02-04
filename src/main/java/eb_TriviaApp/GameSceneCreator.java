package eb_TriviaApp;

import eb_services.TriviaAPIService;
import eb_model.Erwtisi;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class GameSceneCreator {

    // Μεταβλητές 
    private final App app;
    private final List<Erwtisi> questions; // Λίστα ερωτήσεων
    private int currentQuestionIndex = 0; // Δείκτης τρέχουσας ερώτησης
    private int score = 0; // Σκορ χρήστη
    private boolean answered = false; // Έλεγχος αν έχει απαντηθεί η ερώτηση
    private ProgressBar progressBar; // Μπάρα προόδου
    private Label progressLabel; // Ετικέτα προόδου

    //Constructor της GameSceneCreator
    public GameSceneCreator(App app, String difficulty, String type, int numberOfQuestions, Integer categoryId) {
        this.app = app;

        // Εκτύπωση των φίλτρων για debugging
        System.out.println("Creating Game Scene - Difficulty: " + difficulty + ", Type: " + type + ", Number of Questions: " + numberOfQuestions + ", CategoryId: " + categoryId);

        // Ανάκτηση ερωτήσεων από το API μέσω του TriviaAPIService και της Erwtisi από το Α Μέρος
        TriviaAPIService triviaService = new TriviaAPIService();
        List<Erwtisi> fetchedQuestions;

        try {
            // Αν categoryId είναι null, η κατηγορία δεν θα συμπεριληφθεί στα φίλτρα
            fetchedQuestions = triviaService.getQuestionsWithFilters(difficulty, type, numberOfQuestions, categoryId);
            System.out.println("Fetched Questions: " + fetchedQuestions);
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
            e.printStackTrace();
            fetchedQuestions = new ArrayList<>(); // Δημιουργία κενής λίστας σε περίπτωση αποτυχίας
        }

        this.questions = fetchedQuestions; // Αποθήκευση των ερωτήσεων
    }

    //Μέθοδος για τη δημιουργία σκηνής του παιχνιδιού και εμφάνιση των ερωτήσεων & απαντήσεων
    public Scene createScene() {
        // Δημιουργία του βασικού layout
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        // Μπάρα προόδου
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(600);

        // Ετικέτα προόδου
        progressLabel = new Label("Ερώτηση 1 / " + questions.size());
        progressLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Ετικέτα για την ερώτηση
        Label questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #333333; -fx-alignment: center;");
        questionLabel.setMaxWidth(600);
        questionLabel.setMinHeight(60);

        // Ετικέτα για feedback
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #555555;");

        // Δημιουργία κουμπιών επιλογών
        Button option1 = createStyledButton();
        Button option2 = createStyledButton();
        Button option3 = createStyledButton();
        Button option4 = createStyledButton();

        // Φόρτωση της πρώτης ερώτησης
        loadQuestion(questionLabel, option1, option2, option3, option4, feedbackLabel);

        // Ρύθμιση ενεργειών για τα κουμπιά
        option1.setOnAction(e -> handleAnswer(option1.getText(), questionLabel, option1, option2, option3, option4, feedbackLabel));
        option2.setOnAction(e -> handleAnswer(option2.getText(), questionLabel, option1, option2, option3, option4, feedbackLabel));
        option3.setOnAction(e -> handleAnswer(option3.getText(), questionLabel, option1, option2, option3, option4, feedbackLabel));
        option4.setOnAction(e -> handleAnswer(option4.getText(), questionLabel, option1, option2, option3, option4, feedbackLabel));

        // Προσθήκη στοιχείων στο layout
        root.getChildren().addAll(progressBar, progressLabel, questionLabel, option1, option2, option3, option4, feedbackLabel);
        return new Scene(root, 800, 500);
    }

    //Μέθοδος για δημιουργία κουμπιού με συγκεκριμένο στυλ
    private Button createStyledButton() {
        Button button = new Button();
        button.setStyle("-fx-background-color: #d9d9d9; -fx-border-color: black; -fx-border-width: 2px; -fx-text-fill: black; -fx-font-size: 14px; -fx-alignment: center;");
        button.setMaxWidth(300);
        button.setWrapText(true);
        return button;
    }

    //Μέθοδος που φορτώνει την τρέχουσα ερώτηση και τις απαντήσεις προς ερπιλογή
    private void loadQuestion(Label questionLabel, Button option1, Button option2, Button option3, Button option4, Label feedbackLabel) {
        answered = false; // Επαναφορά για τη νέα ερώτηση

        if (currentQuestionIndex < questions.size()) {
            Erwtisi question = questions.get(currentQuestionIndex);
            String decodedQuestion = StringEscapeUtils.unescapeHtml4(question.getKeimeno());
            questionLabel.setText(decodedQuestion);
            
            // Κουμπια για Σ/Λ
            if (question.getTupos().equals("boolean")) {
                option1.setText("True");
                option2.setText("False");
                // Εμφάνιση μόνο 2 κουμπιών
                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(false);
                option4.setVisible(false);
            } else {
            	// Δημιουργία λίστας με όλες τις πιθανές απαντήσεις
                List<String> choices = new ArrayList<>();
                choices.add(StringEscapeUtils.unescapeHtml4(question.getSwsthApanthsh()));
                for (String incorrectAnswer : question.getLanthasmenesApanthseis()) {
                    choices.add(StringEscapeUtils.unescapeHtml4(incorrectAnswer));
                }
                
                // Ανακάτεμα των απαντήσεων ώστε να μην εμφανίζεται πάντα πρώτη η σωστή
                Collections.shuffle(choices);
                
                // Κουμπια για πολλαπλής επιλογής
                option1.setText(choices.get(0));
                option2.setText(choices.get(1));
                option3.setText(choices.get(2));
                option4.setText(choices.get(3));
                // Εμφάνιση και των 4 κουμπιών
                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                option4.setVisible(true);
            }

            feedbackLabel.setText(""); // Καθαρισμός feedback
            enableButtons(option1, option2, option3, option4); // Επανενεργοποίηση κουμπιών
            updateProgress();
        } else {
            // Μετάβαση στην τελική σκηνή
            app.changeScene(new FinalSceneCreator(app, score, questions.size()).createScene());
        }
    }

    //Μέθοδος που διαχειρίζεται την απάντηση του χρήστη
    private void handleAnswer(String selectedAnswer, Label questionLabel, Button option1, Button option2, Button option3, Button option4, Label feedbackLabel) {
        if (answered) return; // Αποφυγή πολλαπλών απαντήσεων
        answered = true;

        // Λήψη της τρέχουσας ερώτησης
        Erwtisi question = questions.get(currentQuestionIndex);

        // Έλεγχος αν η απάντηση είναι σωστή
        if (selectedAnswer.equals(question.getSwsthApanthsh())) {
            feedbackLabel.setText("Σωστή απάντηση!");
            feedbackLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            score += 10; // Αύξηση του σκορ κατά 10 για κάθε σωστή απάντηση
        } else {
            feedbackLabel.setText("Λάθος απάντηση!");
            feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }

        // Πάγωμα των κουμπιών για να μην μπορέσει ο χρήστης να επιλέξει άλλη απάντηση 
        disableButtons(option1, option2, option3, option4);

        // Καθυστέρηση πριν τη μετάβαση στην επόμενη ερώτηση
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            currentQuestionIndex++;
            loadQuestion(questionLabel, option1, option2, option3, option4, feedbackLabel);
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    //Μέθοδος που ενημερώνει την πρόοδο (μπάρα & ετικετα), χρησιμοποιείται στη loadQuestion
    private void updateProgress() {
        double progress = (double) currentQuestionIndex / questions.size();
        progressBar.setProgress(progress);
        progressLabel.setText("Ερώτηση " + (currentQuestionIndex + 1) + " / " + questions.size());
    }

    // Μέθοδοι για απενεργοποίση και επανενεργοποίηση των κουμπιών, χρησιμοποιούνται στη handleAnswer και loadQuestion αντίστοιχα
    private void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }
    
    private void enableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(false);
        }
    }
}
