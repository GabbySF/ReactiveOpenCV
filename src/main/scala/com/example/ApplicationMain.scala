package com.example

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.CvType
import org.opencv.highgui.VideoCapture
import org.opencv.highgui.Highgui
import java.awt.image.BufferedImage
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.scene.text.Text
import org.opencv.core.MatOfByte
import javafx.scene.image.Image
import java.io.ByteArrayInputStream
import javafx.scene.image.ImageView
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import scalafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.layout.GridPane
import scalafx.application.JFXApp.PrimaryStage
import java.util.TimerTask
import scalafx.application.Platform
import java.util.Timer
import org.opencv.objdetect.CascadeClassifier
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Scalar
import javafx.scene.control.Slider
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.opencv.imgproc.Imgproc



object ApplicationMain extends JFXApp {
  var hue = 0
  var saturation = 0
  var value = 0
	System.loadLibrary( Core.NATIVE_LIBRARY_NAME )
	val webcam:VideoCapture = new VideoCapture()
	webcam.open(0)
	//val faceDetector = new CascadeClassifier("/usr/local/share/OpenCV/lbpcascades/lbpcascade_frontalface.xml")

	assert(webcam.isOpened())
	stage = new PrimaryStage()
	val root:Group = new Group()
	val gridpane:GridPane = new GridPane()
	gridpane.setVgap(10);

	val tmpSize:Image = getCameraImage;
	val scene:Scene = new Scene(root, tmpSize.getWidth, tmpSize.getHeight+300, Color.WHITE)

	val imview:ImageView = new ImageView()
	val hSlider = new Slider()
	hSlider.setMin(0)
	hSlider.setMax(100)
  
  val sSlider = new Slider()
  sSlider.setMin(0)
  sSlider.setMax(100)
  
  val vSlider = new Slider()
  vSlider.setMin(0)
  vSlider.setMax(100)
  
  
	hSlider.valueProperty().addListener(new ChangeListener[Number] {
		@Override
		def changed(o: ObservableValue[_ <: Number], oldVal: Number, newVal: Number) {
			hue =newVal.intValue()
		}
	})
  
  sSlider.valueProperty().addListener(new ChangeListener[Number] {
    @Override
    def changed(o: ObservableValue[_ <: Number], oldVal: Number, newVal: Number) {
      saturation =newVal.intValue()
    }
  })
 
  vSlider.valueProperty().addListener(new ChangeListener[Number] {
    @Override
    def changed(o: ObservableValue[_ <: Number], oldVal: Number, newVal: Number) {
      value =newVal.intValue()
    }
  })

	val box:HBox = new HBox()
	box.getChildren().add(imview)
	gridpane.add(box, 1, 1);  
	gridpane.add(hSlider,1,2)
  gridpane.add(sSlider,1,2)
  gridpane.add(vSlider,1,2)

	root.getChildren().add(gridpane);  
	stage.setScene(scene)
	stage.show()

	val timeTask:TimerTask = new TimerTask(){
		override def run {
			val tmp:Image = getCameraImage;
		Platform.runLater(new Runnable(){
			override def run {
				imview.setImage(tmp)
			}
		})
		}
	}
	val timer:Timer= new Timer
	timer.schedule(timeTask, 0, 33)


  def getCameraImage:Image = {
		val frame:Mat = new Mat()
		webcam.read(frame)
    
    val hsv:Mat = new Mat();
    Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		//val faceDetections:MatOfRect = new MatOfRect();
		// faceDetector.detectMultiScale(frame, faceDetections);

		//for(rect <- faceDetections.toArray()){
		//    Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		//}
    
    val tmp:Mat = new Mat();
    Core.inRange(hsv, new Scalar(1,2,3), new Scalar(1,2,3), tmp)

		val byteMat:MatOfByte=new MatOfByte()
		Highgui.imencode(".bmp", tmp, byteMat);
		val image:Image = new Image(new ByteArrayInputStream(byteMat.toArray()))

		image
	}


}