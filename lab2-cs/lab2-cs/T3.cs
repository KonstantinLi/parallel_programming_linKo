using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab2_cs
{
    internal class T3
    {
        int f_3;
        int p_3;
        int x_3;
        int[] D_3 = new int[Program.N];
        int[,] ME_3 = new int[Program.N, Program.N];

        public void Work()
        {
            Console.WriteLine("Запуск Т3");
            Console.WriteLine("Ввід ME");
            for (int i = 0; i < Program.N; i++)
            {
                for (int j = 0; j < Program.N; j++)
                {
                    Program.ME[i, j] = 1;
                }
            }

            Console.WriteLine("Ввід B,E");
            for (int i = 0; i < Program.N; i++)
            {
                Program.B[i] = 1;
                Program.E[i] = 1;
            }

            Program.Sem0_2.Release(3);

            Console.WriteLine("Т3 очікує ввід даних");
            Program.Sem0_1.WaitOne();
            Program.Sem0_3.WaitOne();

            Console.WriteLine("Т3 - початок обчислення 1");
            f_3 = Program.MultiplySubScalar(Program.B, Program.C, Program.H * 2, Program.H * 3);

            // КД1-3
            Console.WriteLine("Т3 - початок обчислення 2");
            Interlocked.Add(ref Program.f, f_3);

            Console.WriteLine("Т3 - кінець обчислень 1,2");
            Program.Sem1_3.Release(3);

            Console.WriteLine("Т3 очікує завершення обчислень 1,2");
            Program.Sem1_1.WaitOne();
            Program.Sem1_2.WaitOne();
            Program.Sem1_4.WaitOne();

            // КД2-3
            Console.WriteLine("Т3 захоплює мютекс");
            Program.Mutex.WaitOne();
            for (int i = 0; i < Program.N; i++)
            {
                D_3[i] = Program.D[i];
            }
            Program.Mutex.ReleaseMutex();
            Console.WriteLine("Т3 звільнює мютекс");

            // КД3-3
            Console.WriteLine("Т3 - критична секція");
            lock (Program.Locker)
            {
                for (int i = 0; i < Program.N; i++)
                {
                    for (int j = 0; j < Program.N; j++)
                    {
                        ME_3[i, j] = Program.ME[i, j];
                    }
                }
            }

            Console.WriteLine("Т3 - початок обчислення 3");
            Program.G_H3 = Program.SortVector(
                Program.MultiplyMatrixAndVector(
                    Program.MultiplySubMatrix(ME_3, Program.MM, Program.H * 2, Program.H * 3),
                    D_3));

            Console.WriteLine("Т3 - кінець обчислення 3");
            Program.Sem2_3.Release(2);

            Console.WriteLine("Т3 очікує завершення обчислення 5");
            Program.Event.WaitOne();

            // КД4-3
            Interlocked.Exchange(ref p_3, Program.p);
            // КД5-3
            Interlocked.Exchange(ref f_3, Program.f);
            // КД6-3
            Interlocked.Exchange(ref x_3, Program.x);

            Console.WriteLine("Т3 - початок обчислення 6");
            Program.calculateSubResult(
                Program.G,
                Program.E,
                p_3, f_3, x_3,
                Program.H * 2, Program.H * 3);

            Console.WriteLine("Т3 - кінець обчислення 3");
            Program.Sem4_3.Release(1);
        }
    }
}
