package com.selimssevgi.sparksubmitgui.view;

import com.selimssevgi.sparksubmitgui.App;
import com.selimssevgi.sparksubmitgui.model.SparkJobRunner;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.selimssevgi.sparksubmitgui.model.SparkJob;
import javafx.scene.control.TextArea;


/**
 * We must put it in the same package as the SparkJobOverview.fxml,
 * otherwise the SceneBuilder won't find it.
 *
 * Created by ssselim on 9/28/16.
 */
public class SparkJobOverviewController {
  @FXML
  private TableView<SparkJob> sparkJobTable;
  @FXML
  private TableColumn<SparkJob, String> sparkJobNameColumn;
  @FXML
  private TextArea sparkSubmitScript;
  @FXML
  private TextArea sparkJobOutput;

  // reference to main app
  private App app;



  /**
   * The constructor.
   * The constructor is called before the initialize() method.
   */
  public SparkJobOverviewController() {
  }

  /**
   * Initializes the controller class. This method is automatically called
   * after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    // Initialize table columns.
    sparkJobNameColumn.setCellValueFactory(cellData -> cellData.getValue().sparkNameProperty());

    // clear the spark job details
    showSparkJobDetails(null);

    // listen for selection changes and show the submit script of a scenario
    sparkJobTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showSparkJobDetails(newValue));

    // listen for output textarea changes and scroll to bottom of the box
    sparkJobOutput.textProperty().addListener( //this will scroll to the bottom
                                               //use Double.MIN_VALUE to scroll to the top
            (observable, oldValue, newValue) -> sparkJobOutput.setScrollTop(Double.MAX_VALUE));
  }

  /**
   * show the details of a spark job, in this case is just submit script
   * @param sparkJob
   */
  private void showSparkJobDetails(SparkJob sparkJob) {
    if (sparkJob != null) {
      // show the script
      sparkSubmitScript.setText(sparkJob.getSparkSubmitScript());
      sparkJobOutput.setText(sparkJob.getSparkOutputBuffer().toString());
    } else {
      // clear the text area
      sparkSubmitScript.setText("");
      sparkJobOutput.setText("");
    }

  }

  /**
   * Is called by the main application to give a reference back to itself.
   *
   * @param app
   */
  public void setApp(App app) {
    this.app = app;

    // Add observable list data to the table
    if (sparkJobTable == null) {
      System.out.println("sparktable is null");
    }
    sparkJobTable.setItems(app.getSparkJobsData());
  }

  /**
   * Called when the user clicked on start button
   */
  @FXML
  private void handleSparkSubmitJobStart() {
    // TODO: disable start button for this job
    // check if job is not done?
    int selectedIndex = sparkJobTable.getSelectionModel().getSelectedIndex();
    if (selectedIndex >= 0) {
      SparkJob job = sparkJobTable.getItems().get(selectedIndex);
      String script = sparkSubmitScript.getText();

      if ( script != null) {
        // TODO: maybe is better to give an option to edit script in a dialog?
        job.setSparkSubmitScript(script); // update script in case of any change
        SparkJobRunner runner = new SparkJobRunner(job);
        runner.start();

        // updates the output textarea
        Thread t1 = new Thread(() -> {
          while (!runner.getKilled()) {
            sparkJobOutput.setText(job.getSparkOutputBuffer().toString());
            sparkJobOutput.appendText(""); // trigger scroll top listener
            try {
              Thread.sleep(200);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
        t1.start();

        // update output textarea for the last time
        // there is a change missing some text until last time checking getKilled
        Thread t2 = new Thread(() -> {
          try {
            t1.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          sparkJobOutput.setText(job.getSparkOutputBuffer().toString());
          sparkJobOutput.appendText("");
        });
        t2.start();
      } else {
        System.out.println("Script was null. Show alert message!!");
      }
      // start spark job
    } else {
      // no selection show alert!
      System.out.println("No selection!");
    }
  }

  /**
   * Called when the user clicked on stop button
   */
  @FXML
  private void handleSparkSubmitJobStop() {
    System.out.println("stopping the job...");
  }
}
