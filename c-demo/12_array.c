
/*~~~~~~~~~~~~~~~~~~~~~~~~【访问数组元素】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   int n[ 10 ]; /* n 是一个包含 10 个整数的数组 */
//   int i,j;
// 
//   /* 初始化数组元素 */         
//   for ( i = 0; i < 10; i++ )
//   {
//      n[ i ] = i + 100; /* 设置元素 i 为 i + 100 */
//   }
//   
//   /* 输出数组中每个元素的值 */
//   for (j = 0; j < 10; j++ )
//   {
//      printf("Element[%d] = %d\n", j, n[j] );
//   }
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【获取数组长度】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//int main() {
//    int array[] = {1, 2, 3, 4, 5};
//    int length = sizeof(array) / sizeof(array[0]);
//
//    printf("数组长度为: %d\n", length);
//
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【使用宏定义：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//#define LENGTH(array) (sizeof(array) / sizeof(array[0]))
//
//int main() {
//    int array[] = {1, 2, 3, 4, 5};
//    int length = LENGTH(array);
//
//    printf("数组长度为: %d\n", length);
//
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【数组名arr被当作指针使用】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//void printArray(int arr[], int size) {
//    for (int i = 0; i < size; i++) {
//        printf("%d ", arr[i]); // 数组名arr被当作指针使用
//    }
//}
//
//int main() {
//    int myArray[5] = {10, 20, 30, 40, 50};
//    printArray(myArray, 5); // 将数组名传递给函数
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【多维数组】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   /* 一个带有 5 行 2 列的数组 */
//   int a[5][2] = { {0,0}, {1,2}, {2,4}, {3,6},{4,8}};
//   int i, j;
// 
//   /* 输出数组中每个元素的值 */
//   for ( i = 0; i < 5; i++ )
//   {
//      for ( j = 0; j < 2; j++ )
//      {
//         printf("a[%d][%d] = %d\n", i,j, a[i][j] );
//      }
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【传递数组给函数】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
///* 函数声明 */
//double getAverage(int arr[], int size);
// 
//int main ()
//{
//   /* 带有 5 个元素的整型数组 */
//   int balance[5] = {1000, 2, 3, 17, 50};
//   double avg;
//   /* 传递一个指向数组的指针作为参数 */
//   avg = getAverage( balance, 5 ) ;
//   /* 输出返回值 */
//   printf( "平均值是： %f ", avg );
//   return 0;
//}
// 
//double getAverage(int arr[], int size)
//{
//  int    i;
//  double avg;
//  double sum=0;
//  for (i = 0; i < size; ++i)
//  {
//    sum += arr[i];
//  }
//  avg = sum / size;
//  return avg;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【从函数返回数组】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <stdlib.h>
//#include <time.h>
// 
///* 要生成和返回随机数的函数 */
//int * getRandom( )
//{
//  static int  r[10];
//  int i;
//  /* 设置种子 */
//  srand( (unsigned)time( NULL ) );
//  for ( i = 0; i < 10; ++i)
//  {
//     r[i] = rand();
//     printf( "r[%d] = %d\n", i, r[i]);
//  }
//  return r;
//}
// 
///* 要调用上面定义函数的主函数 */
//int main ()
//{
//   /* 一个指向整数的指针 */
//   int *p;
//   int i;
//   p = getRandom();
//   for ( i = 0; i < 10; i++ )
//   {
//       printf( "*(p + %d) : %d\n", i, *(p + i));
//   }
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【指向数组的指针】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main ()
//{
//   /* 带有 5 个元素的整型数组 */
//   double balance[5] = {1000.0, 2.0, 3.4, 17.0, 50.0};
//   double *p;
//   int i;
// 
//   p = balance;
// 
//   /* 输出数组中每个元素的值 */
//   printf( "使用指针的数组值\n");
//   for ( i = 0; i < 5; i++ )
//   {
//       printf("*(p + %d) : %f\n",  i, *(p + i) );
//   }
// 
//   printf( "使用 balance 作为地址的数组值\n");
//   for ( i = 0; i < 5; i++ )
//   {
//       printf("*(balance + %d) : %f\n",  i, *(balance + i) );
//   }
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【语言静态数组与动态数组】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//int main() {
//    int staticArray[] = {1, 2, 3, 4, 5}; // 静态数组声明并初始化
//    int length = sizeof(staticArray) / sizeof(staticArray[0]);
//
//    printf("静态数组: ");
//    for (int i = 0; i < length; i++) {
//        printf("%d ", staticArray[i]);
//    }
//    printf("\n");
//
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【动态数组使用实例】~~~~~~~~~~~~~~~~~~~~~~~~~*/

//#include <stdio.h>
//#include <stdlib.h>
//
//int main() {
//    int size = 5;
//    int *dynamicArray = (int *)malloc(size * sizeof(int)); // 动态数组内存分配
//
//    if (dynamicArray == NULL) {
//        printf("Memory allocation failed.\n");
//        return 1;
//    }
//
//    printf("Enter %d elements: ", size);
//    for (int i = 0; i < size; i++) {
//        scanf("%d", &dynamicArray[i]);
//    }
//
//    printf("Dynamic Array: ");
//    for (int i = 0; i < size; i++) {
//        printf("%d ", dynamicArray[i]);
//    }
//    printf("\n");
//
//    free(dynamicArray); // 动态数组内存释放
//
//    return 0;
//}







