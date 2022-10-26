
public class T1 extends Thread {
    private int[][] MC_1 = new int[Data.N][Data.N]; // MC
    private int[][] MB_1 = new int[Data.N][Data.N]; // MB
    private int a1_1; // min(Z_H)
    private int a_1; // a
    private int d_1; // d

    @Override
    public void run() {
        try {
            System.out.println("Старт T1");

            System.out.println("Ввід матриці MB");
            for (int i = 0; i < Data.N; i++) {
                for (int j = 0; j < Data.N; j++) {
                    Data.MB[i][j] = 1;
                }
            }

            System.out.println("Ввід матриці MC");
            for (int i = 0; i < Data.N; i++) {
                for (int j = 0; j < Data.N; j++) {
                    Data.MC[i][j] = 1;
                }
            }

            System.out.println("T1 звільнює дозвіл Sem0_1");
            Data.Sem0_1.release(1);

            System.out.println("T1 чекає на дозвіл Sem0_2");
            Data.Sem0_2.acquire();
            System.out.println("T1 отримує дозвіл Sem0_2");

            synchronized (Data.object) {
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MC[i], 0, MC_1[i], 0, Data.N);
                }
            }

            Data.writeToSubMatrix(
                    Data.multiplySubMatrix(MC_1, Data.MM, 0, Data.H),
                    Data.MX, 0, Data.H);

            a1_1 = Data.min(Data.Z, 0, Data.H);
            Data.a.set(Data.min(Data.a, a1_1));

            System.out.println("T1 звільнює 3 дозволи Sem1_1");
            Data.Sem1_1.release(3);

            System.out.println("T1 чекає на дозволи Sem1_2, Sem1_3, Sem1_4");
            Data.Sem1_2.acquire();
            Data.Sem1_3.acquire();
            Data.Sem1_4.acquire();
            System.out.println("T1 отримує дозволи Sem1_2, Sem1_3, Sem1_4");

            a_1 = Data.a.get();
            synchronized (Data.object) {
                d_1 = Data.d;
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MB[i], 0, MB_1[i], 0, Data.N);
                }
            }

            Data.calculateSubResult(d_1, a_1, MB_1, 0, Data.H);

            System.out.println("T1 звільнює дозвіл Sem2_1");
            Data.Sem2_1.release(1);

            System.out.println("Завершення T1");
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
