/**
 * Паралельне програмування
 * Лабораторна робота №1
 * Варіант 27
 * Завдання: M0 = MB * (MC*MM) * d + min(Z) * MC
 * ПВВ1 - MB, MC; ПВВ2 - MO; ПВВ4 – Z, d, MM
 * Ліненко Костянтин ІО-01
 * Дата 26.10.2022
 **/
public class Main {
    public static void main(String[] args) {
        System.out.println("Старт T0");
        System.out.println("N = " + Data.N + "\n");

        T1 t1 = new T1();
        T2 t2 = new T2();
        T3 t3 = new T3();
        T4 t4 = new T4();

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Завершення T0");
    }
}
