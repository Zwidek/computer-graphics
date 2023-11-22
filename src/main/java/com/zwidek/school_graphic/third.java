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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class third extends Application {

    private static Button rysuj_button;
    private static BorderPane borderPane;
    private static Pane pane;
    private static VBox vBox;
    private Circle[] controlPoints = new Circle[100];
    private Path bezierPath;

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
        borderPane = (BorderPane) root.lookup("#border_pane");
        pane = (Pane) root.lookup("#pane");

        drawBezier();

        return root;
    }

    private void drawBezier() {
        double x0 = 10, y0 = 10;
        double x1 = 30, y1 = 30;
        double x2 = 50, y2 = 50;
        double x3 = 100, y3 = 100;
        double x4 = 150, y4 = 50;
        int numberOfControlPoints = 5;

        controlPoints = new Circle[numberOfControlPoints];
        for (int i = 0; i < numberOfControlPoints; i++) {
            final int index = i;
            controlPoints[i] = createControlPoint(x0 + i * 30, y0);
            pane.getChildren().add(controlPoints[i]);

            controlPoints[i].setOnMouseDragged(event -> {
                controlPoints[index].setCenterX(event.getX());
                controlPoints[index].setCenterY(event.getY());
                drawBezierCurve();
            });
        }
        drawBezierCurve();
    }

    private Circle createControlPoint(double x, double y) {
        Circle circle = new Circle(x, y, 5);
        circle.setFill(Color.BLUEVIOLET);
        return circle;
    }

    private void drawBezierCurve() {
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
            xValues[i] = points[i].getCenterX();
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
            yValues[i] = points[i].getCenterY();
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
