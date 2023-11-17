package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.Light;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class third extends Application {

    private static Button rysuj_button;
    private static TextField rysuj_textfield;
    private static Label rysuj_label;
    private static BorderPane borderPane;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(third.class.getResource("third.fxml"));
        Parent root = brezier(fxmlLoader);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        stage.setTitle("Bezier");
        stage.setScene(scene);
        stage.show();
    }

    private Parent brezier(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        rysuj_button = (Button) root.lookup("#rysuj_button");
        rysuj_textfield = (TextField) root.lookup("#rysuj_textfield");
        rysuj_label = (Label) root.lookup("#rysuj_label");
        borderPane = (BorderPane) root.lookup("#border_pane");


        rysuj_button.setOnAction(actionEvent -> {
            if (rysuj_textfield.getCharacters().isEmpty() || !rysuj_textfield.getCharacters().toString().matches("\\d+")) {
                return;
            }
            drawBrezier(rysuj_textfield.getCharacters().toString());
        });

        return root;
    }

    private void drawBrezier(String brezierDegree) {
        rysuj_label.setVisible(false);
        rysuj_textfield.setVisible(false);
        rysuj_button.setVisible(false);

        int degree = Integer.parseInt(brezierDegree);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Punkty kontrolne i rozmiar");
        dialog.setHeaderText("Wprowadź punkty");

        TextField[][] textFields = new TextField[degree][2];
        for (int i = 0; i < degree - 1; i++) {
            TextField xTextField = new TextField();
            TextField yTextField = new TextField();

            textFields[i][0] = xTextField;
            textFields[i][1] = yTextField;

            grid.add(new Label("x" + (i + 1) + ":"), 0, i);
            grid.add(xTextField, 1, i);
            grid.add(new Label("y" + (i + 1) + ":"), 2, i);
            grid.add(yTextField, 3, i);
        }
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        Line xAxis = new Line(0, 200, 400, 200);
        Line yAxis = new Line(200, 0, 200, 400);

        Pane pane = new Pane();
        pane.getChildren().add(xAxis);
        pane.getChildren().add(yAxis);

        Path bezierPath = new Path();
        bezierPath.setStroke(Color.GOLD);
        bezierPath.setStrokeWidth(4);

        bezierPath.getElements().add(new MoveTo(0, 0));

        double[][] controlPoints = new double[degree][2];
        for (int i = 0; i < degree - 1; i++) {
            controlPoints[i][0] = Double.parseDouble(textFields[i][0].getText());
            controlPoints[i][1] = Double.parseDouble(textFields[i][1].getText());
        }

        MoveTo moveTo = new MoveTo();
        moveTo.setX(0.0f);
        moveTo.setY(0.0f);

        CubicCurveTo cubicTo = new CubicCurveTo();
        cubicTo.setControlX1(0);
        cubicTo.setControlY1(0);
        cubicTo.setX(0);
        cubicTo.setY(0);

        bezierPath.getElements().add(moveTo);
        bezierPath.getElements().add(cubicTo);

//        int tmp = 1;
//        for (int i = 0; i < degree - 1; i++) {
//            CubicCurveTo cubicCurveTo = new CubicCurveTo();
//            cubicCurveTo.setControlX1(controlPoints[i][0]);
//            cubicCurveTo.setControlY1(controlPoints[i][1]);
//            cubicCurveTo.setControlX2(controlPoints[tmp][0]);
//            cubicCurveTo.setControlY2(controlPoints[tmp][1]);
//            cubicCurveTo.setX(200);
//            cubicCurveTo.setY(0);
//            bezierPath.getElements().add(cubicCurveTo);
//            tmp++;
//        }

        pane.getChildren().add(bezierPath);

        borderPane.setCenter(pane);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
