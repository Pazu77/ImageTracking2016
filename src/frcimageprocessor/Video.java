package frcimageprocessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

public class Video extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Video.fxml"));
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Video processing");
			primaryStage.setScene(scene);
			primaryStage.show();			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
		
	}
}