

/*~~~~~~~~~~~~~~~~~~~~~~~~【enum(枚举)】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//enum DAY
//{
//      MON=1, TUE, WED, THU, FRI, SAT, SUN
//};
// 
//int main()
//{
//    enum DAY day;
//    day = WED;
//    printf("%d",day);
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【用 for 来遍历枚举的元素】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
// 
//enum DAY
//{
//      MON=1, TUE, WED, THU, FRI, SAT, SUN
//} day;
//int main()
//{
//    // 遍历枚举元素
//    for (day = MON; day <= SUN; day++) {
//        printf("枚举元素：%d \n", day);
//    }
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【枚举在 switch 中的使用：】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <stdlib.h>
//int main()
//{
//    enum color { red=1, green, blue };
//    enum  color favorite_color;
//    /* 用户输入数字来选择颜色 */
//    printf("请输入你喜欢的颜色: (1. red, 2. green, 3. blue): ");
//    scanf("%u", &favorite_color);
// 
//    /* 输出结果 */
//    switch (favorite_color)
//    {
//    case red:
//        printf("你喜欢的颜色是红色");
//        break;
//    case green:
//        printf("你喜欢的颜色是绿色");
//        break;
//    case blue:
//        printf("你喜欢的颜色是蓝色");
//        break;
//    default:
//        printf("你没有选择你喜欢的颜色");
//    }
// 
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【将整数转换为枚举】~~~~~~~~~~~~~~~~~~~~~~~~~*/
//#include <stdio.h>
//#include <stdlib.h>
// 
//int main()
//{
// 
//    enum day
//    {
//        saturday,
//        sunday,
//        monday,
//        tuesday,
//        wednesday,
//        thursday,
//        friday
//    } workday;
// 
//    int a = 1;
//    enum day weekend;
//    weekend = ( enum day ) a;  //类型转换
//    //weekend = a; //错误
//    printf("weekend:%d",weekend);
//    return 0;
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/


/*~~~~~~~~~~~~~~~~~~~~~~~~【】~~~~~~~~~~~~~~~~~~~~~~~~~*/



