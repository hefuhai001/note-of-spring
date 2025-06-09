
/*~~~~~~~~~~~~~~~~~~~~~~~~【位域】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//C 语言的位域（bit-field）是一种特殊的结构体成员，允许我们按位对成员进行定义，指定其占用的位数。
//
//如果程序的结构中包含多个开关的变量，即变量值为 TRUE/FALSE，如下：
//
//struct
//{
//  unsigned int widthValidated;
//  unsigned int heightValidated;
//} status;
//这种结构需要 8 字节的内存空间，但在实际上，在每个变量中，我们只存储 0 或 1，在这种情况下，C 语言提供了一种更好的利用内存空间的方式。如果您在结构内使用这样的变量，您可以定义变量的宽度来告诉编译器，您将只使用这些字节。例如，上面的结构可以重写成：
//
//struct
//{
//  unsigned int widthValidated : 1;
//  unsigned int heightValidated : 1;
//} status;

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <string.h>
// 
///* 定义简单的结构 */
//struct
//{
//  unsigned int widthValidated;
//  unsigned int heightValidated;
//} status1;
// 
///* 定义位域结构 */
//struct
//{
//  unsigned int widthValidated : 1;
//  unsigned int heightValidated : 1;
//} status2;
// 
//int main( )
//{
//   printf( "Memory size occupied by status1 : %d\n", sizeof(status1));
//   printf( "Memory size occupied by status2 : %d\n", sizeof(status2));
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【位域的特点和使用方法如下】~~~~~~~~~~~~~~~~~~~~~~~~~*/

//定义位域时，可以指定成员的位域宽度，即成员所占用的位数。
//位域的宽度不能超过其数据类型的大小，因为位域必须适应所使用的整数类型。
//位域的数据类型可以是 int、unsigned int、signed int 等整数类型，也可以是枚举类型。
//位域可以单独使用，也可以与其他成员一起组成结构体。
//位域的访问是通过点运算符（.）来实现的，与普通的结构体成员访问方式相同。

/*~~~~~~~~~~~~~~~~~~~~~~~~【定义了一个名为 packed_struct 的结构体，其中包含了多个位域成员。】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//struct packed_struct {
//   unsigned int f1 : 1;   // 1位的位域
//   unsigned int f2 : 1;   // 1位的位域
//   unsigned int f3 : 1;   // 1位的位域
//   unsigned int f4 : 1;   // 1位的位域
//   unsigned int type : 4; // 4位的位域
//   unsigned int my_int : 9; // 9位的位域
//};
//
//int main() {
//   struct packed_struct pack;
//
//   pack.f1 = 1;
//   pack.f2 = 0;
//   pack.f3 = 1;
//   pack.f4 = 0;
//   pack.type = 7;
//   pack.my_int = 255;
//
//   printf("f1: %u\n", pack.f1);
//   printf("f2: %u\n", pack.f2);
//   printf("f3: %u\n", pack.f3);
//   printf("f4: %u\n", pack.f4);
//   printf("type: %u\n", pack.type);
//   printf("my_int: %u\n", pack.my_int);
//
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【代码被编译时，它会带有警告】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <string.h>
// 
//struct
//{
//  unsigned int age : 3;
//} Age;
// 
//int main( )
//{
//   Age.age = 4;
//   printf( "Sizeof( Age ) : %d\n", sizeof(Age) );
//   printf( "Age.age : %d\n", Age.age );
// 
//   Age.age = 7;
//   printf( "Age.age : %d\n", Age.age );
// 
//   Age.age = 8; // 二进制表示为 1000 有四位，超出
//   printf( "Age.age : %d\n", Age.age );
// 
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【计算字节数：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//
//struct example1 {
//   int a : 4;
//   int b : 5;
//   int c : 7;
//};
//
//int main() {
//   struct example1 ex1;
//
//   printf("Size of example1: %lu bytes\n", sizeof(ex1));
//
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【位域的使用】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//int main(){
//    struct bs{
//        unsigned a:1;
//        unsigned b:3;
//        unsigned c:4;
//    } bit,*pbit;
//    bit.a=1;    /* 给位域赋值（应注意赋值不能超过该位域的允许范围） */
//    bit.b=7;    /* 给位域赋值（应注意赋值不能超过该位域的允许范围） */
//    bit.c=15;    /* 给位域赋值（应注意赋值不能超过该位域的允许范围） */
//    printf("%d,%d,%d\n",bit.a,bit.b,bit.c);    /* 以整型量格式输出三个域的内容 */
//    pbit=&bit;    /* 把位域变量 bit 的地址送给指针变量 pbit */
//    pbit->a=0;    /* 用指针方式给位域 a 重新赋值，赋为 0 */
//    pbit->b&=3;    /* 使用了复合的位运算符 "&="，相当于：pbit->b=pbit->b&3，位域 b 中原有值为 7，与 3 作按位与运算的结果为 3（111&011=011，十进制值为 3） */
//    pbit->c|=1;    /* 使用了复合位运算符"|="，相当于：pbit->c=pbit->c|1，其结果为 15 */
//    printf("%d,%d,%d\n",pbit->a,pbit->b,pbit->c);    /* 用指针方式输出了这三个域的值 */
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/



