package com.kpi.parallel.lab1.functions;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Class implemented {@link ArithmeticFunction} for calculating the function <b>2.14</b>
 * of the 1st lab work of "Parallel programming" in KPI FICT.
 * @see ArithmeticFunction
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public class FunctionB implements Runnable, ArithmeticFunction {
    /** The size of arrays and square matrices. */
    private final int size;

    /** The matrix <b>MF</b> */
    private final int[][] MF;
    /** The matrix <b>MG</b> */
    private final int[][] MG;
    /** The matrix <b>MH</b> */
    private final int[][] MH;

    /** Mapping of matrices and their names. */
    private final Map<String, int[][]> matrices;

    /**
     * All arguments-required constructor.
     * @param size size
     * @param MF MF
     * @param MG MG
     * @param MH MH
     */
    public FunctionB(int size, int[][] MF, int[][] MG, int[][] MH) {
        this.size = size;

        assert (MF.length == size &&
                MG.length == size &&
                MH.length == size &&
                Arrays.stream(MF).allMatch(row -> row.length == size) &&
                Arrays.stream(MG).allMatch(row -> row.length == size) &&
                Arrays.stream(MH).allMatch(row -> row.length == size));

        this.MF = MF;
        this.MG = MG;
        this.MH = MH;

        matrices = Map.of("MF", this.MF, "MG", this.MG, "MH", this.MH);
    }

    /**
     * Constructor that provides a self-entry of data by user.
     * @param size size
     */
    public FunctionB(int size) {
        this.size = size;

        MF = new int[size][size];
        MG = new int[size][size];
        MH = new int[size][size];

        matrices = Map.of("MF", MF, "MG", MG, "MH", MH);

        Scanner scanner = new Scanner(System.in);
        String input;
        String[] values;

        for (Map.Entry<String, int[][]> entry : matrices.entrySet()) {
            System.out.println("Enter data to " + entry.getKey() + ":");

            for (int i = 0; i < size; i++) {
                while (true) {
                    System.out.print(i + " -> ");
                    input = scanner.nextLine();
                    values = input.split(" ");

                    if (isCorrectArrayOfNumbers(values, size)) {
                        for (int j = 0; j < size; j++) {
                            entry.getValue()[i][j] = Integer.parseInt(values[j]);
                        }
                        break;
                    } else {
                        System.out.println("Incorrect format of incoming string. Please come again.");
                    }
                }
            }
        }
    }

    /**
     * Calculates the function <b>2.14</b>
     * @return matrix
     */
    public int[][] calculate() {
        int[][] result = MF;
        int[][] product = productMatrices(MG, MH);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] += product[i][j];
            }
        }

        for (int[] row : result) {
            Arrays.sort(row);
        }

        return result;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started");
        System.out.println("F2 = " + Arrays.deepToString(calculate()));
        System.out.println(threadName + " finished");
    }
}
