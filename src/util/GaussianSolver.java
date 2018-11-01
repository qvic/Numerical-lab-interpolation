package util;

import util.Matrices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GaussianSolver {

    public static List<Double> solve(double[][] matrixA, double[] vectorB) {
        if (matrixA.length == 0 || vectorB.length == 0 ||
                matrixA.length != vectorB.length ||
                matrixA.length != matrixA[0].length) {
            throw new UnsupportedOperationException("Check matrix size");
        }

        int N = matrixA.length;

        double[][] A = new double[N][];
        for (int i = 0; i < N; i++) {
            A[i] = new double[N];
            System.arraycopy(matrixA[i], 0, A[i], 0, N);
        }

        double[] b = new double[N];
        System.arraycopy(vectorB, 0, b, 0, N);

        for (int i = 0; i < N; i++) {
            int pivotRow = getPivotRowIndex(A, i);
            if (Double.compare(A[pivotRow][i], 0) == 0) {
                return List.of();
            }
            Matrices.swapRows(A, i, pivotRow);
            Matrices.swapElements(b, i, pivotRow);

            for (int j = i + 1; j < N; j++) {
                double multiplier = -A[j][i] / A[i][i];
                Matrices.add(A, i, j, multiplier);
                Matrices.add(b, i, j, multiplier);
            }
        }

        ArrayList<Double> results = solveUpperTriangularMatrix(A, b);

        return Collections.unmodifiableList(results);
    }

    private static int getPivotRowIndex(double[][] matrixA, int columnIndex) {
        int pivotRow = columnIndex;
        for (int j = columnIndex + 1; j < matrixA.length; j++) {
            if (Math.abs(matrixA[j][columnIndex]) > Math.abs(matrixA[pivotRow][columnIndex])) {
                pivotRow = j;
            }
        }
        return pivotRow;
    }

    private static ArrayList<Double> solveUpperTriangularMatrix(double[][] matrixA, double[] vectorB) {
        ArrayList<Double> results = new ArrayList<>();
        int N = matrixA.length;

        for (int i = N - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < N; j++) {
                sum += matrixA[i][j] * results.get(N - j - 1);
            }
            results.add((vectorB[i] - sum) / matrixA[i][i]);
        }

        Collections.reverse(results);
        return results;
    }
}
