package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class fourth extends Application {
    private static Label dlugoscLabel;
    private static TextField dlugoscTextField;
    private static Label szerokoscLabel;
    private static TextField szerokoscTextField;
    private static Label promienLabel;
    private static TextField srednicaTextField;
    private static ToggleButton liniaButton;
    private static ToggleButton prostokatButton;
    private static ToggleButton okragButton;
    private static Button zmienRozmiarButton;
    private static TextField yTextField;
    private static TextField xTextField;
    private static Color color = Color.RED;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(fourth.class.getResource("fourth.fxml"));
        Parent root = drawShape(fxmlLoader);

        Scene scene = new Scene(root, 750, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private static Parent drawShape(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();

        Button rysujButton = (Button) root.lookup("#rysuj_button");
        zmienRozmiarButton = (Button) root.lookup("#rozmiar_button");

        dlugoscLabel = (Label) root.lookup("#dlugosc_label");
        dlugoscTextField = (TextField) root.lookup("#dlugosc_text");

        szerokoscLabel = (Label) root.lookup("#szerokosc_label");
        szerokoscTextField = (TextField) root.lookup("#szerokosc_text");

        promienLabel = (Label) root.lookup("#promien_label");
        srednicaTextField = (TextField) root.lookup("#promien_text");

        liniaButton = (ToggleButton) root.lookup("#linia_button");
        prostokatButton = (ToggleButton) root.lookup("#prostokat_button");
        okragButton = (ToggleButton) root.lookup("#okrag_button");

        xTextField = (TextField) root.lookup("#x_textfield");
        yTextField = (TextField) root.lookup("#y_textfield");
        Button xButton = (Button) root.lookup("#x_button");
        Button yButton = (Button) root.lookup("#y_button");

        liniaButton.setOnAction(event -> {
            dlugoscLabel.setVisible(true);
            dlugoscTextField.setVisible(true);
            dlugoscTextField.setText(String.valueOf(0));

            szerokoscLabel.setVisible(false);
            szerokoscTextField.setVisible(false);

            promienLabel.setVisible(false);
            srednicaTextField.setVisible(false);

            prostokatButton.setSelected(false);
            okragButton.setSelected(false);

            rysujButton.setVisible(true);
        });

        prostokatButton.setOnAction(event -> {
            dlugoscLabel.setVisible(true);
            dlugoscTextField.setVisible(true);
            dlugoscTextField.setText(String.valueOf(0));

            szerokoscLabel.setVisible(true);
            szerokoscTextField.setVisible(true);
            szerokoscTextField.setText(String.valueOf(0));

            promienLabel.setVisible(false);
            srednicaTextField.setVisible(false);

            liniaButton.setSelected(false);
            okragButton.setSelected(false);

            rysujButton.setVisible(true);
        });

        okragButton.setOnAction(event -> {
            dlugoscLabel.setVisible(false);
            dlugoscTextField.setVisible(false);

            szerokoscLabel.setVisible(false);
            szerokoscTextField.setVisible(false);

            promienLabel.setVisible(true);
            srednicaTextField.setVisible(true);
            srednicaTextField.setText(String.valueOf(0));

            liniaButton.setSelected(false);
            prostokatButton.setSelected(false);

            rysujButton.setVisible(true);
        });

        Pane pane = (Pane) root.lookup("#pane");
        AtomicReference<Double> dlugosc = new AtomicReference<>((double) 0);
        AtomicReference<Double> szerokosc = new AtomicReference<>((double) 0);
        AtomicReference<Double> srednica = new AtomicReference<>((double) 0);

        xButton.setOnAction(event -> xButtonAction());
        yButton.setOnAction(event -> yButtonAction());

        rysujButton.setOnAction(actionEvent -> {
            if (prostokatButton.isSelected()) {
                dlugosc.set(Double.valueOf(dlugoscTextField.getText()));
                szerokosc.set(Double.valueOf(szerokoscTextField.getText()));
                Rectangle rectangle = new Rectangle(100, -50, dlugosc.get(), szerokosc.get());
                rectangle.setStroke(color);
                pane.getChildren().add(rectangle);
                setMouseEvents(rectangle);
            } else if (liniaButton.isSelected()) {
                dlugosc.set(Double.valueOf(dlugoscTextField.getText()));
                Line line = new Line(0, 0, dlugosc.get(), 0);
                line.setStroke(color);
                line.setStrokeWidth(3.0);
                pane.getChildren().add(line);
                setMouseEvents(line);
            } else if (okragButton.isSelected()) {
                srednica.set(Double.valueOf(srednicaTextField.getText()));
                Circle circle = new Circle(150, 0, srednica.get());
                circle.setStroke(color);
                pane.getChildren().add(circle);
                setMouseEvents(circle);
            }
            prostokatButton.setSelected(false);
            okragButton.setSelected(false);
            liniaButton.setSelected(false);
        });

        pane.setOnMousePressed(event -> {
            if (prostokatButton.isSelected()) {
                Rectangle rectangle = new Rectangle(event.getX(), event.getY(), 5, 5);
                rectangle.setStroke(color);
                pane.getChildren().add(rectangle);
                setMouseEvents(rectangle);
            } else if (liniaButton.isSelected()) {
                Line line = new Line(event.getX(), event.getY(), event.getX() + 5, event.getY());
                line.setStroke(color);
                line.setStrokeWidth(3.0);
                pane.getChildren().add(line);
                setMouseEvents(line);
            } else if (okragButton.isSelected()) {
                Circle circle = new Circle(event.getX(), event.getY(), 5);
                circle.setStroke(color);
                pane.getChildren().add(circle);
                setMouseEvents(circle);
            }
            prostokatButton.setSelected(false);
            okragButton.setSelected(false);
            liniaButton.setSelected(false);
        });

        return root;
    }


    private static void xButtonAction() {
    }

    private static void yButtonAction() {
    }

    private static void setMouseEvents(Shape shape) {
        AtomicReference<Double> initialX = new AtomicReference<>((double) 0);
        AtomicReference<Double> initialY = new AtomicReference<>((double) 0);
        AtomicReference<Boolean> resizing = new AtomicReference<>(false);

        shape.setOnMousePressed(event -> {
            zmienRozmiarButton.setVisible(true);
            if (event.isShiftDown()) {
                resizing.set(true);
                initialX.set(event.getSceneX());
                initialY.set(event.getSceneY());
            } else {
                initialX.set(event.getSceneX() - shape.getLayoutX());
                initialY.set(event.getSceneY() - shape.getLayoutY());
            }
            switch (shape) {
                case Rectangle rectangle -> {
                    changeVisibility(rectangle);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    dlugoscTextField.setText(decimalFormat.format(rectangle.getWidth()));
                    szerokoscTextField.setText(decimalFormat.format(rectangle.getHeight()));
                    changeSize(rectangle);
                }
                case Circle circle -> {
                    changeVisibility(circle);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    srednicaTextField.setText(decimalFormat.format(circle.getRadius()));
                    changeSize(circle);
                }
                case Line line -> {
                    changeVisibility(line);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    dlugoscTextField.setText(decimalFormat.format(line.getEndX()));
                    changeSize(line);
                }
                default -> throw new IllegalStateException("Unexpected value: " + shape);
            }
        });

        shape.setOnMouseDragged(event -> {
            zmienRozmiarButton.setVisible(true);
            if (resizing.get()) {
                double currentX = event.getSceneX();
                double currentY = event.getSceneY();

                double offsetX = currentX - initialX.get();
                double offsetY = currentY - initialY.get();

                switch (shape) {
                    case Rectangle rectangle -> {
                        changeVisibility(rectangle);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        dlugoscTextField.setText(decimalFormat.format(rectangle.getHeight()));
                        szerokoscTextField.setText(decimalFormat.format(rectangle.getWidth()));
                        double newWidth = Math.max(rectangle.getWidth() + offsetX, 10);
                        double newHeight = Math.max(rectangle.getHeight() + offsetY, 10);
                        rectangle.setWidth(newWidth);
                        rectangle.setHeight(newHeight);
                    }
                    case Circle circle -> {
                        changeVisibility(circle);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        srednicaTextField.setText(decimalFormat.format(circle.getRadius()));
                        double newRadius = Math.max(circle.getRadius() + (offsetX + offsetY) / 2.0, 5);
                        circle.setRadius(newRadius);
                    }
                    case Line line -> {
                        changeVisibility(line);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        dlugoscTextField.setText(decimalFormat.format(line.getEndX()));
                        line.setEndX(line.getEndX() + offsetX);
                        line.setEndY(line.getEndY() + offsetY);
                    }
                    default -> {
                    }
                }
                initialX.set(currentX);
                initialY.set(currentY);
            } else {
                double newX = event.getSceneX() - initialX.get();
                double newY = event.getSceneY() - initialY.get();
                shape.setLayoutX(newX);
                shape.setLayoutY(newY);
            }
        });
        shape.setOnMouseReleased(event -> resizing.set(false));
    }

    private static void changeSize(Shape shape) {
        if (shape instanceof Rectangle) {
            zmienRozmiarButton.setOnAction(actionEvent -> {
                ((Rectangle) shape).setHeight(Double.parseDouble(szerokoscTextField.getText().split(",")[0]));
                ((Rectangle) shape).setWidth(Double.parseDouble(dlugoscTextField.getText().split(",")[0]));
            });
        } else if (shape instanceof Circle) {
            zmienRozmiarButton.setOnAction(actionEvent -> ((Circle) shape).setRadius(Double.parseDouble(srednicaTextField.getText().split(",")[0])));
        } else if (shape instanceof Line) {
            zmienRozmiarButton.setOnAction(actionEvent -> ((Line) shape).setEndX(Double.parseDouble(dlugoscTextField.getText().split(",")[0])));
        }
    }

    public static void changeVisibility(Shape shape) {
        if (shape instanceof Rectangle) {
            dlugoscLabel.setVisible(true);
            dlugoscTextField.setVisible(true);
            szerokoscLabel.setVisible(true);
            szerokoscTextField.setVisible(true);
            promienLabel.setVisible(false);
            srednicaTextField.setVisible(false);
            prostokatButton.setSelected(false);
            liniaButton.setSelected(false);
            okragButton.setSelected(false);
        } else if (shape instanceof Line) {
            dlugoscLabel.setVisible(true);
            dlugoscTextField.setVisible(true);
            szerokoscLabel.setVisible(false);
            szerokoscTextField.setVisible(false);
            promienLabel.setVisible(false);
            srednicaTextField.setVisible(false);
            prostokatButton.setSelected(false);
            liniaButton.setSelected(false);
            okragButton.setSelected(false);
        } else if (shape instanceof Circle) {
            dlugoscLabel.setVisible(false);
            dlugoscTextField.setVisible(false);
            szerokoscLabel.setVisible(false);
            szerokoscTextField.setVisible(false);
            promienLabel.setVisible(true);
            srednicaTextField.setVisible(true);
            prostokatButton.setSelected(false);
            liniaButton.setSelected(false);
            okragButton.setSelected(false);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
