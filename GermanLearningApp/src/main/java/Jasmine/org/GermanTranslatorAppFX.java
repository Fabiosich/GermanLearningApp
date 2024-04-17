package Jasmine.org;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GermanTranslatorAppFX extends Application {

    private Stage primaryStage;
    private List<WordPair> wordPairs = new ArrayList<>();
    private List<WordPair> failedWords = new ArrayList<>();
    private int currentWordIndex = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int inputCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("German Translator");

        // Load words from the text file
        loadWordsFromFile("src/main/resources/words.txt");

        // Initial layout with Start Challenge and Insert Word buttons
        createInitialLayout();

        primaryStage.show();
    }

    private void createInitialLayout() {
        GridPane initialGrid = new GridPane();
        initialGrid.setHgap(10);
        initialGrid.setVgap(10);
        initialGrid.setPadding(new Insets(10, 10, 10, 10));

        Button startChallengeButton = new Button("Start Challenge");
        Button insertWordButton = new Button("Insert Word");
        Button editDatabaseButton = new Button("Edit Database"); // New button for editing the database
        Button closeApp = new Button("Close");

        // Event handler for the "Start Challenge" button
        startChallengeButton.setOnAction(e -> startChallenge());

        // Event handler for the "Insert Word" button
        insertWordButton.setOnAction(e -> insertWord());

        // Event handler for the "Edit Database" button
        editDatabaseButton.setOnAction(e -> editDatabase());

        // Envent handler for the "Close" button
        closeApp.setOnAction(e -> closeApp());

        // Load the image
        Image image = new Image("27101.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // Set the width of the image
        imageView.setFitHeight(100); // Set the height of the image

        initialGrid.add(startChallengeButton, 0, 0);
        initialGrid.add(insertWordButton, 0, 1);
        initialGrid.add(editDatabaseButton, 0, 2); // Add the "Edit Database" button
        initialGrid.add(closeApp, 0, 2);
        initialGrid.add(imageView, 1, 0, 1, 2); // Add the image to the right side

        GridPane.setMargin(startChallengeButton, new Insets(0, 0, 0, 0));
        GridPane.setMargin(insertWordButton, new Insets(0, 0, 0, 0));
        GridPane.setMargin(editDatabaseButton, new Insets(0, 0, 0, 0)); // Set margin for the "Edit Database" button
        GridPane.setMargin(closeApp, new Insets(0, 0, 0, 0));

        Button showAllWordsButton = new Button("Show All Words");

        // Event handler for the "Show All Words" button
        showAllWordsButton.setOnAction(e -> showAllWords());

        initialGrid.add(showAllWordsButton, 0, 3); // Add the "Show All Words" button

        GridPane.setMargin(showAllWordsButton, new Insets(0, 0, 0, 0)); // Configure the "Show All Words" button to align to the left



        Scene scene = new Scene(initialGrid, 350, 250);
        primaryStage.setScene(scene);
    }

    private void editDatabase() {
        // Create a TableView to display the database elements
        TableView<WordPair> tableView = new TableView<>();
        tableView.setEditable(true);

        // Define columns for German word and Portuguese translation
        TableColumn<WordPair, String> germanWordColumn = new TableColumn<>("German Word");
        germanWordColumn.setCellValueFactory(new PropertyValueFactory<>("germanWord"));
        germanWordColumn.setPrefWidth(150);
        germanWordColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Allow cell editing

        TableColumn<WordPair, String> portugueseTranslationColumn = new TableColumn<>("Portuguese Translation");
        portugueseTranslationColumn.setCellValueFactory(new PropertyValueFactory<>("portugueseTranslation"));
        portugueseTranslationColumn.setPrefWidth(200);
        portugueseTranslationColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Allow cell editing

        // Add columns to the TableView
        tableView.getColumns().addAll(germanWordColumn, portugueseTranslationColumn);

        // Load data into the TableView
        ObservableList<WordPair> data = FXCollections.observableArrayList(wordPairs);
        tableView.setItems(data);

        // Enable cell editing events
        tableView.setEditable(true);
        germanWordColumn.setOnEditCommit(event -> {
            WordPair wordPair = event.getRowValue();
            wordPair.setGermanWord(event.getNewValue());
        });
        portugueseTranslationColumn.setOnEditCommit(event -> {
            WordPair wordPair = event.getRowValue();
            wordPair.setPortugueseTranslation(event.getNewValue());
        });

        // Create a scene for editing the database
        Scene editDatabaseScene = new Scene(tableView, 600, 400); // Increase the size of the scene

        // Set the scene to the primary stage
        primaryStage.setScene(editDatabaseScene);
    }



    private void closeApp() {
        // Save the words to the text file before closing
        saveWordsToFile("/Applications/GermanLearning/GermanLearningApp/src/main/resources/words.txt");
        primaryStage.close();
    }

    private void startChallenge() {
        // Reset counts, failed words, and shuffle the list for a new random order
        correctCount = 0;
        incorrectCount = 0;
        inputCount = 0;
        currentWordIndex = 0;
        failedWords.clear();

        // Shuffle the list for a new random order
        Collections.shuffle(wordPairs);

        // Challenge layout
        createChallengeLayout();

        // Show the challenge layout
        primaryStage.show();
    }

    private void createChallengeLayout() {
        GridPane challengeGrid = new GridPane();
        challengeGrid.setHgap(10);
        challengeGrid.setVgap(10);
        challengeGrid.setPadding(new Insets(20, 20, 20, 20));

        Label wordLabel = new Label(wordPairs.get(currentWordIndex).getGermanWord());
        TextField translationField = new TextField();
        Button checkButton = new Button("Check");

        // Event handler for the Check button
        checkButton.setOnAction(e -> checkTranslation(wordLabel, translationField, checkButton));

        // Event handler for the Enter key in the TextField
        translationField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkTranslation(wordLabel, translationField, checkButton);
            }
        });

        challengeGrid.add(wordLabel, 0, 0, 3, 1); // Span the label across three columns
        challengeGrid.add(translationField, 0, 1, 2, 1); // Span the text field across two columns
        challengeGrid.add(checkButton, 2, 1);

        Scene challengeScene = new Scene(challengeGrid, 400, 300);
        primaryStage.setScene(challengeScene);
    }

    private void checkTranslation(Label wordLabel, TextField translationField, Button checkButton) {
        String userTranslation = translationField.getText().trim();
        String correctTranslation = wordPairs.get(currentWordIndex).getPortugueseTranslation();

        if (userTranslation.equalsIgnoreCase(correctTranslation)) {
            wordLabel.setText("Correct!");
            correctCount++;
        } else {
            wordLabel.setText("Incorrect. The correct translation is: " + correctTranslation);
            failedWords.add(wordPairs.get(currentWordIndex));
            incorrectCount++;
        }

        inputCount++;

        if (inputCount == 10) {
            showResults();
        } else {
            // Move to the next word
            currentWordIndex = (currentWordIndex + 1) % wordPairs.size();
            wordLabel.setText(wordPairs.get(currentWordIndex).getGermanWord());
            translationField.clear();
        }
    }

    private void showResults() {
        GridPane resultsGrid = new GridPane();
        resultsGrid.setHgap(10);
        resultsGrid.setVgap(10);
        resultsGrid.setPadding(new Insets(20, 20, 20, 20));

        Label correctLabel = new Label("Correct Answers: " + correctCount);
        Label incorrectLabel = new Label("Incorrect Answers: " + incorrectCount);

        Button showFailedButton = new Button("Show Failed Words");

        showFailedButton.setOnAction(e -> showFailedWords());

        Button restartButton = new Button("Restart");

        restartButton.setOnAction(e -> {
            // Reset counts, failed words, and shuffle the list for a new random order
            correctCount = 0;
            incorrectCount = 0;
            inputCount = 0;
            currentWordIndex = 0;
            failedWords.clear();

            // Shuffle the list for a new random order
            Collections.shuffle(wordPairs);

            // Initial layout with Start Challenge and Insert Word buttons
            createInitialLayout();

            // Show the initial layout
            primaryStage.show();
        });

        resultsGrid.add(correctLabel, 0, 0);
        resultsGrid.add(incorrectLabel, 0, 1);
        resultsGrid.add(showFailedButton, 0, 2);
        resultsGrid.add(restartButton, 0, 3);

        Scene resultsScene = new Scene(resultsGrid, 400, 300);
        primaryStage.setScene(resultsScene);
    }

    private void showFailedWords() {
        GridPane failedWordsGrid = new GridPane();
        failedWordsGrid.setHgap(10);
        failedWordsGrid.setVgap(10);
        failedWordsGrid.setPadding(new Insets(20, 20, 20, 20));

        Label failedWordsLabel = new Label("Failed Words:");

        // Display the failed words
        for (int i = 0; i < failedWords.size(); i++) {
            Label wordLabel = new Label(failedWords.get(i).getGermanWord() + " -> " + failedWords.get(i).getPortugueseTranslation());
            failedWordsGrid.add(wordLabel, 0, i + 1);
        }

        Button restartButton = new Button("Restart");

        restartButton.setOnAction(e -> {
            // Reset counts, failed words, and shuffle the list for a new random order
            correctCount = 0;
            incorrectCount = 0;
            inputCount = 0;
            currentWordIndex = 0;
            failedWords.clear();

            // Shuffle the list for a new random order
            Collections.shuffle(wordPairs);

            // Initial layout with Start Challenge and Insert Word buttons
            createInitialLayout();

            // Show the initial layout
            primaryStage.show();
        });

        failedWordsGrid.add(failedWordsLabel, 0, 0);
        failedWordsGrid.add(restartButton, 0, failedWords.size() + 1);

        Scene failedWordsScene = new Scene(failedWordsGrid, 400, 300);
        primaryStage.setScene(failedWordsScene);
    }

    private void insertWord() {
        // Insert Word layout
        createInsertWordLayout();

        // Show the Insert Word layout
        primaryStage.show();
    }

    private void createInsertWordLayout() {
        GridPane insertWordGrid = new GridPane();
        insertWordGrid.setHgap(10);
        insertWordGrid.setVgap(10);
        insertWordGrid.setPadding(new Insets(20, 20, 20, 20));

        Label newWordLabel = new Label("New German Word:");
        TextField newWordField = new TextField();
        Label newTranslationLabel = new Label("Translation:");
        TextField newTranslationField = new TextField();
        Button insertButton = new Button("Insert Word");

        // Event handler for the "Insert Word" button
        insertButton.setOnAction(e -> insertNewWord(newWordField.getText(), newTranslationField.getText()));

        insertWordGrid.add(newWordLabel, 0, 0);
        insertWordGrid.add(newWordField, 1, 0);
        insertWordGrid.add(newTranslationLabel, 0, 1);
        insertWordGrid.add(newTranslationField, 1, 1);
        insertWordGrid.add(insertButton, 0, 2);


        Scene insertWordScene = new Scene(insertWordGrid, 400, 300);
        primaryStage.setScene(insertWordScene);
    }

    private void insertNewWord(String newWord, String newTranslation) {
        if (!newWord.isEmpty() && !newTranslation.isEmpty()) {
            // Add the new word to the list
            WordPair newWordPair = new WordPair(newWord, newTranslation);
            wordPairs.add(newWordPair);

            // Save the updated list to the text file
            saveWordsToFile("words.txt");

            // Display a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Word Inserted");
            alert.setHeaderText(null);
            alert.setContentText("The new word has been inserted.");

            alert.showAndWait();

            // Go back to the initial layout after inserting the word
            createInitialLayout();
            primaryStage.show();
        } else {
            // Display an error message if the user didn't provide both the word and translation
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both the word and its translation.");

            alert.showAndWait();
        }
    }

    private void loadWordsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    WordPair wordPair = new WordPair(parts[0], parts[1]);
                    wordPairs.add(wordPair);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveWordsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (WordPair wordPair : wordPairs) {
                writer.write(wordPair.getGermanWord() + "," + wordPair.getPortugueseTranslation());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class WordPair {
        private String germanWord;
        private String portugueseTranslation;

        public WordPair(String germanWord, String portugueseTranslation) {
            this.germanWord = germanWord;
            this.portugueseTranslation = portugueseTranslation;
        }

        public String getGermanWord() {
            return germanWord;
        }

        public void setGermanWord(String germanWord) {
            this.germanWord = germanWord;
        }

        public String getPortugueseTranslation() {
            return portugueseTranslation;
        }

        public void setPortugueseTranslation(String portugueseTranslation) {
            this.portugueseTranslation = portugueseTranslation;
        }
    }


    private void showAllWords() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("All Words");
        alert.setHeaderText("List of All Words:");

        StringBuilder wordsList = new StringBuilder();

        for (WordPair wordPair : wordPairs) {
            wordsList.append(wordPair.getGermanWord())
                    .append(" -> ")
                    .append(wordPair.getPortugueseTranslation())
                    .append("\n");
        }

        alert.setContentText(wordsList.toString());

        alert.showAndWait();
    }
}
