using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab2_cs
{
    internal class T4
    {
        int f_4;
        int p_4;
        int x_4;
        int[] D_4 = new int[Program.N];
        int[,] ME_4 = new int[Program.N, Program.N];

        public void Work()
        {
            Console.WriteLine("Запуск Т4");
            Console.WriteLine("Ввід C");
            for (int i = 0; i < Program.N; i++)
            {
                Program.C[i] = 1;
            }

            Console.WriteLine("Ввід x");
            Program.x = 1;

            Program.Sem0_3.Release(3);

            Console.WriteLine("Т4 очікує ввід даних");
            Program.Sem0_1.WaitOne();
            Program.Sem0_2.WaitOne();

            Console.WriteLine("Т4 - початок обчислення 1");
            f_4 = Program.MultiplySubScalar(Program.B, Program.C, Program.H * 3, Program.N);

            // КД1-4
            Console.WriteLine("Т4 - початок обчислення 2");
            Interlocked.Add(ref Program.f, f_4);

            Console.WriteLine("Т4 - кінець обчислень 1,2");
            Program.Sem1_4.Release(3);

            Console.WriteLine("Т4 очікує завершення обчислень 1,2");
            Program.Sem1_1.WaitOne();
            Program.Sem1_2.WaitOne();
            Program.Sem1_3.WaitOne();

            // КД2-4
            Console.WriteLine("Т4 захоплює мютекс");
            Program.Mutex.WaitOne();
            for (int i = 0; i < Program.N; i++)
            {
                D_4[i] = Program.D[i];
            }
            Console.WriteLine("Т4 звільнює мютекс");
            Program.Mutex.ReleaseMutex();

            // КД3-4
            Console.WriteLine("Т4 - критична секція");
            lock (Program.Locker)
            {
                for (int i = 0; i < Program.N; i++)
                {
                    for (int j = 0; j < Program.N; j++)
                    {
                        ME_4[i, j] = Program.ME[i, j];
                    }
                }
            }

            Console.WriteLine("Т4 - початок обчислення 3");
            Program.G_H4 = Program.SortVector(
                Program.MultiplyMatrixAndVector(
                    Program.MultiplySubMatrix(ME_4, Program.MM, Program.H * 3, Program.N),
                    D_4));

            Console.WriteLine("Т4 - кінець обчислення 3");
            Program.Sem2_4.Release(2);

            Console.WriteLine("Т4 очікує завершення обчислення 5");
            Program.Event.WaitOne();

            // КД4-4
            Interlocked.Exchange(ref p_4, Program.p);
            // КД5-4
            Interlocked.Exchange(ref f_4, Program.f);
            // КД6-4
            Interlocked.Exchange(ref x_4, Program.x);

            Console.WriteLine("Т4 - початок обчислення 6");
            Program.calculateSubResult(
                Program.G,
                Program.E,
                p_4, f_4, x_4,
                Program.H * 3, Program.N);
            Console.WriteLine("Т4 - кінець обчислення 6");

            Console.WriteLine("Т4 очікує завершення обчислень 6");
            Program.Sem4_1.WaitOne();
            Program.Sem4_2.WaitOne();
            Program.Sem4_3.WaitOne();

            Console.Write("Вивід результату: ");
            Program.PrintVector(Program.A);
        }
    }
}
