public class T3 extends Thread {
    private int[][] MC_3 = new int[Data.N][Data.N]; // MC
    private int[][] MB_3 = new int[Data.N][Data.N]; // MB
    private int a1_3; // min(Z_H)
    private int a_3; // a
    private int d_3; // d

    @Override
    public void run() {
        try {
            System.out.println("Старт T3");

            System.out.println("T3 чекає на дозвіл Sem0_2");
            Data.Sem0_2.acquire();
            System.out.println("T3 отримує дозвіл Sem0_2");

            synchronized (Data.object) {
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MC[i], 0, MC_3[i], 0, Data.N);
                }
            }

            Data.writeToSubMatrix(
                    Data.multiplySubMatrix(MC_3, Data.MM, Data.H * 2, Data.H * 3),
                    Data.MX, Data.H * 2, Data.H * 3);

            a1_3 = Data.min(Data.Z, Data.H * 2, Data.H * 3);
            Data.a.set(Data.min(Data.a, a1_3));

            System.out.println("T3 звільнює 3 дозволи Sem1_3");
            Data.Sem1_3.release(3);

            System.out.println("T3 чекає на дозволи Sem1_1, Sem1_2, Sem1_4");
            Data.Sem1_1.acquire();
            Data.Sem1_2.acquire();
            Data.Sem1_4.acquire();
            System.out.println("T3 отримує дозволи Sem1_1, Sem1_2, Sem1_4");

            a_3 = Data.a.get();
            synchronized (Data.object) {
                d_3 = Data.d;
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MB[i], 0, MB_3[i], 0, Data.N);
                }
            }

            Data.calculateSubResult(d_3, a_3, MB_3, Data.H * 2, Data.H * 3);

            System.out.println("T3 звільнює дозвіл Sem2_2");
            Data.Sem2_2.release(1);

            System.out.println("Завершення T3");
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
