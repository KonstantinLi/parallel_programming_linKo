import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    public static final int N = 8;
    public static final int P = 4;
    public static final int H = N / P;

    public static int[][] MB = new int[N][N];
    public static int[][] MC = new int[N][N];
    public static int[][] MM = new int[N][N];
    public static int[][] MO = new int[N][N];
    public static int[][] MX = new int[N][N];

    public static int[] Z = new int[N];

    public static AtomicInteger a = new AtomicInteger();
    public static int d = 0;

    private static boolean firstMin = true;

    // монітор
    public static final Object object = new Object();

    // введення MB, MC в T1 (для T4)
    public static final Semaphore Sem0_1 = new Semaphore(0, true);
    // введення Z, d, MM в T4 (для T1, T2, T3)
    public static final Semaphore Sem0_2 = new Semaphore(0, true);
    // обчислення 1 в T1 (для T2, T3, T4)
    public static final Semaphore Sem1_1 = new Semaphore(0, true);
    // обчислення 1 в T2 (для T1, T3, T4)
    public static final Semaphore Sem1_2 = new Semaphore(0, true);
    // обичслення 1 в T3 (для T1, T2, T4)
    public static final Semaphore Sem1_3 = new Semaphore(0, true);
    // обчислення 1 в T4 (для T1, T2, T3)
    public static final Semaphore Sem1_4 = new Semaphore(0, true);
    // обчислення 2 в T1 (для T2)
    public static final Semaphore Sem2_1 = new Semaphore(0, true);
    // обчислення 2 в T3 (для T2)
    public static final Semaphore Sem2_2 = new Semaphore(0, true);
    // обчислення 2 в T4 (для T2)
    public static final Semaphore Sem2_3 = new Semaphore(0, true);

    // Мінімальне значення частини масива
    public static int min(int[] Z, int start, int end) {
        int min = Z[start];

        for (int i = start; i < end; i++) {
            if (Z[i] < min) {
                min = Z[i];
            }
        }

        return min;
    }

    public static int min(AtomicInteger a, int a_i) {
        if (firstMin) {
            firstMin = false;
            return a_i;
        }

        return Math.min(a.get(), a_i);
    }

    public static int[][] sumMatrix(int[][] MA, int[][] MB) {
        int[][] MY = new int[MA.length][MB[0].length];

        for (int i = 0; i < MY.length; i++) {
            for (int j = 0; j < MY[i].length; j++) {
                MY[i][j] = MA[i][j] + MB[i][j];
            }
        }

        return MY;
    }

    public static int[][] multiplySubMatrix(int[][] MA, int[][] MB, int start, int end) {
        int[][] MX = new int[N][end - start];

        for (int i = 0; i < N; i++) {
            int x = 0;
            for (int j = start; j < end; j++) {
                MX[i][x] = 0;
                for (int k = 0; k < N; k++) {
                    MX[i][x] += MA[i][k] * MB[k][j];
                }
                x++;
            }
        }

        return MX;
    }

    public int[][] multiplyMatrix(int[][] MA, int[][] MB) {
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

    public static int[][] multiplyMatrixAndScalar(int[][] MA, int a) {
        int[][] MY = new int[MA.length][MA[0].length];

        for (int i = 0; i < MY.length; i++) {
            for (int j = 0; j < MY[i].length; j++) {
                MY[i][j] = MA[i][j] * a;
            }
        }

        return MY;
    }

    public static int[][] multiplySubMatrixAndScalar(int[][] MA, int a, int start, int end) {
        int[][] MB = new int[N][end-start];

        for (int i = 0; i < N; i++) {
            int k = 0;
            for (int j = start; j < end; j++) {
                MB[i][k] = MA[i][j] * a;
                k++;
            }
        }

        return MB;
    }

    public static void calculateSubResult(int d_i, int a_i, int[][] MB_i, int start, int end) {
        writeToSubMatrix(sumMatrix(
                multiplyMatrixAndScalar(
                    multiplySubMatrix(MB_i, MX, start, end),
                    d_i),
                multiplySubMatrixAndScalar(MC, a_i, start, end)
            ), MO, start, end
        );
    }

    public static void writeToSubMatrix(int[][] MA, int[][] MB, int start, int end) {
        for (int i = 0; i < MA.length; i++) {
            int k = 0;
            for (int j = start; j < end; j++) {
                MB[i][j] = MA[i][k];
                k++;
            }
        }
    }

    public static void printMatrix(int[][] MA) {
        for (int i = 0; i < MA.length; i++) {
            for (int j = 0; j < MA[i].length; j++) {
                System.out.printf("%-3d", MA[i][j]);
            }
            System.out.println();
        }
    }
}


