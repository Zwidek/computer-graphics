package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class third extends Application {

    private Pane pane;
    private Circle[] controlPoints = new Circle[100];
    private Path bezierPath;
    private VBox vBox;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("third.fxml"));
        Parent root = brezier(fxmlLoader);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        stage.setTitle("Bezier");
        stage.setScene(scene);
        stage.show();
    }

    private Parent brezier(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        BorderPane borderPane = (BorderPane) root.lookup("#border_pane");
        vBox = (VBox) root.lookup("#vbox");
        pane = (Pane) root.lookup("#pane");

        drawBezier();
        setupTextFieldEventHandlers();

        return root;
    }

    private void drawBezier() {
        int numberOfControlPoints = 5;

        // Dodaj pola tekstowe do VBox dla każdego punktu kontrolnego
        for (int i = 0; i < numberOfControlPoints; i++) {
            Label labelX = new Label();
            TextField textFieldX = new TextField();
            Label labelY = new Label();
            TextField textFieldY = new TextField();

            HBox pointHBox = new HBox(labelX, textFieldX, labelY, textFieldY);
            vBox.getChildren().add(pointHBox);
        }

        // Rysuj początkowe punkty kontrolne i zainicjuj obsługę zdarzeń myszy
        controlPoints = new Circle[numberOfControlPoints];
        for (int i = 0; i < numberOfControlPoints; i++) {
            final int index = i;

            // Ustaw domyślne wartości dla punktów kontrolnych
            double defaultX = 10 + i * 30;
            double defaultY = 10;

            controlPoints[i] = createControlPoint(defaultX, defaultY);
            pane.getChildren().add(controlPoints[i]);

            // Ustaw wartości początkowe w polach tekstowych
            ((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1)).setText(String.valueOf(defaultX));
            ((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(3)).setText(String.valueOf(defaultY));

            controlPoints[i].setOnMouseDragged(event -> {
                // Aktualizuj pozycję punktu kontrolnego po przeciągnięciu myszką
                controlPoints[index].setCenterX(event.getX());
                controlPoints[index].setCenterY(event.getY());

                // Aktualizuj wartości w polach tekstowych
                ((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(1)).setText(String.valueOf(event.getX()));
                ((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(3)).setText(String.valueOf(event.getY()));

                // Przeliczaj i rysuj krzywą Béziera na nowo
                drawBezierCurve();
            });
        }

        drawBezierCurve();
    }

    private void setupTextFieldEventHandlers() {
        for (int i = 0; i < controlPoints.length; i++) {
            TextField textFieldX = (TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1);
            TextField textFieldY = (TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(3);

            // Add event handler for Enter key press on X coordinate TextField
            int finalI = i;
            textFieldX.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    updateControlPointAndRedraw(finalI);
                }
            });

            // Add event handler for Enter key press on Y coordinate TextField
            int finalI1 = i;
            textFieldY.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    updateControlPointAndRedraw(finalI1);
                }
            });
        }
    }
    private void updateControlPointAndRedraw(int index) {
        double newX = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(1)).getText());
        double newY = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(3)).getText());

        // Update the control point's position
        controlPoints[index].setCenterX(newX);
        controlPoints[index].setCenterY(newY);

        // Redraw the Bézier curve
        drawBezierCurve();
    }

    private Circle createControlPoint(double x, double y) {
        Circle circle = new Circle(x, y, 5);
        circle.setFill(Color.BLUEVIOLET);
        return circle;
    }

    private void drawBezierCurve() {
        int numberOfControlPoints = controlPoints.length;

        if (bezierPath != null) {
            pane.getChildren().remove(bezierPath);
        }

        bezierPath = new Path();
        MoveTo moveTo = new MoveTo(controlPoints[0].getCenterX(), controlPoints[0].getCenterY());
        bezierPath.getElements().add(moveTo);

        for (double t = 0; t <= 1; t += 0.01) {
            double x = deCasteljauX(t, controlPoints);
            double y = deCasteljauY(t, controlPoints);
            bezierPath.getElements().add(new javafx.scene.shape.LineTo(x, y));
        }

        pane.getChildren().add(bezierPath);
    }

    private double deCasteljauX(double t, Circle[] points) {
        double[] xValues = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            xValues[i] = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1)).getText());
        }
        for (int j = 1; j < points.length; j++) {
            for (int i = 0; i < points.length - j; i++) {
                xValues[i] = (1 - t) * xValues[i] + t * xValues[i + 1];
            }
        }
        return xValues[0];
    }

    private double deCasteljauY(double t, Circle[] points) {
        double[] yValues = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            yValues[i] = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(3)).getText());
        }
        for (int j = 1; j < points.length; j++) {
            for (int i = 0; i < points.length - j; i++) {
                yValues[i] = (1 - t) * yValues[i] + t * yValues[i + 1];
            }
        }
        return yValues[0];
    }


    public static void main(String[] args) {
        launch(args);
    }
}
