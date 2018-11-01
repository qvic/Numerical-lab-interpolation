package methods;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class Spline implements Interpolator {

    private ArrayList<ArrayList<Double>> coeffs;
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
            ArrayList<Double> p = new ArrayList<>();
            p.add(data.get(i).getYValue().doubleValue());
            p.add(b[i]);
            p.add(c[i]);
            p.add(d[i]);
            coeffs.add(p);
        }
    }

    @Override
    public double calculate(double xValue) {
        double result = Double.NaN;
        for (int i = 0; i < x.size() - 1; i++) {
            if (x.get(i + 1) >= xValue) {
                double xi = x.get(i);
                result = coeffs.get(i).get(0);
                result += coeffs.get(i).get(1) * (xValue - xi);
                result += coeffs.get(i).get(2) * Math.pow(xValue - xi, 2);
                result += coeffs.get(i).get(3) * Math.pow(xValue - xi, 3);
                break;
            }
        }
        return result;
    }
}
