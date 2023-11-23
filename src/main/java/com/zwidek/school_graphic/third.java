package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class third extends Application {

    private Pane pane;
    private Path bezierPath;
    private VBox vBox;
    private List<Circle> controlPoints = new ArrayList<>();
    private Button button;

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
        button = (Button) root.lookup("#button");

        drawBezier();
        setupTextFieldEventHandlers();

        return root;
    }

    private void drawBezier() {
        int numberOfControlPoints = 1;

        // Add text fields to VBox for each control point
        for (int i = 0; i < numberOfControlPoints; i++) {
            TextField textFieldX = new TextField();
            TextField textFieldY = new TextField();

            HBox pointHBox = new HBox(textFieldX, textFieldY);
            vBox.getChildren().add(pointHBox);
        }

        button.setOnAction(event -> addNewPoint());

        // Draw initial control points and initialize mouse event handling
        controlPoints = new ArrayList<>();
        for (int i = 0; i < numberOfControlPoints; i++) {
            final int index = i;

            // Set default values for control points
            double defaultX = 50 + i * 30;
            double defaultY = 50;

            Circle controlPoint = createControlPoint(defaultX, defaultY);
            pane.getChildren().add(controlPoint);

            // Set initial values in text fields
            ((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(0)).setText(String.valueOf(defaultX));
            ((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1)).setText(String.valueOf(defaultY));

            controlPoint.setOnMouseDragged(event -> {
                // Update control point position after dragging
                controlPoints.get(index).setCenterX(event.getX());
                controlPoints.get(index).setCenterY(event.getY());

                // Update values in text fields
                ((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(0)).setText(String.valueOf(event.getX()));
                ((TextField) ((HBox) vBox.getChildren().get(index)).getChildren().get(1)).setText(String.valueOf(event.getY()));

                // Recalculate and redraw Bézier curve
                drawBezierCurve();
            });

            controlPoints.add(controlPoint);
        }

        drawBezierCurve();
    }

    private void setupTextFieldEventHandlers() {
        for (int i = 0; i < controlPoints.size(); i++) {
            TextField textFieldX = (TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(0);
            TextField textFieldY = (TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1);

            // Add event handler for Enter key press on X coordinate TextField
            int finalI = i;
            textFieldX.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    updateControlPointAndRedraw(finalI, textFieldX, textFieldY);
                }
            });

            // Add event handler for Enter key press on Y coordinate TextField
            int finalI1 = i;
            textFieldY.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    updateControlPointAndRedraw(finalI1, textFieldX, textFieldY);
                }
            });
        }
    }

    private void updateControlPointAndRedraw(int index, TextField textFieldX, TextField textFieldY) {
        double newX = Double.parseDouble(textFieldX.getText());
        double newY = Double.parseDouble(textFieldY.getText());

        // Update control point's position
        controlPoints.get(index).setCenterX(newX);
        controlPoints.get(index).setCenterY(newY);

        // Redraw Bézier curve
        drawBezierCurve();
    }

    private Circle createControlPoint(double x, double y) {
        Circle circle = new Circle(x, y, 5);
        circle.setFill(Color.BLUEVIOLET);
        return circle;
    }

    private void drawBezierCurve() {
        int numberOfControlPoints = controlPoints.size();

        if (bezierPath != null) {
            pane.getChildren().remove(bezierPath);
        }

        bezierPath = new Path();
        MoveTo moveTo = new MoveTo(controlPoints.get(0).getCenterX(), controlPoints.get(0).getCenterY());
        bezierPath.getElements().add(moveTo);

        for (double t = 0; t <= 1; t += 0.01) {
            double x = deCasteljauX(t, controlPoints);
            double y = deCasteljauY(t, controlPoints);
            bezierPath.getElements().add(new javafx.scene.shape.LineTo(x, y));
        }

        pane.getChildren().add(bezierPath);
    }

    private double deCasteljauX(double t, List<Circle> points) {
        double[] xValues = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xValues[i] = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(0)).getText());
        }
        for (int j = 1; j < points.size(); j++) {
            for (int i = 0; i < points.size() - j; i++) {
                xValues[i] = (1 - t) * xValues[i] + t * xValues[i + 1];
            }
        }
        return xValues[0];
    }

    private double deCasteljauY(double t, List<Circle> points) {
        double[] yValues = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            yValues[i] = Double.parseDouble(((TextField) ((HBox) vBox.getChildren().get(i)).getChildren().get(1)).getText());
        }
        for (int j = 1; j < points.size(); j++) {
            for (int i = 0; i < points.size() - j; i++) {
                yValues[i] = (1 - t) * yValues[i] + t * yValues[i + 1];
            }
        }
        return yValues[0];
    }

    private void addNewPoint() {
        TextField textFieldX = new TextField();
        TextField textFieldY = new TextField();

        HBox pointHBox = new HBox(textFieldX, textFieldY);
        vBox.getChildren().add(pointHBox);

        // Set event handlers for new text fields
        int newIndex = controlPoints.size();
        textFieldX.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateControlPointAndRedraw(newIndex, textFieldX, textFieldY);
            }
        });

        textFieldY.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateControlPointAndRedraw(newIndex, textFieldX, textFieldY);
            }
        });

        // Create a new control point for the added TextField and add it to the list
        Circle newControlPoint = createControlPoint(30, 30); // Set initial values or update as needed
        controlPoints.add(newControlPoint);
        pane.getChildren().add(newControlPoint);

        // Set up mouse event handler for the new control point
        newControlPoint.setOnMouseDragged(event -> {
            // Update control point position after dragging
            controlPoints.get(newIndex).setCenterX(event.getX());
            controlPoints.get(newIndex).setCenterY(event.getY());

            // Update values in text fields
            textFieldX.setText(String.valueOf(event.getX()));
            textFieldY.setText(String.valueOf(event.getY()));

            // Recalculate and redraw Bézier curve
            drawBezierCurve();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
