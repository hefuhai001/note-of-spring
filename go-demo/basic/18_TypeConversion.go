package main

/*
类型转换用于将一种数据类型的变量转换为另外一种类型的变量。Go 语言类型转换基本格式如下：
type_name(expression)
type_name 为类型，expression 为表达式。
*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 类型转换 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import (
//	"fmt"
//	"strconv"
//)
//
//func main() {
//	var sum int = 17
//	var count int = 5
//	var mean float32
//
//	mean = float32(sum) / float32(count)
//	fmt.Printf("mean 的值为: %f\n", mean)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 将字符串转换为整数 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import (
//	"fmt"
//	"strconv"
//)
//
//func main() {
//	str := "123"
//	num, err := strconv.Atoi(str)
//	if err != nil {
//		fmt.Println("转换错误:", err)
//	} else {
//		fmt.Printf("字符串 '%s' 转换为整数为：%d\n", str, num)
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 将整数转换为字符串： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	num := 123
//	str := strconv.Itoa(num)
//	fmt.Printf("整数 %d  转换为字符串为：'%s'\n", num, str)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 将字符串转换为浮点数： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	str := "3.14"
//	num, err := strconv.ParseFloat(str, 64)
//	if err != nil {
//		fmt.Println("转换错误:", err)
//	} else {
//		fmt.Printf("字符串 '%s' 转为浮点型为：%f\n", str, num)
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 将浮点数转换为字符串： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	num := 3.14
//	str := strconv.FormatFloat(num, 'f', 2, 64)
//	fmt.Printf("浮点数 %f 转为字符串为：'%s'\n", num, str)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 接口类型转换  接口类型转换有两种情况：类型断言和类型转换。 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*
接口类型转换
接口类型转换有两种情况：类型断言和类型转换。类型断言用于将接口类型转换为指定类型，其语法为：
value.(type)
或者
value.(T)
其中 value 是接口类型的变量，type 或 T 是要转换成的类型。
*/

//import "fmt"
//
//func main() {
//	var i interface{} = "Hello, World"
//	str, ok := i.(string)
//	if ok {
//		fmt.Printf("'%s' is a string\n", str)
//	} else {
//		fmt.Println("conversion failed")
//	}
//}

/*
定义了一个接口类型变量 i，并将它赋值为字符串 "Hello, World"。
然后，我们使用类型断言将 i 转换为字符串类型，并将转换后的值赋值给变量 str。
最后，我们使用 ok 变量检查类型转换是否成功，如果成功，我们打印转换后的字符串；否则，我们打印转换失败的消息。
*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//type Writer interface {
//	Write([]byte) (int, error)
//}
//
//type StringWriter struct {
//	str string
//}
//
//func (sw *StringWriter) Write(data []byte) (int, error) {
//	sw.str += string(data)
//	return len(data), nil
//}
//
//func main() {
//	var w Writer = &StringWriter{}
//	sw := w.(*StringWriter)
//	sw.str = "Hello, World"
//	fmt.Println(sw.str)
//}

/*
定义了一个 Writer 接口和一个实现了该接口的结构体 StringWriter。
然后，我们将 StringWriter 类型的指针赋值给 Writer 接口类型的变量 w。
接着，我们使用类型转换将 w 转换为 StringWriter 类型，并将转换后的值赋值给变量 sw。
最后，我们使用 sw 访问 StringWriter 结构体中的字段 str，并打印出它的值。
*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
