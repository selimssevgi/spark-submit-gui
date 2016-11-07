# Spark Submit GUI
it is a simple GUI program, to run spark submit script.

![spark-submit gui
screenshot](https://github.com/SSSelim/spark-submit-gui/blob/master/spark-submit.png)

## Problem with starting from a maven project
If starting from maven project in Idea, there may be change to run into
problem with locating resource files. Instead start from JavaFx project.
Put .fxml views and their controllers in the same package,
otherwise FXMLLoader may not located them.

## Program UI
JavaFX is going to used to create GUI. 

Oracle does not support JavaFX scene builder anymore. Instead, we are going to
use JavaFX scene builder from [guonhq.com](http://gluonhq.com/labs/scene-builder/).

## Using JavaFx Scene Builder in Idea
In order to use scene builder bottom tab in Idea, we have to download jar of
Scene Builder.(at the time of writing, latest version was 8.2.0)

* Download Scene Builder executable from above link.
* Install or place somewhere on your local machine
* Open your sample.fxml file in Idea, click on "Scene Builder" tab.
* It asks you to configure the path of your Scene Builder installation.
* if deb is install, 'dkpg -L scenebuilder' outputs where it was installed.

!! It may be better to right click on fxml files and open them in Scene Builder
it has a better and faster UI than the one in Idea.

## java.lang.IllegalStateException: Location is not set.
This runtime error occurs when fxml files cannot be located.

JavaFx Project: put them in 'view' package and refer them as follows: 
FXMLLoader loader = new FXMLLoader();
loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));

Maven Project: put them in resources directory and refer them as follows:
FXMLLoader loader = new FXMLLoader();
loader.setLocation(App.class.getResource("/RootLayout.fxml"));

## Textare autoscroll
listener gets trigger when called appendText method on textare object,
call appendText("") method, after set method.
