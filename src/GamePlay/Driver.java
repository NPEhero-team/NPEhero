/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package GamePlay;

import javafx.application.*;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
        primaryPane.getChildren().add(player);
        setBackground("assets/water.png");  
        primaryStage.show();
        player.init();
	}
	
	  public static void setBackground(String url)
      {
          primaryPane.setBackground(new Background(
              new BackgroundImage(
                      new Image(url),
                      BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                      new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                      new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
              )));
      }
}
