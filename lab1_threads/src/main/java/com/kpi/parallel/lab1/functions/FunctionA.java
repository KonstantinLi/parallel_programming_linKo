package com.kpi.parallel.lab1.functions;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Class implemented {@link ArithmeticFunction} for calculating the function <b>1.22</b>
 * of the 1st lab work of "Parallel programming" in KPI FICT.
 * @see ArithmeticFunction
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public class FunctionA implements Runnable, ArithmeticFunction {
    /** The size of arrays and square matrices. */
    private final int size;

    /** The array <b>A</b> */
    private final int[] A;
    /** The array <b>B</b> */
    private final int[] B;
    /** The array <b>C</b> */
    private final int[] C;
    /** The matrix <b>MA</b> */
    private final int[][] MA;
    /** The matrix <b>ME</b> */
    private final int[][] ME;

    /** Mapping of vectors and their names. */
    private final Map<String, int[]> vectors;
    /** Mapping of matrices and their names. */
    private final Map<String, int[][]> matrices;

    /**
     * All arguments-required constructor.
     * @param size size
     * @param A A
     * @param B B
     * @param C C
     * @param MA MA
     * @param ME ME
     */
    public FunctionA(int size, int[] A, int[] B, int[] C, int[][] MA, int[][] ME) {
        this.size = size;

        assert (A.length == size &&
                B.length == size &&
                C.length == size &&
                MA.length == size &&
                ME.length == size &&
                Arrays.stream(MA).allMatch(row -> row.length == size) &&
                Arrays.stream(ME).allMatch(row -> row.length == size)
                );

        this.A = A;
        this.B = B;
        this.C = C;
        this.MA = MA;
        this.ME = ME;

        vectors = Map.of("A", this.A, "B", this.B, "C", this.C);
        matrices = Map.of("MA", this.MA, "ME", this.ME);
    }

    /**
     * Constructor that provides a self-entry of data by user.
     * @param size size
     */
    public FunctionA(int size) {
        this.size = size;

        A = new int[size];
        B = new int[size];
        C = new int[size];

        MA = new int[size][size];
        ME = new int[size][size];

        vectors = Map.of("A", A, "B", B, "C", C);
        matrices = Map.of("MA", MA, "ME", ME);

        Scanner scanner = new Scanner(System.in);
        String input;
        String[] values;

        for (Map.Entry<String, int[]> entry : vectors.entrySet()) {
            while (true) {
                System.out.print("Enter data to " + entry.getKey() + " -> ");
                input = scanner.nextLine();
                values = input.split(" ");

                if (isCorrectArrayOfNumbers(values, size)) {
                    for (int i = 0; i < size; i++) {
                        entry.getValue()[i] = Integer.parseInt(values[i]);
                    }
                    break;
                } else {
                    System.out.println("Incorrect format of incoming string. Please come again.");
                }
            }
        }

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
     * Calculates the function <b>1.22</b>
     * @return number
     */
    public int calculate() {
        return scalarProduct(B, C) + scalarProduct(A, B) +
                scalarProduct(C, productMatrices(wrapVectorToMatrix(B),
                                productMatrices(MA, ME))[0]);
    }

    /**
     *
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started");
        System.out.println("F1 = " + calculate());
        System.out.println(threadName + " finished");
    }
}
