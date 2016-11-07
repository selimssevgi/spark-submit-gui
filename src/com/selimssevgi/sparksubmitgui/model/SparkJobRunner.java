package com.selimssevgi.sparksubmitgui.model;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ssselim on 9/28/16.
 */
public class SparkJobRunner extends Thread{
  private final static Logger log = Logger.getLogger(SparkJobRunner.class.getName());

  private Process process;
  private Boolean killed;
  private final SparkJob job;

  public SparkJobRunner(SparkJob job) {
    this.job = job;
    killed = false;
  }

  @Override
  public void run() {
    String[] cmdArray = job.getSparkSubmitScript().split(" ");
    log.info("cmdArray: " + Arrays.toString(cmdArray));
    // Run java
    try {
      runChild(cmdArray, null);
    } catch (IOException e) {
      log.info("An error occured while calling runChild method.");
      e.printStackTrace();
    }
  }

  /**
   * Run the child process
   */
  private void runChild(String[] args, File dir) throws IOException {
    this.process = Runtime.getRuntime().exec(args, null, dir);
    try {
      new Thread() {
        public void run() {
          logStream(process.getErrorStream());    // copy log output
        }
      }.start();

      logStream(process.getInputStream());        // normally empty

      if (this.process.waitFor() != 0) {
        throw new IOException("Task process exit with nonzero status.");
      }

    } catch (InterruptedException e) {
      throw new IOException(e.toString());
    } finally {
      kill();
    }
  }

  /**
   * Kill the child process
   */
  public void kill() {
    if (process != null) {
      process.destroy();
    }
    killed = true;
  }

  /**
   */
  private void logStream(InputStream output) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(output));
      String line;
      StringBuffer sb = job.getSparkOutputBuffer();
      while ((line = in.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
        log.info(line);
      }
    } catch (IOException e) {
      log.log(Level.WARNING, " Error reading child output", e);
    } finally {
      try {
        output.close();
      } catch (IOException e) {
        log.log(Level.WARNING, " Error closing child output", e);
      }
    }
  }

  public Boolean getKilled() {
    return killed;
  }
}
