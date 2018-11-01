package util;

import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import methods.Interpolator;
import methods.Lagrange;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

public class ChartUtils {

    private static HashMap<String, XYChart.Series<Number, Number>> indeces = new HashMap<>();

    public static XYChart.Series<Number, Number> getSeriesByName(String name) {
        return indeces.get(name);
    }

    public static void createSeries(LineChart<Number, Number> lineChart, String name, DoubleUnaryOperator f, double lowerBound, double upperBound, double tick) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);

        for (double i = lowerBound; i < upperBound; i += tick) {
            XYChart.Data<Number, Number> element = new XYChart.Data<>(i, f.applyAsDouble(i));
            series.getData().add(element);
        }
        lineChart.getData().add(series);
    }

    public static void createSeries(LineChart<Number, Number> lineChart, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);
        series.setName(name);
        series.getNode().setVisible(false);
        indeces.put(name, series);
    }

    public static ObservableList<XYChart.Data<Number, Number>> generateSeriesData(Interpolator interpolator, double lowerBound, double upperBound, double tick) {
        ObservableList<XYChart.Data<Number, Number>> seriesData = FXCollections.observableArrayList();

        for (double i = lowerBound; i < upperBound; i += tick) {
            XYChart.Data<Number, Number> element = new XYChart.Data<>(i, interpolator.calculate(i));
            seriesData.add(element);
        }

        return seriesData;
    }

    public static List<XYChart.Data<Number, Number>> generateInputData(Path file) {
        try (Reader reader = Files.newBufferedReader(file);
             CSVReader csvReader = new CSVReader(reader)) {

            return csvReader.readAll()
                    .stream()
                    .map(strings -> new XYChart.Data<Number, Number>(Double.parseDouble(strings[0]), Double.parseDouble(strings[1])))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read csv file: " + e);
        }
    }

    public static List<XYChart.Data<Number, Number>> generateInputData(DoubleUnaryOperator f, double lowerBound, double upperBound, double interpolationTick) {
        List<XYChart.Data<Number, Number>> input = new ArrayList<>();
        for (double i = lowerBound; i <= upperBound; i += interpolationTick) {
            XYChart.Data<Number, Number> e = new XYChart.Data<>(i, f.applyAsDouble(i));
            input.add(e);
        }
        return input;
    }

    public static void toggleSeriesByName(String name) {
        indeces.get(name).getNode().setVisible(true);
        indeces.forEach((seriesName, series) -> {
            if (seriesName.equals(name)) {
                series.getNode().setVisible(true);
            } else {
                series.getNode().setVisible(false);
            }
        });
    }
}
