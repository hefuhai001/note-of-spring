package main

/*~~~~~~~~~~~~~~~~~~~~~~~~~~语言变量~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//import "fmt"
//
//func main() {
//	var a string = "Runoob"
//	fmt.Println(a)
//
//	var b, c int = 1, 2
//	fmt.Println(b, c)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~变量声明~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//import "fmt"
//
//func main() {
//
//	// 声明一个变量并初始化
//	var a = "RUNOOB"
//	fmt.Println(a)
//
//	// 没有初始化就为零值
//	var b int
//	fmt.Println(b)
//
//	// bool 零值为 false
//	var c bool
//	fmt.Println(c)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
/*var a *int
var a []int
var a map[string] int
var a chan int
var a func(string) int
//var a error // error 是接口
数值类型（包括complex64/128）为 0
布尔类型为 false
字符串为 ""（空字符串）
以下几种类型为 nil：*/

//import "fmt"
//
//func main() {
//	var i int
//	var f float64
//	var b bool
//	var s string
//	fmt.Printf("%v %v %v %q\n", i, f, b, s)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~根据值自行判定变量类型。~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//import "fmt"
//
//func main() {
//	var d = true
//	fmt.Println(d)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~可以将 var f string = "Runoob" 简写为 f := "Runoob"：~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//import "fmt"
//
//func main() {
//	f := "Runoob" // var f string = "Runoob"
//
//	fmt.Println(f)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~多变量声明~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//import "fmt"
//
//var x, y int
//var ( // 这种因式分解关键字的写法一般用于声明全局变量
//	a int
//	b bool
//)
//
//var c, d int = 1, 2
//var e, f = 123, "hello"
//
////这种不带声明格式的只能在函数体中出现
////g, h := 123, "hello"
//
//func main() {
//	g, h := 123, "hello"
//	fmt.Println(x, y, a, b, c, d, e, f, g, h)
//}
