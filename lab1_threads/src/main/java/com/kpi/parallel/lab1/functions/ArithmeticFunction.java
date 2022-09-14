package com.kpi.parallel.lab1.functions;

import java.util.Arrays;

/**
 * The <b>ArithmeticFunction</b> interface should be implemented by any class,
 * whose main goal is to calculate an arithmetic function. Interface provides default methods,
 * that contain basic operations on <u>vectors</u> and <u>matrices</u>.
 * This software is created special for executing lab work for "Parallel programming" in KPI FICT.
 * @see FunctionA
 * @see FunctionB
 * @see FunctionC
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public interface ArithmeticFunction {

    /**
     * Computes a scalar product of vectors.
     * @param A vector1
     * @param B vector2
     * @return scalar
     */
    default int scalarProduct(int[] A, int[] B) {
        assert (A.length == B.length);

        int result = 0;
        for (int i = 0; i < A.length; i++) {
            result += A[i] * B[i];
        }

        return result;
    }

    /**
     * Computes a matrix obtained after multiplying two matrices.
     * @param MA matrix1
     * @param MB matrix2
     * @return matrix3
     */
    default int[][] productMatrices(int[][] MA, int[][] MB) {
        int m  = MA.length;
        int n = MB[0].length;
        int o = MB.length;

        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; k++) {
                    result[i][j] += MA[i][k] * MB[k][j];
                }
            }
        }

        return result;
    }

    /**
     * Wrap a vector to the one-row matrix.
     * It's necessary to have compatibility between vectors and matrices.
     * @param vector vector
     * @return matrix
     */
    default int[][] wrapVectorToMatrix(int[] vector) {
        int[][] matrix = new int[1][vector.length];
        for (int i = 0; i < matrix[0].length; i++) {
            matrix[0][i] = vector[i];
        }

        return matrix;
    }

    /**
     * Validate an input string of numbers, that use to fill out data to a variable.
     * @param values values
     * @param size size
     * @return true or false
     */
    default boolean isCorrectArrayOfNumbers(String[] values, int size) {
        return values.length == size &&
                Arrays.stream(values).allMatch(string -> string.matches("[-+]?\\d+"));
    }
}
