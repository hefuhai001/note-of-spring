

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/

//运算符是一种告诉编译器执行特定的数学或逻辑操作的符号。C 语言内置了丰富的运算符，并提供了以下类型的运算符：
//
//算术运算符
//关系运算符
//逻辑运算符
//位运算符
//赋值运算符
//杂项运算符

/*~~~~~~~~~~~~~~~~~~~~~~~~【算术运算符：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 21;
//   int b = 10;
//   int c ;
// 
//   c = a + b;
//   printf("Line 1 - c 的值是 %d\n", c );
//   c = a - b;
//   printf("Line 2 - c 的值是 %d\n", c );
//   c = a * b;
//   printf("Line 3 - c 的值是 %d\n", c );
//   c = a / b;
//   printf("Line 4 - c 的值是 %d\n", c );
//   c = a % b;
//   printf("Line 5 - c 的值是 %d\n", c );
//   c = a++;  // 赋值后再加 1 ，c 为 21，a 为 22
//   printf("Line 6 - c 的值是 %d\n", c );
//   c = a--;  // 赋值后再减 1 ，c 为 22 ，a 为 21
//   printf("Line 7 - c 的值是 %d\n", c );
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【 a++ 与 ++a 的区别】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int c;
//   int a = 10;
//   c = a++; 
//   printf("先赋值后运算：\n");
//   printf("Line 1 - c 的值是 %d\n", c );
//   printf("Line 2 - a 的值是 %d\n", a );
//   a = 10;
//   c = a--; 
//   printf("Line 3 - c 的值是 %d\n", c );
//   printf("Line 4 - a 的值是 %d\n", a );
// 
//   printf("先运算后赋值：\n");
//   a = 10;
//   c = ++a; 
//   printf("Line 5 - c 的值是 %d\n", c );
//   printf("Line 6 - a 的值是 %d\n", a );
//   a = 10;
//   c = --a; 
//   printf("Line 7 - c 的值是 %d\n", c );
//   printf("Line 8 - a 的值是 %d\n", a );
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【所有可用的关系运算符：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 21;
//   int b = 10;
//   int c ;
// 
//   if( a == b )
//   {
//      printf("Line 1 - a 等于 b\n" );
//   }
//   else
//   {
//      printf("Line 1 - a 不等于 b\n" );
//   }
//   if ( a < b )
//   {
//      printf("Line 2 - a 小于 b\n" );
//   }
//   else
//   {
//      printf("Line 2 - a 不小于 b\n" );
//   }
//   if ( a > b )
//   {
//      printf("Line 3 - a 大于 b\n" );
//   }
//   else
//   {
//      printf("Line 3 - a 不大于 b\n" );
//   }
//   /* 改变 a 和 b 的值 */
//   a = 5;
//   b = 20;
//   if ( a <= b )
//   {
//      printf("Line 4 - a 小于或等于 b\n" );
//   }
//   if ( b >= a )
//   {
//      printf("Line 5 - b 大于或等于 a\n" );
//   }
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【逻辑运算符】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 5;
//   int b = 20;
//   int c ;
// 
//   if ( a && b )
//   {
//      printf("Line 1 - 条件为真\n" );
//   }
//   if ( a || b )
//   {
//      printf("Line 2 - 条件为真\n" );
//   }
//   /* 改变 a 和 b 的值 */
//   a = 0;
//   b = 10;
//   if ( a && b )
//   {
//      printf("Line 3 - 条件为真\n" );
//   }
//   else
//   {
//      printf("Line 3 - 条件为假\n" );
//   }
//   if ( !(a && b) )
//   {
//      printf("Line 4 - 条件为真\n" );
//   }
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【位运算符】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
// 
//   unsigned int a = 60;    /* 60 = 0011 1100 */  
//   unsigned int b = 13;    /* 13 = 0000 1101 */
//   int c = 0;           
// 
//   c = a & b;       /* 12 = 0000 1100 */ 
//   printf("Line 1 - c 的值是 %d\n", c );
// 
//   c = a | b;       /* 61 = 0011 1101 */
//   printf("Line 2 - c 的值是 %d\n", c );
// 
//   c = a ^ b;       /* 49 = 0011 0001 */
//   printf("Line 3 - c 的值是 %d\n", c );
// 
//   c = ~a;          /*-61 = 1100 0011 */
//   printf("Line 4 - c 的值是 %d\n", c );
// 
//   c = a << 2;     /* 240 = 1111 0000 */
//   printf("Line 5 - c 的值是 %d\n", c );
// 
//   c = a >> 2;     /* 15 = 0000 1111 */
//   printf("Line 6 - c 的值是 %d\n", c );
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【赋值运算符：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 21;
//   int c ;
// 
//   c =  a;
//   printf("Line 1 - =  运算符实例，c 的值 = %d\n", c );
// 
//   c +=  a;
//   printf("Line 2 - += 运算符实例，c 的值 = %d\n", c );
// 
//   c -=  a;
//   printf("Line 3 - -= 运算符实例，c 的值 = %d\n", c );
// 
//   c *=  a;
//   printf("Line 4 - *= 运算符实例，c 的值 = %d\n", c );
// 
//   c /=  a;
//   printf("Line 5 - /= 运算符实例，c 的值 = %d\n", c );
// 
//   c  = 200;
//   c %=  a;
//   printf("Line 6 - %%= 运算符实例，c 的值 = %d\n", c );
// 
//   c <<=  2;
//   printf("Line 7 - <<= 运算符实例，c 的值 = %d\n", c );
// 
//   c >>=  2;
//   printf("Line 8 - >>= 运算符实例，c 的值 = %d\n", c );
// 
//   c &=  2;
//   printf("Line 9 - &= 运算符实例，c 的值 = %d\n", c );
// 
//   c ^=  2;
//   printf("Line 10 - ^= 运算符实例，c 的值 = %d\n", c );
// 
//   c |=  2;
//   printf("Line 11 - |= 运算符实例，c 的值 = %d\n", c );
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【杂项运算符 ↦ sizeof & 三元】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 4;
//   short b;
//   double c;
//   int* ptr;
// 
//   /* sizeof 运算符实例 */
//   printf("Line 1 - 变量 a 的大小 = %lu\n", sizeof(a) );
//   printf("Line 2 - 变量 b 的大小 = %lu\n", sizeof(b) );
//   printf("Line 3 - 变量 c 的大小 = %lu\n", sizeof(c) );
// 
//   /* & 和 * 运算符实例 */
//   ptr = &a;    /* 'ptr' 现在包含 'a' 的地址 */
//   printf("a 的值是 %d\n", a);
//   printf("*ptr 是 %d\n", *ptr);
// 
//   /* 三元运算符实例 */
//   a = 10;
//   b = (a == 1) ? 20: 30;
//   printf( "b 的值是 %d\n", b );
// 
//   b = (a == 10) ? 20: 30;
//   printf( "b 的值是 %d\n", b );
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【C 中的运算符优先级】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main()
//{
//   int a = 20;
//   int b = 10;
//   int c = 15;
//   int d = 5;
//   int e;
// 
//   e = (a + b) * c / d;      // ( 30 * 15 ) / 5
//   printf("(a + b) * c / d 的值是 %d\n",  e );
// 
//   e = ((a + b) * c) / d;    // (30 * 15 ) / 5
//   printf("((a + b) * c) / d 的值是 %d\n" ,  e );
// 
//   e = (a + b) * (c / d);   // (30) * (15/5)
//   printf("(a + b) * (c / d) 的值是 %d\n",  e );
// 
//   e = a + (b * c) / d;     //  20 + (150/5)
//   printf("a + (b * c) / d 的值是 %d\n" ,  e );
//  
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


