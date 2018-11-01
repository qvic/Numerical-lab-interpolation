package methods;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class Newton implements Interpolator {

    private ArrayList<Double> x, coeffs;

    public Newton(List<XYChart.Data<Number, Number>> data) {
        int N = data.size();
        x = new ArrayList<>(N);
        coeffs = new ArrayList<>(N);
        ArrayList<Double> y = new ArrayList<>(N);

        data.forEach(xyData -> {
            x.add(xyData.getXValue().doubleValue());
            y.add(xyData.getYValue().doubleValue());
        });

        double f[][] = new double[N][N];
        for (int i = 0; i < N; i++) {
            f[i][0] = y.get(i);
        }

        for (int i = 1; i < N; i++) {
            for (int j = 0; j < N - i; j++) {
                f[j][i] = (f[j + 1][i - 1] - f[j][i - 1]) / (x.get(i + j) - x.get(j));
            }
        }

        for (int i = 0; i < N; i++) {
            coeffs.add(f[0][i]);
        }
    }

    @Override
    public double calculate(double xValue) {
        double result = coeffs.get(0);
        double product = 1.0;
        for (int i = 1; i < coeffs.size(); i++) {
            product *= xValue - x.get(i-1);
            result += coeffs.get(i) * product;
        }
        return result;
    }
}