package methods;

import javafx.scene.chart.XYChart;
import util.GaussianSolver;
import util.Matrices;

import java.util.List;

public class LeastSquares implements Interpolator {

    private List<Double> polynomial;

    public LeastSquares(List<XYChart.Data<Number, Number>> data, int m) {
        int n = data.size();
        double[][] X = new double[n][m + 1];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            XYChart.Data<Number, Number> point = data.get(i);

            y[i] = point.getYValue().doubleValue();
            for (int j = 0; j < m + 1; j++) {
                X[i][j] = Math.pow(point.getXValue().doubleValue(), j);
            }
        }

        double[][] XT = Matrices.transpose(X);
        double[][] A = Matrices.multiply(XT, X);
        double[] b = Matrices.multiply(XT, y);

        polynomial = GaussianSolver.solve(A, b);
    }

    private double polynomialValue(double x) {
        double result = 0;
        int power = 0;
        for (Double a : polynomial) {
            result += a * Math.pow(x, power);
            power++;
        }
        return result;
    }


    @Override
    public double calculate(double xValue) {
        return polynomialValue(xValue);
    }
}
