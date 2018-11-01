package methods;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class Spline implements Interpolator {

    private ArrayList<List<Double>> coeffs;
    private ArrayList<Double> x;

    public Spline(List<XYChart.Data<Number, Number>> data) {
        int n = data.size() - 1;

        coeffs = new ArrayList<>();
        x = new ArrayList<>();

        data.forEach(numberNumberData -> {
            x.add(numberNumberData.getXValue().doubleValue());
        });

        double h[] = new double[n];
        double alpha[] = new double[n];
        double l[] = new double[n + 1];
        l[0] = 1;
        double m[] = new double[n];
        double z[] = new double[n + 1];
        double c[] = new double[n + 1];
        double b[] = new double[n];
        double d[] = new double[n];

        h[0] = data.get(1).getXValue().doubleValue() - data.get(0).getXValue().doubleValue();
        for (int i = 1; i < n; i++) {
            h[i] = data.get(i + 1).getXValue().doubleValue() - data.get(i).getXValue().doubleValue();
            alpha[i] = 3 * (data.get(i + 1).getYValue().doubleValue() - data.get(i).getYValue().doubleValue()) / h[i];
            alpha[i] -= 3 * (data.get(i).getYValue().doubleValue() - data.get(i - 1).getYValue().doubleValue()) / h[i - 1];
        }

        for (int i = 1; i < n; i++) {
            l[i] = 2 * (data.get(i + 1).getXValue().doubleValue() - data.get(i - 1).getXValue().doubleValue()) - h[i - 1] * m[i - 1];
            m[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }

        l[n] = 1;

        for (int j = n - 1; j >= 0; j--) {
            c[j] = z[j] - m[j] * c[j + 1];
            b[j] = (data.get(j + 1).getYValue().doubleValue() - data.get(j).getYValue().doubleValue()) / h[j];
            b[j] -= h[j] * (c[j + 1] + 2 * c[j]) / 3;
            d[j] = (c[j + 1] - c[j]) / (3 * h[j]);
        }

        for (int i = 0; i < n; i++) {
            List<Double> p = List.of(data.get(i).getYValue().doubleValue(), b[i], c[i], d[i]);
            coeffs.add(p);
        }
    }

    private double polynomialValue(double xValue, List<Double> coeffs) {
        double result = coeffs.get(3);
        result = result * xValue + coeffs.get(2);
        result = result * xValue + coeffs.get(1);
        result = result * xValue + coeffs.get(0);
        return result;
    }

    @Override
    public double calculate(double xValue) {
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i + 1) >= xValue) {
                return polynomialValue(xValue - x.get(i), coeffs.get(i));
            }
        }
        return Double.NaN;
    }
}
