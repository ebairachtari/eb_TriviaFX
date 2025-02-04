package eb_TriviaApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import eb_services.TriviaAPIService;

public class MainSceneCreator {

    // Μεταβλητές 
	private final App app; // Κύριο αντικείμενο της εφαρμογής
    private Map<String, Integer> categoriesMap; // Χάρτης με κατηγορίες και τα ID τους

    //Constructor της MainSceneCreator
    public MainSceneCreator(App app) {
        this.app = app;
    }

    // Μέθοδος για τη δημιουργία της αρχικής σκηνής του παιχνιδιού
    public Scene createScene() {
        // Δημιουργία του κύριου layout με κάθετα ευθυγραμμισμένα στοιχεία
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f0f0f0;");

        // Προσθήκη ετικέτας καλωσορίσματος
        Label welcomeLabel = new Label("Καλώς ήρθατε στο Παιχνίδι Γενικών Γνώσεων!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Δημιουργία κάρτας για τις επιλογές
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        card.setAlignment(Pos.CENTER);

        // Ρύθμιση επιλογής κατηγορίας
        Label categoryLabel = new Label("Επιλέξτε κατηγορία:");
        categoryLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setPrefWidth(400);

        try {
            // Κλήση API για ανάκτηση κατηγοριών
            TriviaAPIService triviaService = new TriviaAPIService();
            categoriesMap = triviaService.getCategories();
            System.out.println("Categories Map: " + categoriesMap);

            List<String> categories = new ArrayList<>(categoriesMap.keySet());

            // Προσθήκη επιλογής "Any Category" (χωρίς ID)
            categories.add(0, "Any Category");
            categories.sort(String::compareToIgnoreCase); // Ταξινόμηση κατηγοριών αλφαβητικά
            categoryBox.getItems().addAll(categories);

            // Ορισμός προεπιλεγμένης κατηγορίας
            categoryBox.setValue(app.getSelectedCategory() != null ? app.getSelectedCategory() : "Any Category");

        } catch (Exception e) {
            // Διαχείριση σφάλματος αν αποτύχει το API
            categoryBox.getItems().add("Σφάλμα φόρτωσης κατηγοριών");
            categoryBox.setValue("Σφάλμα φόρτωσης κατηγοριών");
            System.out.println("Error fetching categories: " + e.getMessage());
            e.printStackTrace();
        }

        // Ενημέρωση της επιλεγμένης κατηγορίας στην εφαρμογή
        categoryBox.setOnAction(e -> {
            String selectedCategory = categoryBox.getValue();
            if ("Any Category".equals(selectedCategory)) {
                app.setSelectedCategory(null); // Ορισμός null αν είναι "Any Category"
            } else {
                app.setSelectedCategory(selectedCategory);
            }
        });

        // Δημιουργία ComboBox για επιλογή τύπου και mapping για να εμφανίζονται στα ελληνικά
        Map<String, String> typeTranslations = Map.of(
            "Any Type", "Όλα",
            "multiple", "Πολλαπλής Επιλογής",
            "boolean", "Σωστό/Λάθος"
        );
        List<String> typeOrder = List.of("Any Type", "multiple", "boolean");

        Label typeLabel = new Label("Επιλέξτε τύπο:");
        typeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeOrder.forEach(key -> typeBox.getItems().add(typeTranslations.get(key))); // Προσθήκη μεταφρασμένων επιλογών
        typeBox.setValue(typeTranslations.get(app.getSelectedType())); // Προεπιλεγμένη τιμή από την Αpp
        typeBox.setOnAction(e -> app.setSelectedType(
            typeOrder.stream()
                .filter(key -> typeTranslations.get(key).equals(typeBox.getValue())) // Εύρεση τιμής API
                .findFirst()
                .orElse("Any Type")
        ));

        // Δημιουργία ComboBox για επιλογή δυσκολίας και mapping για να εμφανίζονται στα ελληνικά, ίδια λογική με τον τύπο
        Map<String, String> difficultyTranslations = Map.of(
            "easy", "Εύκολη",
            "medium", "Μεσαία",
            "hard", "Δύσκολη"
        );
        List<String> difficultyOrder = List.of("easy", "medium", "hard");

        Label difficultyLabel = new Label("Επιλέξτε δυσκολία:");
        difficultyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyOrder.forEach(key -> difficultyBox.getItems().add(difficultyTranslations.get(key)));
        difficultyBox.setValue(difficultyTranslations.get(app.getSelectedDifficulty())); 
        difficultyBox.setOnAction(e -> app.setSelectedDifficulty(
            difficultyOrder.stream()
                .filter(key -> difficultyTranslations.get(key).equals(difficultyBox.getValue()))
                .findFirst()
                .orElse("easy")
        ));

        // Δημιουργία ComboBox για επιλογή αριθμού ερωτήσεων, ίδια λογική με παραπάνω
        Label questionsLabel = new Label("Επιλέξτε αριθμό ερωτήσεων:");
        questionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<Integer> questionsBox = new ComboBox<>();
        questionsBox.getItems().addAll(5, 10, 15, 20);
        questionsBox.setValue(app.getSelectedNumberOfQuestions());
        questionsBox.setOnAction(e -> app.setSelectedNumberOfQuestions(questionsBox.getValue()));

        // Δημιουργία κουμπιού έναρξης παιχνιδιού
        Button startButton = new Button("Έναρξη Παιχνιδιού");
        startButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20 10 20;");
        startButton.setOnAction(e -> {
            Integer selectedCategoryId = null;

            // Έλεγχος αν έχει επιλεγεί το "Any Category"
            if (!"Any Category".equals(categoryBox.getValue()) && categoriesMap != null) {
                selectedCategoryId = categoriesMap.get(categoryBox.getValue());
            }

            System.out.println("Selected Category: " + categoryBox.getValue());
            System.out.println("Selected Category ID: " + selectedCategoryId);

            GameSceneCreator gameSceneCreator = new GameSceneCreator(
                app,
                app.getSelectedDifficulty(),
                app.getSelectedType(),
                app.getSelectedNumberOfQuestions(),
                selectedCategoryId
            );
            Scene gameScene = gameSceneCreator.createScene();
            App.changeScene(gameScene);
        });

        // Προσθήκη ολων των στοιχείων στην κάρτα επιλογής
        card.getChildren().addAll(
            categoryLabel, categoryBox,
            typeLabel, typeBox,
            difficultyLabel, difficultyBox,
            questionsLabel, questionsBox,
            startButton
        );
        
        // Προσθήκη τηε ετικέτας για το welcome
        root.getChildren().addAll(welcomeLabel, card);
        
        // Δημιουργία και επιστροφή της Scene
        return new Scene(root, 700, 500);
    }
}
