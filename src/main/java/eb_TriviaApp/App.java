package eb_TriviaApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    // Μεταβλητές 
    private static Stage primaryStage; // Στατική αναφορά στο κύριο παράθυρο της εφαρμογής
    private static Scene mainScene; // Στατική μεταβλητή για την αρχική σκηνή
    private String selectedDifficulty = "easy"; // Επιλογή δυσκολίας (προεπιλογή: easy)
    private String selectedType = "Any Type"; // Επιλογή τύπου ερωτήσεων (προεπιλογή: Any Type)
    private int selectedNumberOfQuestions = 5; // Αριθμός ερωτήσεων (προεπιλογή: 5)
    private String selectedCategory = null; // Επιλογή κατηγορίας (null σημαίνει "Any Category")
    private int highScore = 0; // Υψηλότερο σκορ
    private boolean isFirstGame = true; // Έλεγχος αν είναι το πρώτο παιχνίδι

    // Getters & setters για τη δυσκολία
    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }
    public void setSelectedDifficulty(String selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
    }

    // Getters & setters για τον τύπο 
    public String getSelectedType() {
        return selectedType;
    }
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    // Getters & setters για τον αριθμό ερωτήσεων 
    public int getSelectedNumberOfQuestions() {
        return selectedNumberOfQuestions;
    }
    public void setSelectedNumberOfQuestions(int selectedNumberOfQuestions) {
        this.selectedNumberOfQuestions = selectedNumberOfQuestions;
    }

    // Getters & setters για την κατηγορία
    public String getSelectedCategory() {
        return selectedCategory != null ? selectedCategory : "Any Category";
    }
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = "Any Category".equals(selectedCategory) ? null : selectedCategory;
    }
    
    // Getters & setters για το μέγιστο σκορ
    public int getHighScore() {
        return highScore;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    // Getters & setters για το πρώτο παιχνίδι
    public boolean isFirstGame() {
        return isFirstGame;
    }
    public void setFirstGame(boolean isFirstGame) {
        this.isFirstGame = isFirstGame;
    }

    // Κύρια μέθοδος εκκίνησης της εφαρμογής - Καλείται αυτόματα όταν ξεκινήσει η εφαρμογή μέσω της launch(args) της main
    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Αποθήκευση της αναφοράς στο κύριο παράθυρο

        // Δημιουργία της αρχικής σκηνής 
        MainSceneCreator mainSceneCreator = new MainSceneCreator(this);
        mainScene = mainSceneCreator.createScene();

        // Ρύθμιση και εμφάνιση της αρχικής σκηνής
        primaryStage.setTitle("Παιχνίδι Γενικών Γνώσεων");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false); // Απενεργοποίηση αλλαγής μεγέθους του παραθύρου
        primaryStage.show();
    }

    //Μέθοδος για εναλλαγή σκηνής κατά τη διάρκεια της εφαρμογής.
    public static void changeScene(Scene newScene) {
        primaryStage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch(args); // Εκκίνηση της εφαρμογής
    }
}
