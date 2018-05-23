// Dillon Esponda
// Summer 2018 
// Bootcamp with Shawna
// With comments and stuff for Jin :)

/*
    So here's the deal with the controller file,
    It does all the back end stuff for the FXML document its associated with.

    All the logic that needs to happen behind the scenes for FXMLDocument.fxml
    gets handled in here.
*/
package guessinggame;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FXMLDocumentController implements Initializable
{ 
    /*
	So whats the deal with all these @FXML class variables?
	A @FXML tag tells the code to go look in the .fxml file for 
	the FX id that corresponds to the variable name.
    
	For example,
	@FXML 
	private Label label;
	
	lets break it down:
	@FXML tells the code to look in the .fxml file to find a Label object with the FXid 'label'
	and private is just the usual access modifier so we can only access this label inside this class
    
	but why do we need all these @FXML thingies anyways?
	answer: to connect the UI to code
	so if we were to change 'label', it would change on the UI.
    */
    
    @FXML
    private Label label;
    @FXML
    private Button guessButton;
    @FXML
    private TextField textField;
    
    private int lowerbound;
    private int upperbound;
    private int randomNum;
    
    /*
	Override here is the usual override of a superclass function
	
	the function 'initilize' is executed before anything is rendered out to the screen
	so this is an ideal place to do any set up or anything that would need to occur
	before the main scene is displayed
    */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {	
        // i put these into functions because a tidy 
        // and readable initialize function is important
        lowerbound = promptForLowerBound();
        upperbound = promptForUpperBound();
	
	Random rand = new Random();
	randomNum = rand.nextInt(upperbound - lowerbound) + lowerbound;
	
	label.setText("Number is between " + lowerbound + " and " + upperbound);
        
        // gross function name but it's descriptive enough to understand at a glance
        configureTextAreaToSubmitWhenEnterPressed();
    }    
    
    private int promptForLowerBound() {
        /*	
	    check out the JavaDocs for more info on TextInputDialogs
	    basically, they're  modal dialog boxes that pop up over the current view (if any)
	    and we're customizing it by changing is properties using the 'setters'
	*/
	TextInputDialog dialog = new TextInputDialog("0");
	dialog.setTitle("Welcome");
	dialog.setHeaderText("Welcome to this shitty game!");
	dialog.setContentText("Please enter the lower bound:");
	
	/* 
	    showAndWait() will display the window as a modal window, meaning
            the program will not continue code execution until the window is dismissed.
            
            showAndWait() also returns an Optional String, representing the text 
	    from the textArea in the dialog window
	    
	    basically, if the user hit 'Okay' in the dialogue,
	    even if the textfield is empty, it will return a non-null string
	    and 'input.isPresent()' will be true
	
	    but if the user hits the 'Cancel' button, or closes the window,
	    'input' will be empty, and as such, 'input.isPresent()' will 
	    return false
	*/
	Optional<String> input = dialog.showAndWait();    
	if (input.isPresent())
	{
	    // typical java exception-handling. the contents of a TextArea
	    // will always be a string, just like getting user input from terminal.
	    try {
		// we have to use .get() to get the contents of the Optional object.
		return Integer.parseInt(input.get());
	    } catch (Exception e) {
		// sets a default value if the user entered a non-numeric input
		return 0;
	    }
	}
	
	// if the user hit cancel or closed the window, lets just close the whole game
	else {
	    System.exit(1);
	    return 0;
	}
    }
    
    private int promptForUpperBound() {
        // same as the last dialog, except now we're getting the upperbound.
        int upperbound;
        
	TextInputDialog dialog = new TextInputDialog("100");
	dialog.setTitle("Welcome");
	dialog.setHeaderText("Welcome to this shitty game!");
	dialog.setContentText("Please enter the upper bound:");
	
        Optional<String> input = dialog.showAndWait();    
	if (input.isPresent())
	{
	    try {
		upperbound = Integer.parseInt(input.get());
		if (upperbound <= lowerbound)
		    upperbound = (lowerbound + 1) * 10;
	    } catch (Exception e) {
		upperbound = (lowerbound + 1) * 10;
	    }
	}
	else {
	    System.exit(1);
	    return 0;
	}
        return upperbound;
    }
    
    private void configureTextAreaToSubmitWhenEnterPressed() {
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    guessButtonPressed(null);
            }
        });
    }

    @FXML
    private void guessButtonPressed(ActionEvent event)
    {
	/*
	    most of this is straight-foward Java programming,
	    i'm only going to comment on certain JavaFX related things.
	    If you have any questions, please open an issue on Github
	*/
	
	String rawInput = textField.getText();
	int convertedInput;
	try {
	    convertedInput = Integer.parseInt(rawInput);
	} catch (Exception e) {
            displayInputFormatError(rawInput);
            
            // we set the text to an empty string to clear any user input
            textField.setText("");
            return;
        }
        
        if (convertedInput < lowerbound || convertedInput > upperbound) {
            displayGuessOutOfBoundsError(convertedInput);
            textField.setText("");
            return;
        }
	if (convertedInput < randomNum) 
            label.setText(convertedInput + " is too low! Guess higher!");
        else if (convertedInput > randomNum)
            label.setText(convertedInput + " is too high! Guess lower!");
        else 
            displayWinDialog();
        textField.setText("");
    }
    
    void displayInputFormatError(String incorrectInput) {
        Alert dialog = new Alert(AlertType.ERROR);
        dialog.setTitle("Error!");
        dialog.setHeaderText("Guess formatted incorrectly!");
        dialog.setContentText("Your guess must be a number! You entered \"" + incorrectInput + "\"!" );
        dialog.showAndWait();
    }
    
    void displayGuessOutOfBoundsError(int guess) {
        Alert dialog = new Alert(AlertType.ERROR);
        dialog.setTitle("Error!");
        dialog.setHeaderText("Guess out of bounds!");
        dialog.setContentText("Your guess must be a number between " + lowerbound + " and " + upperbound + "! You entered <" + guess + ">" );
        dialog.showAndWait();
        label.setText("Number is between " + lowerbound + " and " + upperbound);
    }
    
    void displayWinDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You guessed the correct number!");
        alert.showAndWait();
        
        // close the game after user closes alert
        System.exit(0);
    }
}
