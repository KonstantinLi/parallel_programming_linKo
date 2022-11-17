using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab2_cs
{
    internal class T1
    {
        int f_1;
        int p_1;
        int x_1;
        int[] D_1 = new int[Program.N];
        int[,] ME_1 = new int[Program.N, Program.N];

        public void Work()
        {
            Console.WriteLine("Запуск Т1");
            Console.WriteLine("Т1 очікує ввід даних");
            Program.Sem0_1.WaitOne();
            Program.Sem0_2.WaitOne();
            Program.Sem0_3.WaitOne();

            Console.WriteLine("Т1 - обчислення 1");
            f_1 = Program.MultiplySubScalar(Program.B, Program.C, 0, Program.H);

            // КД1-1
            Console.WriteLine("Т1 - обчислення 2");
            Interlocked.Add(ref Program.f, f_1);

            Console.WriteLine("Т1 - кінець обчислень 1,2");
            Program.Sem1_1.Release(3);

            Console.WriteLine("Т1 очікує завершення обчислень 1");
            Program.Sem1_2.WaitOne();
            Program.Sem1_3.WaitOne();
            Program.Sem1_4.WaitOne();

            // КД2-1
            Console.WriteLine("Т1 захоплює мютекс");
            Program.Mutex.WaitOne();
            for (int i = 0; i < Program.N; i++)
            {
                D_1[i] = Program.D[i];
            }
            Program.Mutex.ReleaseMutex();
            Console.WriteLine("Т1 звільненює мютекс");

            // КД3-1
            Console.WriteLine("Т1 - критична секція");
            lock (Program.Locker)
            {
                for (int i = 0; i < Program.N; i++)
                {
                    for (int j = 0; j < Program.N; j++)
                    {
                        ME_1[i, j] = Program.ME[i, j];
                    }
                }
            }


            Console.WriteLine("Т1 - початок обчислення 3");
            Program.G_H1 = Program.SortVector(
                Program.MultiplyMatrixAndVector(
                    Program.MultiplySubMatrix(ME_1, Program.MM, 0, Program.H), 
                    D_1));

            Console.WriteLine("Т1 - кінець обчислення 3");
            Program.Sem2_1.Release(1);

            Console.WriteLine("Т1 очікує завершення обчислень 3");
            Program.Sem2_2.WaitOne();
            Program.Sem2_3.WaitOne();
            Program.Sem2_4.WaitOne();

            Console.WriteLine("Т1 - обчислення 4");
            Program.G_2H1 = Program.SortVector(
                Program.ConcatVectors(
                    Program.G_H1, 
                    Program.G_H2));

            Console.WriteLine("Т1 очікує завершення обчислення 4 в Т2");
            Program.Sem3.WaitOne();

            Console.WriteLine("Т1 - початок обчислення 5");
            Program.G = Program.SortVector(
                Program.ConcatVectors(
                    Program.G_2H1, 
                    Program.G_2H2));

            Console.WriteLine("Т1 - кінець обчислення 5");
            Program.Event.Set();

            // КД4-1
            Interlocked.Exchange(ref p_1, Program.p);
            // КД5-1
            Interlocked.Exchange(ref f_1, Program.f);
            // КД6-1
            Interlocked.Exchange(ref x_1, Program.x);

            Console.WriteLine("Т1 - початок обчислення 6");
            Program.calculateSubResult(
                Program.G,
                Program.E,
                p_1, f_1, x_1,
                0, Program.H);

            Console.WriteLine("Т1 - кінець обчислення 6");
            Program.Sem4_1.Release(1);
        }
    }
}
