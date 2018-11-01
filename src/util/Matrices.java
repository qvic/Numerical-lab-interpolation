package util;

import java.math.BigDecimal;
import java.util.List;

public final class Matrices {

    static void swapRows(double[][] matrix, int i, int j) {
        if (i == j) return;
        double[] temp = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = temp;
    }

    static void swapElements(double[] vector, int i, int j) {
        if (i == j) return;
        double temp = vector[i];
        vector[i] = vector[j];
        vector[j] = temp;
    }

    static void add(double[][] matrix, int row, int toRow, double multiplier) {
        for (int k = 0; k < matrix.length; k++) {
            matrix[toRow][k] += matrix[row][k] * multiplier;
        }
    }

    public static double[] multiply(double[][] A, double[] b) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = b.length;
        if (aColumns != bRows) {
            throw new IllegalArgumentException("Check matrices sizes");
        }

        double[] result = new double[aRows];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                result[i] += A[i][j] * b[j];
            }
        }
        return result;
    }

    public static double[][] multiply(double[][] A, double[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;
        if (aColumns != bRows) {
            throw new IllegalArgumentException("Check matrices sizes");
        }

        double[][] result = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] transpose(double[][] A) {
        int aRows = A.length;
        int aColumns = A[0].length;
        double[][] result = new double[aColumns][aRows];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                result[j][i] = A[i][j];
            }
        }
        return result;
    }

    static void add(double[] vector, int row, int toRow, double multiplier) {
        vector[toRow] += vector[row] * multiplier;
    }

    public static void printMatrix(String prefix, double[][] matrix) {
        System.out.println(prefix);
        for (double[] row : matrix) {
            for (double element : row) {
                System.out.printf("%15.5f", element);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printVector(String prefix, double[] vector) {
        System.out.println(prefix);
        for (double element : vector) {
            System.out.printf("%15.5f\n", element);
        }
        System.out.println();
    }

    public static <T> void printVector(String prefix, List<T> vector) {
        System.out.println(prefix);
        if (vector.size() == 0) {
            System.out.println("∅");
        } else {
            for (int i = 0; i < vector.size(); i++) {
                T element = vector.get(i);
                System.out.printf("x%-2d = ", i + 1);
                System.out.println(element);
            }
        }
        System.out.println();
    }
}
