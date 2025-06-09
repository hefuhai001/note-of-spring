package main

/*
Go 语言切片是对数组的抽象。
Go 数组的长度不可改变，在特定场景中这样的集合就不太适用，Go 中提供了一种灵活，功
能强悍的内置类型切片("动态数组")，与数组相比切片的长度是不固定的，可以追加元素，在追加时可能使切片的容量增大。
*/
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 len() 和 cap() 函数 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	var numbers = make([]int, 3, 5)
//
//	printSlice(numbers)
//}
//
//func printSlice(x []int) {
//	fmt.Printf("len=%d cap=%d slice=%v\n", len(x), cap(x), x)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 空(nil)切片 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	var numbers []int
//
//	printSlice(numbers)
//
//	if numbers == nil {
//		fmt.Printf("切片是空的")
//	}
//}
//
//func printSlice(x []int) {
//	fmt.Printf("len=%d cap=%d slice=%v\n", len(x), cap(x), x)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 切片截取 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	/* 创建切片 */
//	numbers := []int{0, 1, 2, 3, 4, 5, 6, 7, 8}
//	printSlice(numbers)
//
//	/* 打印原始切片 */
//	fmt.Println("numbers ==", numbers)
//
//	/* 打印子切片从索引1(包含) 到索引4(不包含)*/
//	fmt.Println("numbers[1:4] ==", numbers[1:4])
//
//	/* 默认下限为 0*/
//	fmt.Println("numbers[:3] ==", numbers[:3])
//
//	/* 默认上限为 len(s)*/
//	fmt.Println("numbers[4:] ==", numbers[4:])
//
//	numbers1 := make([]int, 0, 5)
//	printSlice(numbers1)
//
//	/* 打印子切片从索引  0(包含) 到索引 2(不包含) */
//	number2 := numbers[:2]
//	printSlice(number2)
//
//	/* 打印子切片从索引 2(包含) 到索引 5(不包含) */
//	number3 := numbers[2:5]
//	printSlice(number3)
//}
//
//func printSlice(x []int) {
//	fmt.Printf("len=%d cap=%d slice=%v\n", len(x), cap(x), x)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 append() 和 copy() 函数 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	var numbers []int
//	printSlice(numbers)
//
//	/* 允许追加空切片 */
//	numbers = append(numbers, 0)
//	printSlice(numbers)
//
//	/* 向切片添加一个元素 */
//	numbers = append(numbers, 1)
//	printSlice(numbers)
//
//	/* 同时添加多个元素 */
//	numbers = append(numbers, 2, 3, 4)
//	printSlice(numbers)
//
//	/* 创建切片 numbers1 是之前切片的两倍容量*/
//	numbers1 := make([]int, len(numbers), (cap(numbers))*2)
//
//	/* 拷贝 numbers 的内容到 numbers1 */
//	copy(numbers1, numbers)
//	printSlice(numbers1)
//}
//
//func printSlice(x []int) {
//	fmt.Printf("len=%d cap=%d slice=%v\n", len(x), cap(x), x)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
