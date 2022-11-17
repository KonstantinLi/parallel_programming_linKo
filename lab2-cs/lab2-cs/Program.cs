using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab2_cs
{
    internal class Program
    {
        public static int N = 8;
        public static int P = 4;
        public static int H = N / P;

        public static int f = 0;
        public static int p = 0;
        public static int x = 0;

        public static int[] A = new int[N];
        public static int[] B = new int[N];
        public static int[] C = new int[N];
        public static int[] D = new int[N];
        public static int[] E = new int[N];

        public static int[,] ME = new int[N, N];
        public static int[,] MM = new int[N, N];

        public static int[] G_H1 = new int[H];
        public static int[] G_H2 = new int[H];
        public static int[] G_H3 = new int[H];
        public static int[] G_H4 = new int[H];

        public static int[] G_2H1 = new int[H * 2];
        public static int[] G_2H2 = new int[H * 2];

        public static int[] G = new int[N];

        public static Semaphore Sem0_1 = new Semaphore(0, 3);
        public static Semaphore Sem0_2 = new Semaphore(0, 3);
        public static Semaphore Sem0_3 = new Semaphore(0, 3);

        public static Semaphore Sem1_1 = new Semaphore(0, 3);
        public static Semaphore Sem1_2 = new Semaphore(0, 3);
        public static Semaphore Sem1_3 = new Semaphore(0, 3);
        public static Semaphore Sem1_4 = new Semaphore(0, 3);

        public static Semaphore Sem2_1 = new Semaphore(0, 1);
        public static Semaphore Sem2_2 = new Semaphore(0, 1);
        public static Semaphore Sem2_3 = new Semaphore(0, 2);
        public static Semaphore Sem2_4 = new Semaphore(0, 2);

        public static Semaphore Sem3 = new Semaphore(0, 1);

        public static EventWaitHandle Event = new EventWaitHandle(false, EventResetMode.ManualReset);

        public static Semaphore Sem4_1 = new Semaphore(0, 1);
        public static Semaphore Sem4_2 = new Semaphore(0, 1);
        public static Semaphore Sem4_3 = new Semaphore(0, 1);

        public static Mutex Mutex = new Mutex();
        public static object Locker = new object();

        static void Main(string[] args)
        {
            Thread T1 = new Thread(new T1().Work);
            Thread T2 = new Thread(new T2().Work);
            Thread T3 = new Thread(new T3().Work);
            Thread T4 = new Thread(new T4().Work);

            T1.Start();
            T2.Start();
            T3.Start();
            T4.Start();

            T1.Join();
            T2.Join();
            T3.Join();
            T4.Join();
        }

        public static int MultiplySubScalar(int[] A, int[] B, int start, int end)
        {
            int result = 0;

            for (int i = start; i < end; i++)
            {
                result += A[i] * B[i];
            }

            return result;
        }



        public static int[,] MultiplySubMatrix(int[,] MA, int[,] MB, int start, int end)
        {
            int[,] MX = new int[N, end - start];

            for (int i = 0; i < N; i++)
            {
                int x = 0;
                for (int j = start; j < end; j++)
                {
                    MX[i, x] = 0;
                    for (int k = 0; k < N; k++)
                    {
                        MX[i, x] += MA[i, k] * MB[k, j];
                    }
                    x++;
                }
            }

            return MX;
        }

        public static int[] MultiplyMatrixAndVector(int[,] MA, int[] B)
        {
            int[] X = new int[MA.GetLength(1)];

            for (int i = 0; i < MA.GetLength(0); i++)
            {
                for (int j = 0; j < MA.GetLength(1); j++)
                {
                    X[j] += MA[i, j] * B[i];
                }
            }

            return X;
        }
        
        public static int[] SortVector(int[] A)
        {
            for (int i = 0; i < A.Length; i++)
            {
                for (int j = 0; j < A.Length - 1; j++)
                {
                    if (A[j] > A[j + 1])
                    {
                        Swap(ref A[j], ref A[j + 1]);
                    }
                }
            }

            return A;
        }

        public static void Swap(ref int a, ref int b)
        {
            int temp = a;
            a = b;
            b = temp;
        }

        public static int[] ConcatVectors(int[] A, int[] B)
        {
            int[] C = new int[A.Length + B.Length];
            int index = 0;

            for (int i = 0; i < A.Length; i++)
            {
                C[index++] = A[i];
            }

            for (int i = 0; i < B.Length; i++)
            {
                C[index++] = B[i];
            }

            return C;
        }

        public static int[] MultiplyVectorAndScalar(int[] A, int b)
        {
            int[] B = new int[A.Length];

            for (int i = 0; i < B.Length; i++)
            {
                B[i] = A[i] * b;
            }

            return B;
        }

        public static void calculateSubResult(int[] G, int[] E, int p, int f, int x, int start, int end) {
            writeToSubVector(A,
                sumVectors(MultiplyVectorAndScalar(G, p),
                            MultiplyVectorAndScalar(
                                MultiplyVectorAndScalar(E, f),
                                x)),
                start, end);
        }

        public static void writeToSubVector(int[] A, int[] B, int start, int end)
        {
            int index = 0;
            for (int i = start; i < end; i++)
            {
                A[i] = B[index++];
            }
        }

        public static int[] sumVectors(int[] A, int[] B)
        {
            int[] X = new int[A.Length];

            for (int i = 0; i < X.Length; i++)
            {
                X[i] = A[i] + B[i];
            }

            return X;
        }

        public static void PrintVector(int[] A)
        {
            for (int i = 0; i < A.Length; i++)
            {
                Console.Write(A[i] + " ");
            }
            Console.WriteLine();
        }
    }
}
