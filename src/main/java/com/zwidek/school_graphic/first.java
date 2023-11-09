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

public class first extends Application {
    private static ColorPicker colorPicker;
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
    private static TextField rgbText;
    private static TextField cmykText;
    private static Color color = Color.RED;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(first.class.getResource("first.fxml"));
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

        cmykText = (TextField) root.lookup("#cmyk_text");
        rgbText = (TextField) root.lookup("#rgb_text");
        colorPicker = (ColorPicker) root.lookup("#color_picker");
        Button paletaButton = (Button) root.lookup("#paleta_button");
        Button cmykButton = (Button) root.lookup("#cmyk_button");
        Button rgbButton = (Button) root.lookup("#rgb_button");

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

        paletaButton.setOnAction(event -> paletaButtonAction());
        cmykButton.setOnAction(event -> cmykButtonAction());
        rgbButton.setOnAction(event -> rgbButtonAction());

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

    private static void paletaButtonAction() {
        Color selectedColor = colorPicker.getValue();
        int r = (int) (selectedColor.getRed() * 255);
        int g = (int) (selectedColor.getGreen() * 255);
        int b = (int) (selectedColor.getBlue() * 255);
        rgbText.setText(r + ", " + g + ", " + b);
        cmykText.setText(convertRGBToCMYK(r, g, b));
        color = Color.rgb(r,g,b);
    }

    private static void cmykButtonAction() {
        String cmykInput = cmykText.getText();
        String[] cmykValues = cmykInput.split(",");
        if (cmykValues.length == 4) {
            int c = Integer.parseInt(cmykValues[0].trim());
            int m = Integer.parseInt(cmykValues[1].trim());
            int y = Integer.parseInt(cmykValues[2].trim());
            int k = Integer.parseInt(cmykValues[3].trim());
            rgbText.setText(convertCMYKToRGB(c, m, y, k));
            Color convertedColor = convertCMYKToColor(c, m, y, k);
            colorPicker.setValue(convertedColor);
            color = Color.rgb(c, m, y, k);
        }
    }

    private static void rgbButtonAction() {
        String rgbInput = rgbText.getText();
        String[] rgbValues = rgbInput.split(",");
        if (rgbValues.length == 3) {
            int r = Integer.parseInt(rgbValues[0].trim());
            int g = Integer.parseInt(rgbValues[1].trim());
            int b = Integer.parseInt(rgbValues[2].trim());
            cmykText.setText(convertRGBToCMYK(r, g, b));
            colorPicker.setValue(Color.rgb(r, g, b));
            color = Color.rgb(r, g, b);
        }
    }

    private static String convertRGBToCMYK(int r, int g, int b) {
        double c, m, y, k;
        double normalizedR = r / 255.0;
        double normalizedG = g / 255.0;
        double normalizedB = b / 255.0;

        k = 1 - Math.max(normalizedR, Math.max(normalizedG, normalizedB));
        if (k == 1.0) {
            c = m = y = 0;
        } else {
            c = (1 - normalizedR - k) / (1 - k);
            m = (1 - normalizedG - k) / (1 - k);
            y = (1 - normalizedB - k) / (1 - k);
        }

        return String.format("%.2f, %.2f, %.2f, %.2f", c * 100, m * 100, y * 100, k * 100);
    }

    private static String convertCMYKToRGB(int c, int m, int y, int k) {
        double r = 255 * (1 - (double) c / 100) * (1 - (double) k / 100);
        double g = 255 * (1 - (double) m / 100) * (1 - (double) k / 100);
        double b = 255 * (1 - (double) y / 100) * (1 - (double) k / 100);
        return String.format("%.0f, %.0f, %.0f", r, g, b);
    }

    private static Color convertCMYKToColor(int c, int m, int y, int k) {
        double r = 255 * (1 - (double) c / 100) * (1 - (double) k / 100);
        double g = 255 * (1 - (double) m / 100) * (1 - (double) k / 100);
        double b = 255 * (1 - (double) y / 100) * (1 - (double) k / 100);
        return Color.rgb((int) r, (int) g, (int) b);
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
