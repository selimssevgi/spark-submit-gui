package com.selimssevgi.sparksubmitgui;

import com.selimssevgi.sparksubmitgui.model.SparkJob;
import com.selimssevgi.sparksubmitgui.view.SparkJobOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ssselim on 9/27/16.
 */
public class App extends Application{

  private Stage primaryStage;
  private BorderPane rootLayout;

  private ObservableList<SparkJob> sparkJobsData = FXCollections.observableArrayList();

  /**
   * Constructor
   */
  public App() {
    final String exPath = "/usr/local/spark/examples/jars/spark-examples_2.11-2.0.0.jar";
    // Add some sample data
    sparkJobsData.add(new SparkJob("Spark PI",
            "spark-submit --class org.apache.spark.examples.SparkPi --master local[*] " + exPath + " 100"));
    sparkJobsData.add(new SparkJob("Scenario B", "Some script"));
    sparkJobsData.add(new SparkJob("Scenario C", "Some script"));
    sparkJobsData.add(new SparkJob("Scenario D", "Some script"));
    sparkJobsData.add(new SparkJob("Scenario E", "Some script"));
    sparkJobsData.add(new SparkJob("Scenario F", "Some script"));
  }

  /**
   * returns the data as an observable list of sparkJobs
   */
  public ObservableList<SparkJob> getSparkJobsData() {
    return sparkJobsData;
  }

  /**
   * The start() method is the main entry point for all JavaFX applications.
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Spark-Submit GUI");

    initRootLayout();

    showSparkJobOverview();
  }

  /**
   * shows spark submit related ui in the root layout
   */
  private void showSparkJobOverview() {
    try {
      // load spark submit overview
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/SparkJobOverview.fxml"));
      AnchorPane sparkSubmitOverview = loader.load();

      // set sparksubmit overview into the center of root layout
      rootLayout.setCenter(sparkSubmitOverview);

      // give the controller access to the app
      SparkJobOverviewController controller = loader.getController();
      controller.setApp(this);
    } catch (IOException e) {
      System.err.println("Could not load the sparksubmitoverview.fxml");
      e.printStackTrace();
    }
  }

  /**
   * Initializes the root layout
   */
  private void initRootLayout() {
    try {
      // load root layout from fxml file
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("view/RootLayout.fxml"));
      rootLayout = loader.load();

      // show the scene containing the root layout
      Scene scene = new Scene(rootLayout);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      System.err.println("Could not load rootlayout...");
      e.printStackTrace();
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void main(String[] args)  {
    launch(args);
  }
}
