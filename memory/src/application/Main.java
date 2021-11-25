package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage meineStage) throws Exception {
		FlowPane rootNode = new MemoryFeld().initGUI(new FlowPane());
		Scene meineScene = new Scene(rootNode, 480, 550);
		meineStage.setTitle("Memory");
		meineStage.setScene(meineScene);
		meineStage.setResizable(false);
		meineStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
