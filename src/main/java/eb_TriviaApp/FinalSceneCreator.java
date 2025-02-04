package eb_TriviaApp;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FinalSceneCreator {

    // Μεταβλητές 
    private final App app; // Αναφορά στην κύρια εφαρμογή
    private final int score; // Το τελικό σκορ του χρήστη
    private final int totalQuestions; // Το συνολικό πλήθος ερωτήσεων

    // Constructor της FinalSceneCreator
    public FinalSceneCreator(App app, int score, int totalQuestions) {
        this.app = app;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    // Μέθοδος για τη δημιουργία της τελικής σκηνής
    public Scene createScene() {
        // Δημιουργία βασικού layout (VBox) για κάθετη διάταξη στοιχείων
        VBox root = new VBox(20); 
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        // Ετικέτα που ανακοινώνει το τέλος του παιχνιδιού
        Label titleLabel = new Label("Τέλος παιχνιδιού!");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-fill: #333333;");

        // Ετικέτα που εμφανίζει το τελικό σκορ
        Label scoreLabel = new Label("Τελικό σκορ: " + score + "/" + (totalQuestions * 10));
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333333;");

        // Υπολογισμός ποσοστού επιτυχίας και εμφάνισή του
        double percentage = ((double) score / (totalQuestions * 10)) * 100;
        Label percentageLabel = new Label("Ποσοστό: " + String.format("%.2f", percentage) + "%");
        percentageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333333;");

        // Εμφάνιση μηνύματος για νέο υψηλότερο σκορ, αν υπάρχει
        int highScore = app.getHighScore();
        if (!app.isFirstGame() && score > highScore) {
            app.setHighScore(score); // Ενημέρωση του νέου υψηλότερου σκορ
            handleHighScore(root);
        }

        // Ρύθμιση του πρώτου παιχνιδιού
        app.setFirstGame(false);

        // Κουμπί επιστροφής στην αρχική οθόνη
        Button restartButton = new Button("Επιστροφή στην αρχική οθόνη");
        restartButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        restartButton.setOnAction(e -> 
            // Αλλαγή σκηνής στην αρχική μενού χρησιμοποιώντας τη MainSceneCreator
            app.changeScene(new MainSceneCreator(app).createScene())
        );

        // Προσθήκη όλων των στοιχείων στο layout
        root.getChildren().addAll(titleLabel, scoreLabel, percentageLabel, restartButton);

        // Επιστροφή στην τελική σκηνή
        return new Scene(root, 800, 500);
    }

    // Μέθοδος για την εμφάνιση του νέου υψηλότερου σκορ
    private void handleHighScore(VBox root) {
    	// Δημιουργία ετικέτας για το νέο υψηλό σκορ
        Label highScoreLabel = new Label("Νέο Υψηλότερο Σκορ!");
        highScoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green;");
        
        // Ρυθμίσεις για το εφέ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), highScoreLabel);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), highScoreLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        
        // Εκκίνηση των εφέ
        scaleTransition.play();
        fadeTransition.play();
        
        // Προσθήκη της ετικέτας στην κορυφή του VBox
        root.getChildren().add(0, highScoreLabel);
    }
}
