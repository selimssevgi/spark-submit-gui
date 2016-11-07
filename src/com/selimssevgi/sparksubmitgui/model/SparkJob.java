package com.selimssevgi.sparksubmitgui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a SparkJob
 * Created by ssselim on 9/28/16.
 */
public class SparkJob {

  /**
   * With JavaFX it's common to use Properties for all fields of a model class.
   * A Property allows us, for example, to automatically be notified
   * when the sparkName or any other variable is changed.
   * This helps us keep the view in sync with the data.
   */

  private final StringProperty sparkName;

  public void setSparkSubmitScript(String sparkSubmitScript) {
    this.sparkSubmitScript.set(sparkSubmitScript);
  }

  private final StringProperty sparkSubmitScript;
  private StringBuffer sparkOutputBuffer;

  /**
   *  default constructor
   */
  public SparkJob() {
    this(null, null);
  }

  /**
   * Constructor with some initial data
   * @param sparkName
   * @param script
   */
  public SparkJob(String sparkName, String script) {
    this.sparkName = new SimpleStringProperty(sparkName);
    this.sparkSubmitScript = new SimpleStringProperty(script);
    sparkOutputBuffer = new StringBuffer(127);
  }


  public StringBuffer getSparkOutputBuffer() {
    return sparkOutputBuffer;
  }

  public String getSparkName() {
    return sparkName.get();
  }

  public StringProperty sparkNameProperty() {
    return sparkName;
  }

  public String getSparkSubmitScript() {
    return sparkSubmitScript.get();
  }

  public StringProperty sparkSubmitScriptProperty() {
    return sparkSubmitScript;
  }
}
