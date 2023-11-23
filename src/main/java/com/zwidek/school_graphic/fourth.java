package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class fourth extends Application {
    private static BorderPane borderPane;
    private static Pane pane;
    private static VBox vBox;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(fourth.class.getResource("fourth.fxml"));
        Parent root = draw(fxmlLoader);

        Scene scene = new Scene(root, 750, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private Parent draw(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        borderPane = (BorderPane) root.lookup("#border_pane");
        pane = (Pane) root.lookup("#pane");
        vBox = (VBox) root.lookup("#vbox");

        drawShapes(pane);
        return root;
    }

    private void drawShapes(Pane pane) {
        List<Circle> points = new ArrayList<>();
        List<Line> lines = new ArrayList<>();
        List<Polygon> polygons = new ArrayList<>();

        pane.setOnMouseClicked((MouseEvent event) -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            if (event.getButton() == MouseButton.SECONDARY && points.size() >= 3) {
                Circle firstPoint = points.get(0);
                Circle lastPoint = points.get(points.size() - 1);

                Line closingLine = new Line(lastPoint.getCenterX(), lastPoint.getCenterY(), firstPoint.getCenterX(),
                        firstPoint.getCenterY());
                lines.add(closingLine);

                pane.getChildren().addAll(closingLine);

                Polygon polygon = new Polygon();
                List<Circle> clonedPoints = new ArrayList<>(); // Lista punktów do wielokąta
                for (Circle point : points) {
                    polygon.getPoints().addAll(point.getCenterX(), point.getCenterY());
                    Circle clonedPoint = new Circle(point.getCenterX(), point.getCenterY(), 3);
                    clonedPoints.add(clonedPoint);
                }

                pane.getChildren().add(polygon);
                polygons.add(polygon);
                setPolygonDraggable(polygon, clonedPoints); // Ustawianie obsługi przesunięcia dla nowego polygonu i jego punktów

                // Usunięcie punktów i linii
                pane.getChildren().removeAll(points);
                pane.getChildren().removeAll(lines);
                points.clear();
                lines.clear();
            } else {
                Circle point = new Circle(mouseX, mouseY, 3);
                point.setFill(Color.RED);

                pane.getChildren().add(point);
                points.add(point);

                if (points.size() > 1) {
                    Circle previousPoint = points.get(points.size() - 2);
                    Line line = new Line(previousPoint.getCenterX(), previousPoint.getCenterY(), mouseX, mouseY);
                    pane.getChildren().add(line);
                    lines.add(line);
                }
            }
        });
    }
    private void setPolygonDraggable(Polygon polygon, List<Circle> points) {
        AtomicReference<Double> startAngle = new AtomicReference<>(0.0);
        AtomicBoolean ctrlPressed = new AtomicBoolean(false);

        polygon.setOnMousePressed(event -> {
            if (event.isShiftDown()) {
                polygon.getParent().setMouseTransparent(true);
                startAngle.set(Math.atan2(event.getSceneY() - polygon.getBoundsInParent().getMaxY(),
                        event.getSceneX() - polygon.getBoundsInParent().getMaxX()));
            } else if (event.isControlDown()) {
                polygon.getParent().setMouseTransparent(true);
                ctrlPressed.set(true);
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                polygon.setUserData(new double[]{mouseX, mouseY});
            } else {
                polygon.getParent().setMouseTransparent(true);
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                polygon.setUserData(new double[]{mouseX, mouseY});
            }
        });

        polygon.setOnMouseDragged(event -> {
            if (event.isShiftDown()) {
                double angle = Math.atan2(event.getSceneY() - polygon.getBoundsInParent().getMaxY(),
                        event.getSceneX() - polygon.getBoundsInParent().getMaxX()) - startAngle.get();
                polygon.setRotate(Math.toDegrees(angle));
            } else if (ctrlPressed.get()) {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                double[] lastMousePos = (double[]) polygon.getUserData();

                double deltaX = mouseX - lastMousePos[0];
                double deltaY = mouseY - lastMousePos[1];

                double pivotX = polygon.getBoundsInLocal().getWidth() / 2 + polygon.getBoundsInParent().getMinX();
                double pivotY = polygon.getBoundsInLocal().getHeight() / 2 + polygon.getBoundsInParent().getMinY();

                double factorX = deltaX / polygon.getBoundsInLocal().getWidth();
                double factorY = deltaY / polygon.getBoundsInLocal().getHeight();

                polygon.setScaleX(polygon.getScaleX() * (1 + factorX));
                polygon.setScaleY(polygon.getScaleY() * (1 + factorY));

                polygon.setUserData(new double[]{mouseX, mouseY});
            } else {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                double[] lastMousePos = (double[]) polygon.getUserData();

                double deltaX = mouseX - lastMousePos[0];
                double deltaY = mouseY - lastMousePos[1];

                polygon.setLayoutX(polygon.getLayoutX() + deltaX);
                polygon.setLayoutY(polygon.getLayoutY() + deltaY);

                polygon.setUserData(new double[]{mouseX, mouseY});

                // Aktualizacja pozycji punktów
                for (Circle point : points) {
                    point.setCenterX(point.getCenterX() + deltaX);
                    point.setCenterY(point.getCenterY() + deltaY);
                }
            }
        });

        polygon.setOnMouseReleased(event -> {
            polygon.getParent().setMouseTransparent(false);
            ctrlPressed.set(false);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
