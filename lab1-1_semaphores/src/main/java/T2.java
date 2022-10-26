public class T2 extends Thread {
    private int[][] MC_2 = new int[Data.N][Data.N]; // MC
    private int[][] MB_2 = new int[Data.N][Data.N]; // MB
    private int a1_2; // min(Z_H)
    private int a_2; // a
    private int d_2; // d

    @Override
    public void run() {
        try {
            System.out.println("Старт T2");

            System.out.println("T2 чекає на дозвіл Sem0_2");
            Data.Sem0_2.acquire();
            System.out.println("T2 отримує дозвіл Sem0_2");

            synchronized (Data.object) {
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MC[i], 0, MC_2[i], 0, Data.N);
                }
            }

            Data.writeToSubMatrix(
                    Data.multiplySubMatrix(MC_2, Data.MM, Data.H, Data.H * 2),
                    Data.MX, Data.H, Data.H * 2);

            a1_2 = Data.min(Data.Z, Data.H, Data.H * 2);
            Data.a.set(Data.min(Data.a, a1_2));

            System.out.println("T2 звільнює 3 дозволи Sem1_2");
            Data.Sem1_2.release(3);

            System.out.println("T2 чекає на дозволи Sem1_1, Sem1_3, Sem1_4");
            Data.Sem1_1.acquire();
            Data.Sem1_3.acquire();
            Data.Sem1_4.acquire();
            System.out.println("T2 отримує дозволи Sem1_1, Sem1_3, Sem1_4");

            a_2 = Data.a.get();
            synchronized (Data.object) {
                d_2 = Data.d;
                for (int i = 0; i < Data.N; i++) {
                    System.arraycopy(Data.MB[i], 0, MB_2[i], 0, Data.N);
                }
            }

            Data.calculateSubResult(d_2, a_2, MB_2, Data.H, Data.H * 2);

            System.out.println("T2 чекає на дозволи Sem2_1, Sem2_2, Sem2_3");
            Data.Sem2_1.acquire();
            Data.Sem2_2.acquire();
            Data.Sem2_3.acquire();
            System.out.println("T2 отримує дозволи Sem2_1, Sem2_2, Sem2_3");

            System.out.println("Вивід результату MO:");
            Data.printMatrix(Data.MO);

            System.out.println("Завершення T2");
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
