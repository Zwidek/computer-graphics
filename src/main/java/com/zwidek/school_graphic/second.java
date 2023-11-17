package com.zwidek.school_graphic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class second extends Application {
    private static final Menu fileMenu = new Menu("Plik");
    private static final Menu filterMenu = new Menu("Filtry");
    private static Image image;
    private static MenuBar menuBar;
    private static Canvas canvas;
    private static GraphicsContext graphicsContext;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(second.class.getResource("second.fxml"));
        Parent root = getFilter(fxmlLoader);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/main.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static Parent getFilter(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        menuBar = (MenuBar) root.lookup("#menu_bar");
        canvas = (Canvas) root.lookup("#canvas");
        graphicsContext = canvas.getGraphicsContext2D();

        readFileFromPC(); // wczytanie pliku

        filterSmooth(); // logika filtru wygladzajacego
        filterMedian(); // logika filtru medianowego
        filterEdge(); // logika filtru krawędziowego
        filterDilatation(); // logika filtru dylatacji
        filterErosion(); // logika filtru erozji

        return root;
    }

    private static void filterDilatation() {
        MenuItem dilatationFilterMenuItem = new MenuItem("Filtr dylatacji");
        filterMenu.getItems().add(dilatationFilterMenuItem);

        dilatationFilterMenuItem.setOnAction(e -> applyDilatationLogic());
    }

    private static void filterErosion() {
        MenuItem erosionFilterMenuItem = new MenuItem("Filtr erozji");
        filterMenu.getItems().add(erosionFilterMenuItem);

        erosionFilterMenuItem.setOnAction(e -> showErosionDialog());
    }

    private static void filterMedian() {
        MenuItem median_filter_menu_item = new MenuItem("Filtr medianowy");
        filterMenu.getItems().add(median_filter_menu_item);

        median_filter_menu_item.setOnAction(e -> applyMedianFilter(image));
    }

    private static void filterEdge() {
        MenuItem edge_filter_menu_item = new MenuItem("Filtr wykrywania krawedzi");
        filterMenu.getItems().add(edge_filter_menu_item);

        edge_filter_menu_item.setOnAction(e -> applyEdgeFilter(image));
    }

    private static void filterSmooth() {
        MenuItem smooth_filter_menu_item = new MenuItem("Filtr wygładzający");
        filterMenu.getItems().add(smooth_filter_menu_item);

        smooth_filter_menu_item.setOnAction(e -> applySmoothingFilter(image));

        menuBar.getMenus().add(filterMenu);
    }


    private static void applyDilatationLogic() {
        TextInputDialog dialog = new TextInputDialog("3");
        dialog.setTitle("Filtr dylatacji");
        dialog.setHeaderText("Wprowadź rozmiar elementu strukturyzującego");
        dialog.setContentText("Rozmiar:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(size -> {
            int structuringElementSize = Integer.parseInt(size);
            showStructuringElementEditor(structuringElementSize, true);
        });
    }

    private static void applyErosionFilter(Image image, int[][] structuringElement) {
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int structuringElementRadiusX = structuringElement.length / 2;
        int structuringElementRadiusY = structuringElement[0].length / 2;

        for (int y = structuringElementRadiusY; y < height - structuringElementRadiusY; y++) {
            for (int x = structuringElementRadiusX; x < width - structuringElementRadiusX; x++) {
                boolean isHit = true;

                for (int dy = -structuringElementRadiusY; dy <= structuringElementRadiusY; dy++) {
                    for (int dx = -structuringElementRadiusX; dx <= structuringElementRadiusX; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (structuringElement[dx + structuringElementRadiusX][dy + structuringElementRadiusY] == 1 &&
                                (nx < 0 || nx >= width || ny < 0 || ny >= height || pixelReader.getColor(nx, ny).equals(Color.BLACK))) {
                            isHit = false;
                            break;
                        }
                    }

                    if (!isHit) {
                        break;
                    }
                }

                pixelWriter.setColor(x, y, isHit ? Color.WHITE : Color.BLACK);
            }
        }
    }


    private static void applyDilatationFilter(Image image, int[][] structuringElement) {
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int structuringElementRadiusX = structuringElement.length / 2;
        int structuringElementRadiusY = structuringElement[0].length / 2;

        for (int y = structuringElementRadiusY; y < height - structuringElementRadiusY; y++) {
            for (int x = structuringElementRadiusX; x < width - structuringElementRadiusX; x++) {
                boolean isHit = false;

                for (int dy = -structuringElementRadiusY; dy <= structuringElementRadiusY; dy++) {
                    for (int dx = -structuringElementRadiusX; dx <= structuringElementRadiusX; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (structuringElement[dx + structuringElementRadiusX][dy + structuringElementRadiusY] == 1 &&
                                nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            if (color.equals(Color.WHITE)) {
                                isHit = true;
                                break;
                            }
                        }
                    }

                    if (isHit) {
                        break;
                    }
                }

                pixelWriter.setColor(x, y, isHit ? Color.WHITE : Color.BLACK);
            }
        }
    }

    private static void applyEdgeFilter(Image image) {
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        int filterRadius = 1;

        for (int y = filterRadius; y < height - filterRadius; y++) {
            for (int x = filterRadius; x < width - filterRadius; x++) {
                double sumX = 0.0;
                double sumY = 0.0;

                for (int dy = -filterRadius; dy <= filterRadius; dy++) {
                    for (int dx = -filterRadius; dx <= filterRadius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            double grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;

                            sumX += grayValue * sobelX[dy + filterRadius][dx + filterRadius];
                            sumY += grayValue * sobelY[dy + filterRadius][dx + filterRadius];
                        }
                    }
                }

                double magnitude = Math.sqrt(sumX * sumX + sumY * sumY);

                magnitude = Math.min(1.0, Math.max(0.0, magnitude));

                pixelWriter.setColor(x, y, new Color(magnitude, magnitude, magnitude, 1.0));
            }
        }
    }

    private static void applyMedianFilter(Image image) {
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int filterRadius = 5;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] redValues = new double[(2 * filterRadius + 1) * (2 * filterRadius + 1)];
                double[] greenValues = new double[(2 * filterRadius + 1) * (2 * filterRadius + 1)];
                double[] blueValues = new double[(2 * filterRadius + 1) * (2 * filterRadius + 1)];

                int pixelCount = 0;

                for (int dy = -filterRadius; dy <= filterRadius; dy++) {
                    for (int dx = -filterRadius; dx <= filterRadius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redValues[pixelCount] = color.getRed();
                            greenValues[pixelCount] = color.getGreen();
                            blueValues[pixelCount] = color.getBlue();
                            pixelCount++;
                        }
                    }
                }

                Arrays.sort(redValues);
                Arrays.sort(greenValues);
                Arrays.sort(blueValues);

                double medianRed = redValues[pixelCount / 2];
                double medianGreen = greenValues[pixelCount / 2];
                double medianBlue = blueValues[pixelCount / 2];

                pixelWriter.setColor(x, y, new Color(medianRed, medianGreen, medianBlue, 1.0));
            }
        }
    }

    private static void applySmoothingFilter(Image inputImage) {
        PixelReader pixelReader = inputImage.getPixelReader();
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        int filterRadius = 5;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double redSum = 0.0;
                double greenSum = 0.0;
                double blueSum = 0.0;

                int pixelCount = 0;

                for (int dy = -filterRadius; dy <= filterRadius; dy++) {
                    for (int dx = -filterRadius; dx <= filterRadius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redSum += color.getRed();
                            greenSum += color.getGreen();
                            blueSum += color.getBlue();
                            pixelCount++;
                        }
                    }
                }

                double avgRed = redSum / pixelCount;
                double avgGreen = greenSum / pixelCount;
                double avgBlue = blueSum / pixelCount;

                pixelWriter.setColor(x, y, new Color(avgRed, avgGreen, avgBlue, 1.0));
            }
        }
    }

    private static void showErosionDialog() {
        TextInputDialog dialog = new TextInputDialog("3");
        dialog.setTitle("Filtr erozji");
        dialog.setHeaderText("Wprowadź rozmiar elementu strukturyzującego");
        dialog.setContentText("Rozmiar:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(size -> {
            int structuringElementSize = Integer.parseInt(size);
            showStructuringElementEditor(structuringElementSize, false);
        });
    }

    private static void showStructuringElementEditor(int size, boolean isDilatation) {
        Stage editorStage = new Stage();
        editorStage.setTitle(isDilatation ? "Edytor Elementu Strukturyzującego (Dylatacja)" : "Edytor Elementu Strukturyzującego (Erozja)");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        TextField[][] textFields = new TextField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                TextField textField = new TextField();
                textField.setPrefWidth(30);
                textField.setPrefHeight(30);
                textField.setAlignment(Pos.CENTER);
                textFields[i][j] = textField;
                gridPane.add(textField, j, i);
            }
        }

        Button confirmButton = new Button("Potwierdź");
        confirmButton.setOnAction(event -> {
            int[][] structuringElement = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    try {
                        structuringElement[i][j] = Integer.parseInt(textFields[i][j].getText());
                    } catch (NumberFormatException e) {
                        structuringElement[i][j] = 0;
                    }
                }
            }

            if (isDilatation) {
                applyDilatationFilter(image, structuringElement);
            } else {
                applyErosionFilter(image, structuringElement);
            }
            editorStage.close();
        });

        gridPane.add(confirmButton, size, size / 2);

        Scene scene = new Scene(gridPane, 400, 400);
        editorStage.setScene(scene);
        editorStage.show();
    }

    private static void readFileFromPC() {
        MenuItem choosePhotosItem = new MenuItem("Wybierz zdjęcie");
        choosePhotosItem.setOnAction(e -> openImageFile());

        fileMenu.getItems().add(choosePhotosItem);
        menuBar.getMenus().add(fileMenu);
    }

    private static void openImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("zdjęcia", "*.jpg", "*.png", "*.gif", "*.bmp", "*.jpeg"));
        fileChooser.setTitle("Wybierz zdjęcie");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if (image != null) {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
            image = new Image(selectedFile.toURI().toString());
            graphicsContext.drawImage(image, 0, 0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
