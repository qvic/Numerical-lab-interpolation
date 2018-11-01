package controllers;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import methods.Lagrange;
import methods.LeastSquares;
import methods.Newton;
import methods.Spline;
import util.ChartUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.DoubleUnaryOperator;

public class MainController implements Initializable {

    private static final double X_LOWER_BOUND = 0;
    private static final double X_UPPER_BOUND = 50;
    private static final double Y_UPPER_BOUND = 100;
    private static final double Y_LOWER_BOUND = -100;
    private static final int X_TICK_UNIT = 5;
    private static final int Y_TICK_UNIT = 10;

    private static final double PLOT_TICK = .2;
    private static final double INTERPOLATION_TICK = .1;
    private static final int DEFAULT_M = 2;

    private static final DoubleUnaryOperator function = (x) -> Math.cos(Math.sin(x)) * x + 0.1 * x * x;
    private static final boolean USE_FILE = false;
    private static final Path CSV_FILE_PATH = Paths.get("/home/vic/Dropbox/projects/3kurs/numerical/lab_4/src/points.csv");

    @FXML
    private BorderPane lineChartPane;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton buildSpline;

    @FXML
    private RadioButton buildLagrange;

    @FXML
    private RadioButton buildNewton;

    @FXML
    private RadioButton buildLeastSquares;

    @FXML
    private TextField polynomialDegreeField;

    @FXML
    private ProgressIndicator progress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final NumberAxis xAxis = new NumberAxis(X_LOWER_BOUND, X_UPPER_BOUND, X_TICK_UNIT);
        final NumberAxis yAxis = new NumberAxis(Y_LOWER_BOUND, Y_UPPER_BOUND, Y_TICK_UNIT);

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);

        if (!USE_FILE) {
            ChartUtils.createSeries(lineChart, "function", function, X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK);
        }

        List<XYChart.Data<Number, Number>> input;
        if (USE_FILE) {
            input = ChartUtils.generateInputData(CSV_FILE_PATH);
        } else {
            input = ChartUtils.generateInputData(function, X_LOWER_BOUND, X_UPPER_BOUND, INTERPOLATION_TICK);
        }

        ChartUtils.createSeries(lineChart, "spline");
        ChartUtils.createSeries(lineChart, "lagrange");
        ChartUtils.createSeries(lineChart, "newton");
        ChartUtils.createSeries(lineChart, "least squares");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                ChartUtils.getSeriesByName("spline").setData(
                        ChartUtils.generateSeriesData(new Spline(input), X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK)
                );
                updateProgress(1, 4);

                ChartUtils.getSeriesByName("lagrange").setData(
                        ChartUtils.generateSeriesData(new Lagrange(input), X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK)
                );
                updateProgress(2, 4);

                ChartUtils.getSeriesByName("newton").setData(
                        ChartUtils.generateSeriesData(new Newton(input), X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK)
                );
                updateProgress(3, 4);

                ChartUtils.getSeriesByName("least squares").setData(
                        ChartUtils.generateSeriesData(new LeastSquares(input, DEFAULT_M), X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK)
                );
                updateProgress(4, 4);
                return null;
            }
        };

        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

        polynomialDegreeField.setText(String.valueOf(DEFAULT_M));
        polynomialDegreeField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int m = Integer.parseInt(newValue);
                ObservableList<XYChart.Data<Number, Number>> data = ChartUtils.generateSeriesData(
                        new LeastSquares(input, m), X_LOWER_BOUND, X_UPPER_BOUND, PLOT_TICK);
                ChartUtils.getSeriesByName("least squares").setData(data);
            } catch (NumberFormatException ignored) {
            }
        });

        ChartUtils.toggleSeriesByName("spline"); // default is spline

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == buildSpline) {
                ChartUtils.toggleSeriesByName("spline");
            } else if (newValue == buildLagrange) {
                ChartUtils.toggleSeriesByName("lagrange");
            } else if (newValue == buildNewton) {
                ChartUtils.toggleSeriesByName("newton");
            } else if (newValue == buildLeastSquares) {
                ChartUtils.toggleSeriesByName("least squares");
            }
        });

        lineChartPane.setCenter(lineChart);
    }
}
