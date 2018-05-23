package guessinggame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dillon Esponda
 */
public class GuessingGame extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
	Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	
	Scene scene = new Scene(root);
	
	// The only things i've added here from the template is the next two lines of code
	
	// title of window
	stage.setTitle("Guessing Game");
	
	// make it non-resizable so we don't have to worry about adding constraints
	stage.setResizable(false);
	
	stage.setScene(scene);
	stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
	launch(args);
    }
    
}
