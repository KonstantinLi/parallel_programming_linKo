public class T4 extends Thread {
    private int[][] MC_4 = new int[Data.N][Data.N]; // MC
    private int[][] MB_4 = new int[Data.N][Data.N]; // MB
    private int a1_4; // min(Z_H)
    private int a_4; // a
    private int d_4; // d

    @Override
    public void run() {
        try {
            System.out.println("Старт T4");

            System.out.println("T4 чекає на дозвіл Sem0_1");
            Data.Sem0_1.acquire();
            System.out.println("T4 отримує дозвіл Sem0_1");

            System.out.println("Ввід матриці MM");
            for (int i = 0; i < Data.N; i++) {
                for (int j = 0; j < Data.N; j++) {
                    Data.MM[i][j] = 1;
                }
            }

            System.out.println("Ввід вектора Z");
            for (int i = 0; i < Data.N; i++) {
                Data.Z[i] = 1;
            }

            System.out.println("Ввід скаляра d");
            Data.d = 1;

            System.out.println("T4 звільнює 3 дозволи Sem0_2");
            Data.Sem0_2.release(3);

            synchronized (Data.object) {
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MC[i], 0, MC_4[i], 0, Data.N);
                }
            }

            Data.writeToSubMatrix(
                    Data.multiplySubMatrix(MC_4, Data.MM, Data.H * 3, Data.N),
                    Data.MX, Data.H * 3, Data.N);

            a1_4 = Data.min(Data.Z, Data.H * 3, Data.N);
            Data.a.set(Data.min(Data.a, a1_4));

            System.out.println("T4 звільнює 3 дозволи Sem1_4");
            Data.Sem1_4.release(3);

            System.out.println("T4 чекає на дозволи Sem1_1, Sem1_2, Sem1_3");
            Data.Sem1_1.acquire();
            Data.Sem1_2.acquire();
            Data.Sem1_3.acquire();
            System.out.println("T4 отримує дозволи Sem1_1, Sem1_2, Sem1_3");

            a_4 = Data.a.get();
            synchronized (Data.object) {
                d_4 = Data.d;
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MB[i], 0, MB_4[i], 0, Data.N);
                }
            }

            Data.calculateSubResult(d_4, a_4, MB_4, Data.H * 3, Data.N);

            System.out.println("T4 звільнює дозвіл Sem2_3");
            Data.Sem2_3.release(1);

            System.out.println("Завершення T4");
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
