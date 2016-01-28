package frcimageprocessor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VideoController
{
	@FXML
	private Button button;
	@FXML
	private CheckBox grayscale;
	@FXML
	private CheckBox track;
	@FXML
	private ImageView currentFrame;
	
	private ScheduledExecutorService timer;
	private VideoCapture capture;
	private boolean cameraActive;
	public void initialize()
	{
		this.capture = new VideoCapture();
		this.cameraActive = false;
	}
	@FXML
	protected void startCamera()
	{
		this.currentFrame.setFitWidth(600);
		this.currentFrame.setPreserveRatio(true);
		if (!this.cameraActive)
		{
			this.capture.open(0);
			if (this.capture.isOpened())
			{
				this.cameraActive = true;
				Runnable frameGrabber = new Runnable() {
					@Override
					public void run()
					{
						Image imageToShow = grabFrame();
						currentFrame.setImage(imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				this.button.setText("Stop Camera");
			}
			else
			{
				System.err.println("Cannot connect to camera.");
			}
		}
		else
		{
			this.cameraActive = false;
			this.button.setText("Start Camera");
			try
			{
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				System.err.println("Exception in stopping the frame capture, trying to release the camera now." + e);
			}
			this.capture.release();
			this.currentFrame.setImage(null);
		}
	}
	private Image grabFrame()
	{
		Image imageToShow = null;
		//Mat frame = new Mat();
		Mat frame = new Mat(frame1.getHeight(), frame1.getWidth(), CvType.CV_8UC3);
		byte[] pixels = ((DataBufferByte)frame1.getRaster().getDataBuffer()).getData();
		frame.put(0, 0, pixels);
		if (this.capture.isOpened())
		{
			try
			{
				//this.capture.read(frame);
				if (!frame.empty())
				{
					if (grayscale.isSelected())
					{
						Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
					}
					
					if (track.isSelected())
					{
						Scalar hsv_min = new Scalar(13, 50, 50, 0);  
					    Scalar hsv_max = new Scalar(6, 255, 255, 0);    
						Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
						Core.inRange(frame, hsv_min, hsv_max, frame);
					}
					imageToShow = mat2Image(frame);
				}
				
			}
			catch (Exception e)
			{
				System.err.println("Exception during the frame elaboration: " + e);
			}
		}
		return imageToShow;
	}
	private Image mat2Image(Mat frame)
	{
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	
}
