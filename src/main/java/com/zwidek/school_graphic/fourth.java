package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private static VBox vBox2;
    private static TextField wektorXTextfield;
    private static TextField wektorYTextfield;
    private static TextField obrotTextfield;
    private static TextField szerokoscTextfield;
    private static TextField wysokoscTextfield;
    private static Button wektorXButton;
    private static Button wektorYButton;
    private static Button obrotButton;
    private static Button szerokoscButton;
    private static Button wysokoscButton;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(fourth.class.getResource("fourth.fxml"));
        Parent root = draw(fxmlLoader);

        Scene scene = new Scene(root, 750, 600);
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
        vBox2 = (VBox) root.lookup("#vbox2");
        wektorXTextfield = (TextField) root.lookup("#wektorX_textfield");
        wektorYTextfield = (TextField) root.lookup("#wektorY_textfield");
        wektorXButton = (Button) root.lookup("#wektorX_button");
        wektorYButton = (Button) root.lookup("#wektorY_button");
        obrotTextfield = (TextField) root.lookup("#obrot_textfield");
        wysokoscTextfield = (TextField) root.lookup("#wysokosc_textfield");
        szerokoscTextfield = (TextField) root.lookup("#szerokosc_textfield");
        obrotButton = (Button) root.lookup("#obrot_button");
        wysokoscButton = (Button) root.lookup("#wysokosc_button");
        szerokoscButton = (Button) root.lookup("#szerokosc_button");

        drawShapes(pane);
        setupEventHandlers();
        return root;
    }

    private void setupEventHandlers() {
        wektorXButton.setOnAction(event -> {
            try {
                double deltaX = Double.parseDouble(wektorXTextfield.getText());
                for (Node node : pane.getChildren()) {
                    if (node instanceof Polygon) {
                        Polygon polygon = (Polygon) node;
                        polygon.setLayoutX(polygon.getLayoutX() + deltaX);
                    } else if (node instanceof Circle) {
                        Circle point = (Circle) node;
                        point.setCenterX(point.getCenterX() + deltaX);
                    }
                }
                updateVBoxInfo(getAllPoints());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
            updateVBoxInfo(getAllPoints());
        });

        wektorYButton.setOnAction(event -> {
            try {
                double deltaY = Double.parseDouble(wektorYTextfield.getText());
                for (Node node : pane.getChildren()) {
                    if (node instanceof Polygon) {
                        Polygon polygon = (Polygon) node;
                        polygon.setLayoutY(polygon.getLayoutY() + deltaY);
                    } else if (node instanceof Circle) {
                        Circle point = (Circle) node;
                        point.setCenterY(point.getCenterY() + deltaY);
                    }
                }
                updateVBoxInfo(getAllPoints());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
            updateVBoxInfo(getAllPoints());
        });

        obrotButton.setOnAction(event -> {
            try {
                double rotationAngle = Double.parseDouble(obrotTextfield.getText());
                for (Node node : pane.getChildren()) {
                    if (node instanceof Polygon) {
                        Polygon polygon = (Polygon) node;
                        polygon.setRotate(polygon.getRotate() + rotationAngle);
                    }
                }
                updateVBoxInfo(getAllPoints());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
            updateVBoxInfo(getAllPoints());
        });

        szerokoscButton.setOnAction(event -> {
            try {
                double szerokoscDelta = Double.parseDouble(szerokoscTextfield.getText());
                for (Node node : pane.getChildren()) {
                    if (node instanceof Polygon) {
                        Polygon polygon = (Polygon) node;
                        polygon.setScaleX(polygon.getScaleX() + szerokoscDelta);
                    }
                }
                updateVBoxInfo(getAllPoints());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
            updateVBoxInfo(getAllPoints());
        });

        wysokoscButton.setOnAction(event -> {
            try {
                double wysokoscDelta = Double.parseDouble(wysokoscTextfield.getText());
                for (Node node : pane.getChildren()) {
                    if (node instanceof Polygon) {
                        Polygon polygon = (Polygon) node;
                        polygon.setScaleY(polygon.getScaleY() + wysokoscDelta);
                    }
                }
                updateVBoxInfo(getAllPoints());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }

            updateVBoxInfo(getAllPoints());
        });
    }

    private List<Circle> getAllPoints() {
        List<Circle> allPoints = new ArrayList<>();

        for (Node node : pane.getChildren()) {
            if (node instanceof Circle) {
                allPoints.add((Circle) node);
            }
        }
        return allPoints;
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
                List<Circle> clonedPoints = new ArrayList<>();
                for (Circle point : points) {
                    polygon.getPoints().addAll(point.getCenterX(), point.getCenterY());
                    Circle clonedPoint = new Circle(point.getCenterX(), point.getCenterY(), 3);
                    clonedPoints.add(clonedPoint);
                }

                pane.getChildren().add(polygon);
                polygons.add(polygon);
                setPolygonDraggable(polygon, clonedPoints);

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

                updateVBoxInfo(points);
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

                for (Circle point : points) {
                    point.setCenterX(point.getCenterX() + deltaX);
                    point.setCenterY(point.getCenterY() + deltaY);
                }

                updateVBoxInfo(points);
            }
        });
        polygon.setOnMouseReleased(event -> {
            polygon.getParent().setMouseTransparent(false);
            ctrlPressed.set(false);
        });
    }

    private void updateVBoxInfo(List<Circle> points) {
        vBox.getChildren().clear();

        for (Circle point : points) {
            double x = point.getCenterX();
            double y = point.getCenterY();

            HBox hbox = new HBox();

            TextField xTextField = new TextField(String.valueOf(x));
            xTextField.setPrefWidth(50);
            xTextField.setOnAction(e -> {
                try {
                    double newX = Double.parseDouble(xTextField.getText());
                    point.setCenterX(newX);
                    updateVBoxInfo(points);
                } catch (NumberFormatException ex) {
                    xTextField.setText(String.valueOf(point.getCenterX()));
                }
            });

            TextField yTextField = new TextField(String.valueOf(y));
            yTextField.setPrefWidth(50);
            yTextField.setOnAction(e -> {
                try {
                    double newY = Double.parseDouble(yTextField.getText());
                    point.setCenterY(newY);
                    updateVBoxInfo(points);
                } catch (NumberFormatException ex) {
                    yTextField.setText(String.valueOf(point.getCenterY()));
                }
            });
            hbox.getChildren().addAll(xTextField, yTextField);
            vBox.getChildren().add(hbox);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
