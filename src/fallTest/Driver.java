/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package fallTest;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Driver extends Application
{

    static Pane primaryPane = new Pane();

    public static void main(String[] args)
    {
        launch(args);

    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene primaryScene = new Scene(primaryPane, 800, 600);
        primaryScene.getStylesheets().add("gui/style.css");
        primaryStage.setScene(primaryScene);
        newSongPlayer player = new newSongPlayer();
        primaryStage.setTitle("TEST");
        primaryStage.show();
        primaryPane.getChildren().add(player);
        player.init();
        
	}

}
