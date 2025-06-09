
/*~~~~~~~~~~~~~~~~~~~~~~~~【预处理器】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//指令	描述
//#define	定义宏
//#include	包含一个源代码文件
//#undef	取消已定义的宏
//#ifdef	如果宏已经定义，则返回真
//#ifndef	如果宏没有定义，则返回真
//#if	如果给定条件为真，则编译下面代码
//#else	#if 的替代方案
//#elif	如果前面的 #if 给定条件不为真，当前条件为真，则编译下面代码
//#endif	结束一个 #if……#else 条件编译块
//#error	当遇到标准错误时，输出错误消息
//#pragma	使用标准化方法，向编译器发布特殊的命令到编译器中

/*~~~~~~~~~~~~~~~~~~~~~~~~【预处理器实例】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//
//#define MAX_ARRAY_LENGTH 20
//这个指令告诉 CPP 把所有的 MAX_ARRAY_LENGTH 定义为 20。使用 #define 定义常量来增强可读性。
//
//#include <stdio.h>
//#include "myheader.h"
//这些指令告诉 CPP 从系统库中获取 stdio.h，并添加文本到当前的源文件中。下一行告诉 CPP 从本地目录中获取 myheader.h，并添加内容到当前的源文件中。
//
//#undef  FILE_SIZE
//#define FILE_SIZE 42
//这个指令告诉 CPP 取消已定义的 FILE_SIZE，并定义它为 42。
//
//#ifndef MESSAGE
//   #define MESSAGE "You wish!"
//#endif
//这个指令告诉 CPP 只有当 MESSAGE 未定义时，才定义 MESSAGE。
//
//#ifdef DEBUG
//   /* Your debugging statements here */
//#endif分析下面的实例来理解不同的指令。


/*~~~~~~~~~~~~~~~~~~~~~~~~【预定义宏】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//宏	描述
//__DATE__	当前日期，一个以 "MMM DD YYYY" 格式表示的字符常量。
//__TIME__	当前时间，一个以 "HH:MM:SS" 格式表示的字符常量。
//__FILE__	这会包含当前文件名，一个字符串常量。
//__LINE__	这会包含当前行号，一个十进制常量。
//__STDC__	当编译器以 ANSI 标准编译时，则定义为 1。

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//main()
//{
//   printf("File :%s\n", __FILE__ );
//   printf("Date :%s\n", __DATE__ );
//   printf("Time :%s\n", __TIME__ );
//   printf("Line :%d\n", __LINE__ );
//   printf("ANSI :%d\n", __STDC__ );
// 
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【预处理器运算符】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//#define  message_for(a, b)  \
//    printf(#a " and " #b ": We love you!\n")
// 
//int main(void)
//{
//   message_for(Carole, Debra);
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【标记粘贴运算符（##）】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//#define tokenpaster(n) printf ("token" #n " = %d", token##n)
// 
//int main(void)
//{
//   int token34 = 40;
//   
//   tokenpaster(34);
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【预处理器 defined 运算符是用在常量表达式中的，用来确定一个标识符是否已经使用 #define 定义过。如果指定的标识符已定义，则值为真（非零）。如果指定的标识符未定义，则值为假（零）。】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//#if !defined (MESSAGE)
//   #define MESSAGE "You wish!"
//#endif
// 
//int main(void)
//{
//   printf("Here is the message: %s\n", MESSAGE);  
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【参数化的宏】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//#define MAX(x,y) ((x) > (y) ? (x) : (y))
// 
//int main(void)
//{
//   printf("Max between 20 and 10 is %d\n", MAX(10, 20));  
//   return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/



