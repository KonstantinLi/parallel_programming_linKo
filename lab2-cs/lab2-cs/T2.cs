using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab2_cs
{
    internal class T2
    {
        int f_2;
        int p_2;
        int x_2;
        int[] D_2 = new int[Program.N];
        int[,] ME_2 = new int[Program.N, Program.N];

        public void Work()
        {
            Console.WriteLine("Запуск Т2");
            Console.WriteLine("Ввід D");
            for (int i = 0; i < Program.N; i++)
            {
                Program.D[i] = 1;
            }

            Console.WriteLine("Ввід MM");
            for (int i = 0; i < Program.N; i++)
            {
                for (int j = 0; j < Program.N; j++)
                {
                    Program.MM[i, j] = 1;
                }
            }

            Console.WriteLine("Ввід p");
            Program.p = 1;

            Program.Sem0_1.Release(3);

            Console.WriteLine("Т2 очікує ввід даних");
            Program.Sem0_2.WaitOne();
            Program.Sem0_3.WaitOne();

            Console.WriteLine("Т2 - початок обчислення 1");
            f_2 = Program.MultiplySubScalar(Program.B, Program.C, Program.H, Program.H * 2);
            Console.WriteLine("Т2 - початок обчислення 2");

            // КД1-2
            Interlocked.Add(ref Program.f, f_2);

            Console.WriteLine("Т2 - кінець обчислень 1,2");
            Program.Sem1_2.Release(3);

            Console.WriteLine("Т2 очікує завершення обчислень 1,2");
            Program.Sem1_1.WaitOne();
            Program.Sem1_3.WaitOne();
            Program.Sem1_4.WaitOne();

            // КД2-2
            Console.WriteLine("Т2 захоплює мютекс");
            Program.Mutex.WaitOne();
            for (int i = 0; i < Program.N; i++)
            {
                D_2[i] = Program.D[i];
            }
            Program.Mutex.ReleaseMutex();
            Console.WriteLine("Т2 звільнює мютекс");

            // КД3-2
            Console.WriteLine("Т2 - критична секція");
            lock (Program.Locker)
            {
                for (int i = 0; i < Program.N; i++)
                {
                    for (int j = 0; j < Program.N; j++)
                    {
                        ME_2[i, j] = Program.ME[i, j];
                    }
                }
            }

            Console.WriteLine("Т2 - початок обчислення 3");
            Program.G_H2 = Program.SortVector(
                Program.MultiplyMatrixAndVector(
                    Program.MultiplySubMatrix(ME_2, Program.MM, Program.H, Program.H * 2),
                    D_2));

            Console.WriteLine("Т2 - кінець обчислення 3");
            Program.Sem2_2.Release(1);

            Console.WriteLine("Т2 очікує завершення обчислень 3");
            Program.Sem2_1.WaitOne();
            Program.Sem2_3.WaitOne();
            Program.Sem2_4.WaitOne();

            Console.WriteLine("Т2 - початок обчислення 4");
            Program.G_2H2 = Program.SortVector(
                Program.ConcatVectors(
                    Program.G_H3,
                    Program.G_H4));

            Console.WriteLine("Т2 - кінець обчислення 4");
            Program.Sem3.Release(1);

            Console.WriteLine("Т2 очікує завершення обчислення 5");
            Program.Event.WaitOne();

            // КД4-2
            Interlocked.Exchange(ref p_2, Program.p);
            // КД5-2
            Interlocked.Exchange(ref f_2, Program.f);
            // КД6-2
            Interlocked.Exchange(ref x_2, Program.x);

            Console.WriteLine("Т2 - початок обчислення 6");
            Program.calculateSubResult(
                Program.G,
                Program.E,
                p_2, f_2, x_2,
                Program.H, Program.H * 2);

            Console.WriteLine("Т2 - кінець обчислення 6");
            Program.Sem4_2.Release(1);
        }
    }
}
