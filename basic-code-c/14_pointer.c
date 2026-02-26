
/*~~~~~~~~~~~~~~~~~~~~~~~~【输出定义的变量地址】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//    int var_runoob = 10;
//    int *p;              // 定义指针变量
//    p = &var_runoob;
// 
//   printf("var_runoob 变量的地址： %p\n", p);
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【使用一元运算符 * 来返回位于操作数所指定地址的变量的值】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   int  var = 20;   /* 实际变量的声明 */
//   int  *ip;        /* 指针变量的声明 */
// 
//   ip = &var;  /* 在指针变量中存储 var 的地址 */
// 
//   printf("var 变量的地址: %p\n", &var  );
// 
//   /* 在指针变量中存储的地址 */
//   printf("ip 变量存储的地址: %p\n", ip );
// 
//   /* 使用指针访问值 */
//   printf("*ip 变量的值: %d\n", *ip );
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【C 中的 NULL 指针】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   int  *ptr = NULL;
// 
//   printf("ptr 的地址是 %p\n", ptr  );
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【指针的算术运算】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 3;
// 
//int main ()
//{
//   int  var[] = {10, 100, 200};
//   int  i, *ptr;
// 
//   /* 指针中的数组地址 */
//   ptr = var;
//   for ( i = 0; i < MAX; i++)
//   {
//      printf("存储地址：var[%d] = %p\n", i, ptr );
//      printf("存储值：var[%d] = %d\n", i, *ptr );
//      /* 指向下一个位置 */
//      ptr++;
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【递减一个指针】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 3;
// 
//int main ()
//{
//   int  var[] = {10, 100, 200};
//   int  i, *ptr;
// 
//   /* 指针中最后一个元素的地址 */
//   ptr = &var[MAX-1];
//   for ( i = MAX; i > 0; i--)
//   {
// 
//      printf("存储地址：var[%d] = %p\n", i-1, ptr );
//      printf("存储值：var[%d] = %d\n", i-1, *ptr );
// 
//      /* 指向下一个位置 */
//      ptr--;
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【指针的比较】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 3;
// 
//int main ()
//{
//   int  var[] = {10, 100, 200};
//   int  i, *ptr;
// 
//   /* 指针中第一个元素的地址 */
//   ptr = var;
//   i = 0;
//   while ( ptr <= &var[MAX - 1] )
//   {
// 
//      printf("存储地址：var[%d] = %p\n", i, ptr );
//      printf("存储值：var[%d] = %d\n", i, *ptr );
// 
//      /* 指向上一个位置 */
//      ptr++;
//      i++;
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【指针数组】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 3;
// 
//int main ()
//{
//   int  var[] = {10, 100, 200};
//   int i;
// 
//   for (i = 0; i < MAX; i++)
//   {
//      printf("Value of var[%d] = %d\n", i, var[i] );
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【三个整数，它们将存储在一个指针数组中】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 3;
// 
//int main ()
//{
//   int  var[] = {10, 100, 200};
//   int i, *ptr[MAX];
// 
//   for ( i = 0; i < MAX; i++)
//   {
//      ptr[i] = &var[i]; /* 赋值为整数的地址 */
//   }
//   for ( i = 0; i < MAX; i++)
//   {
//      printf("Value of var[%d] = %d\n", i, *ptr[i] );
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【用一个指向字符的指针数组来存储一个字符串列表】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//const int MAX = 4;
// 
//int main ()
//{
//   const char *names[] = {
//                   "Zara Ali",
//                   "Hina Ali",
//                   "Nuha Ali",
//                   "Sara Ali",
//   };
//   int i = 0;
// 
//   for ( i = 0; i < MAX; i++)
//   {
//      printf("Value of names[%d] = %s\n", i, names[i] );
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【声明了一个包含三个整数指针的指针数组 ptrArray，然后，我们将这些指针分别指向不同的整数变量 num1、num2 和 num3，最后，我们使用指针数组访问这些整数变量的值】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main() {
//    int num1 = 10, num2 = 20, num3 = 30;
//    
//    // 声明一个整数指针数组，包含三个指针
//    int *ptrArray[3];
//    
//    // 将指针指向不同的整数变量
//    ptrArray[0] = &num1;
//    ptrArray[1] = &num2;
//    ptrArray[2] = &num3;
//    
//    // 使用指针数组访问这些整数变量的值
//    printf("Value at index 0: %d\n", *ptrArray[0]);
//    printf("Value at index 1: %d\n", *ptrArray[1]);
//    printf("Value at index 2: %d\n", *ptrArray[2]);
//    
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【指向指针的指针】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   int  V;
//   int  *Pt1;
//   int  **Pt2;
// 
//   V = 100;
// 
//   /* 获取 V 的地址 */
//   Pt1 = &V;
// 
//   /* 使用运算符 & 获取 Pt1 的地址 */
//   Pt2 = &Pt1;
// 
//   /* 使用 pptr 获取值 */
//   printf("var = %d\n", V );
//   printf("Pt1 = %p\n", Pt1 );
//   printf("*Pt1 = %d\n", *Pt1 );
//    printf("Pt2 = %p\n", Pt2 );
//   printf("**Pt2 = %d\n", **Pt2);
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【传递指针给函数】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <time.h>
// 
//void getSeconds(unsigned long *par);
//
//int main ()
//{
//   unsigned long sec;
//
//
//   getSeconds( &sec );
//
//   /* 输出实际值 */
//   printf("Number of seconds: %ld\n", sec );
//
//   return 0;
//}
//
//void getSeconds(unsigned long *par)
//{
//   /* 获取当前的秒数 */
//   *par = time( NULL );
//   return;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【能接受指针作为参数的函数，也能接受数组作为参数】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
///* 函数声明 */
//double getAverage(int *arr, int size);
// 
//int main ()
//{
//   /* 带有 5 个元素的整型数组  */
//   int balance[5] = {1000, 2, 3, 17, 50};
//   double avg;
// 
//   /* 传递一个指向数组的指针作为参数 */
//   avg = getAverage( balance, 5 ) ;
// 
//   /* 输出返回值  */
//   printf("Average value is: %f\n", avg );
//    
//   return 0;
//}
//
//double getAverage(int *arr, int size)
//{
//  int    i, sum = 0;       
//  double avg;          
// 
//  for (i = 0; i < size; ++i)
//  {
//    sum += arr[i];
//  }
// 
//  avg = (double)sum / size;
// 
//  return avg;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【从函数返回指针】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <time.h>
//#include <stdlib.h> 
// 
///* 要生成和返回随机数的函数 */
//int * getRandom( )
//{
//   static int  r[10];
//   int i;
// 
//   /* 设置种子 */
//   srand( (unsigned)time( NULL ) );
//   for ( i = 0; i < 10; ++i)
//   {
//      r[i] = rand();
//      printf("%d\n", r[i] );
//   }
// 
//   return r;
//}
// 
///* 要调用上面定义函数的主函数 */
//int main ()
//{
//   /* 一个指向整数的指针 */
//   int *p;
//   int i;
// 
//   p = getRandom();
//   for ( i = 0; i < 10; i++ )
//   {
//       printf("*(p + [%d]) : %d\n", i, *(p + i) );
//   }
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/



