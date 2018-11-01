package methods;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class Lagrange implements Interpolator {

    private ArrayList<Double> nominators, x, y;

    public Lagrange(List<XYChart.Data<Number, Number>> data) {
        int N = data.size();
        x = new ArrayList<>(N);
        y = new ArrayList<>(N);
        nominators = new ArrayList<>(N);

        data.forEach(xyData -> {
            x.add(xyData.getXValue().doubleValue());
            y.add(xyData.getYValue().doubleValue());
        });

        for (int i = 0; i < N; i++) {
            double product = 1.0;
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    product *= x.get(i) - x.get(j);
                }
            }
            nominators.add(product);
        }
    }

    @Override
    public double calculate(double xValue) {
        int N = x.size();
        double result = 0;
        for (int i = 0; i < N; i++) {
            double product = 1.0;
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    product *= xValue - x.get(j);
                }
            }
            product /= nominators.get(i);
            result += y.get(i) * product;
        }
        return result;
    }
}
