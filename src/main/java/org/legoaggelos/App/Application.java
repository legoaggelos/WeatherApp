package org.legoaggelos.App;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.legoaggelos.util.GetWeatherUtil;

import java.util.Collection;
import java.util.List;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        HBox mainElements = new HBox();
        Label creteLabel = new Label("Today's weather for crete");
        creteLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 22));
        Label athensLabel = new Label("Today's weather for Athens");
        athensLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 22));
        GetWeatherUtil getWeather = new GetWeatherUtil();

        VBox athensVBox = new VBox();
        athensVBox.getChildren().add(athensLabel);
        athensVBox.getChildren().addAll(mapStringListToLabel(getWeather.getForecast("athens", 1).stream().map(v->"         "+v).toList()));

        VBox creteVBox = new VBox();
        creteVBox.getChildren().add(creteLabel);
        creteVBox.getChildren().addAll(mapStringListToLabel(getWeather.getForecast("crete", 1).stream().map(v->"         "+v).toList()));

        Button refreshButton = new Button("Refresh data");
        refreshButton.setOnAction(e->{
            creteVBox.getChildren().removeAll(creteVBox.getChildren());
            athensVBox.getChildren().removeAll(athensVBox.getChildren());
            creteVBox.getChildren().add(creteLabel);
            creteVBox.getChildren().addAll(mapStringListToLabel(getWeather.getForecast("crete", 1).stream().map(v->"         "+v).toList()));
            athensVBox.getChildren().add(athensLabel);
            athensVBox.getChildren().addAll(mapStringListToLabel(getWeather.getForecast("athens", 1).stream().map(v->"         "+v).toList()));
        });
        refreshButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 22));
        refreshButton.setPrefSize(260,60);

        mainElements.getChildren().addAll(athensVBox,refreshButton,creteVBox);
        mainElements.setAlignment(Pos.TOP_CENTER);
        mainElements.setSpacing(30);

        Scene scene = new Scene(mainElements,1000,800);
        stage.setScene(scene);
        stage.setTitle("Weather App");
        stage.show();
    }
    public List<Label> mapStringListToLabel(Collection<String> list){
        List<Label> temp=list.stream().map(Label::new).toList();
        for (Label label : temp) {
            label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 22));
        }
        return temp;
    }
}
