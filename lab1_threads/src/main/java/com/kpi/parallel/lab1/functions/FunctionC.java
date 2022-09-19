package com.kpi.parallel.lab1.functions;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Class implemented {@link ArithmeticFunction} for calculating the function <b>3.14</b>
 * of the 1st lab work of "Parallel programming" in KPI FICT.
 * @see ArithmeticFunction
 * @version 1.0-SNAPSHOT
 * @author Linenko Kostyantyn IO-01
 */
public class FunctionC implements Runnable, ArithmeticFunction {

    /** Semaphore. */
    Semaphore sem;

    /** The size of arrays and square matrices. */
    private final int size;

    /** The array <b>O</b> */
    private final int[] O;
    /** The array <b>P</b> */
    private final int[] P;
    /** The matrix <b>MP</b> */
    private final int[][] MP;
    /** The matrix <b>MS</b> */
    private final int[][] MS;

    /** Mapping of vectors and their names. */
    private final Map<String, int[]> vectors;
    /** Mapping of matrices and their names. */
    private final Map<String, int[][]> matrices;

    /**
     * All arguments-required constructor
     * @param size size
     * @param O O
     * @param P P
     * @param MP MP
     * @param MS MS
     */
    public FunctionC(Semaphore sem, int size, int[] O, int[] P, int[][] MP, int[][] MS) {
        this.sem = sem;
        this.size = size;

        assert (O.length == size &&
                P.length == size &&
                MP.length == size &&
                MS.length == size &&
                Arrays.stream(MP).allMatch(row -> row.length == size) &&
                Arrays.stream(MS).allMatch(row -> row.length == size));

        this.O = O;
        this.P = P;
        this.MP = MP;
        this.MS = MS;

        vectors = Map.of("O", this.O, "P", this.P);
        matrices = Map.of("MP", this.MP, "MS", this.MS);
    }

    /**
     * Constructor that provides a self-entry of data by user.
     * @param size size
     */
    public FunctionC(Semaphore sem, int size) {
        this.sem = sem;
        this.size = size;

        O = new int[size];
        P = new int[size];

        MP = new int[size][size];
        MS = new int[size][size];

        vectors = Map.of("O", O, "P", P);
        matrices = Map.of("MP", MP, "MS", MS);

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
     * Calculates the function <b>3.14</b>
     * @return vector
     */
    public int[] calculate() {
        int[] result = O;
        for (int i = 0; i < size; i++) {
            result[i] += P[i];
        }

        return productMatrices(wrapVectorToMatrix(result), productMatrices(MP, MS))[0];
    }

    /**
     * Run method
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Запуск потоку " + threadName);

        try {
            System.out.println("Потік " + threadName + " чекає на дозвіл");
            sem.acquire();

            System.out.println("Потік " + threadName + " отримує дозвіл");
            int[] result = calculate();
            System.out.println("F3 = " + Arrays.toString(result));

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("Потік " + threadName + " звільняє дозвіл");
        sem.release();
    }
}
