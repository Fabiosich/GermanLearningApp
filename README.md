# German Translator Application

## Description

The German Translator Application is a JavaFX desktop application designed to help users learn German vocabulary by translating German words into Portuguese. The application provides a challenging quiz mode where users are presented with German words and must input the corresponding Portuguese translation. Additionally, users can insert new word pairs into the application's database for personalized learning.

## Features

1. **Start Challenge:** Begin a translation challenge where users are presented with German words to translate into Portuguese.

2. **Insert Word:** Add new German word pairs and their Portuguese translations to the application's database.

3. **Show All Words:** View a list of all German words and their corresponding Portuguese translations stored in the application.

4. **Save and Load:** Words are saved to and loaded from a text file to persist user data across sessions.

5. **Error Handling:** User inputs are validated, and appropriate error messages are displayed if necessary.

## How to Use

1. **Starting the Application:**
   - Run the `GermanTranslatorAppFX` class to start the application.
   - Upon starting, the application presents an initial layout with options to start a challenge, insert a new word, or close the application.

2. **Starting a Challenge:**
   - Click on the "Start Challenge" button to begin a translation challenge.
   - Users are presented with German words to translate into Portuguese. Input the translation and click "Check" or press Enter.

3. **Inserting a New Word:**
   - Click on the "Insert Word" button to add a new German word pair and its translation.
   - Input the German word and its Portuguese translation, then click "Insert Word."

4. **Viewing All Words:**
   - Click on the "Show All Words" button to view a list of all German words and their translations stored in the application.

5. **Closing the Application:**
   - Click on the "Close" button to close the application. Changes are saved automatically.

## Requirements

- Java Development Kit (JDK) 8 or higher.
- JavaFX library.
- IDE with Java support (e.g., IntelliJ IDEA, Eclipse).

## File Structure

- `src/`: Contains the Java source files.
  - `GermanTranslatorAppFX.java`: Main application class.
  - `WordPair.java`: Defines the `WordPair` class for storing German words and their translations.
- `src/main/resources/`: Contains application resources.
  - `words.txt`: Text file for storing word pairs.
- `README.md`: Instructions and information about the application.

## Author

[Your Name]

## License

This project is licensed under the [MIT License](LICENSE).
