package main

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数调用 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	/* 定义局部变量 */
//	var a int = 100
//	var b int = 200
//	var ret int
//
//	/* 调用函数并返回最大值 */
//	ret = max(a, b)
//
//	fmt.Printf("最大值是 : %d\n", ret)
//}
//
///* 函数返回两个数的最大值 */
//func max(num1, num2 int) int {
//	/* 定义局部变量 */
//	var result int
//
//	if num1 > num2 {
//		result = num1
//	} else {
//		result = num2
//	}
//	return result
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数返回多个值 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func swap(x, y string) (string, string) {
//	return y, x
//}
//
//func main() {
//	a, b := swap("Google", "Runoob")
//	fmt.Println(a, b)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数值传递值 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	/* 定义局部变量 */
//	var a int = 100
//	var b int = 200
//
//	fmt.Printf("交换前 a 的值为 : %d\n", a)
//	fmt.Printf("交换前 b 的值为 : %d\n", b)
//
//	/* 通过调用函数来交换值 */
//	swap(a, b)
//
//	fmt.Printf("交换后 a 的值 : %d\n", a)
//	fmt.Printf("交换后 b 的值 : %d\n", b)
//}
//
///* 定义相互交换值的函数 */
//func swap(x, y int) int {
//	var temp int
//
//	temp = x /* 保存 x 的值 */
//	x = y    /* 将 y 值赋给 x */
//	y = temp /* 将 temp 值赋给 y*/
//
//	return temp
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数引用传递值 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	/* 定义局部变量 */
//	var a int = 100
//	var b int = 200
//
//	fmt.Printf("交换前，a 的值 : %d\n", a)
//	fmt.Printf("交换前，b 的值 : %d\n", b)
//
//	/* 调用 swap() 函数
//	 * &a 指向 a 指针，a 变量的地址
//	 * &b 指向 b 指针，b 变量的地址
//	 */
//	swap(&a, &b)
//
//	fmt.Printf("交换后，a 的值 : %d\n", a)
//	fmt.Printf("交换后，b 的值 : %d\n", b)
//}
//
//func swap(x *int, y *int) {
//	var temp int
//	temp = *x /* 保存 x 地址上的值 */
//	*x = *y   /* 将 y 值赋给 x */
//	*y = temp /* 将 temp 值赋给 y */
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数用法 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数作为实参 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	/* 声明函数变量 */
//	getSquareRoot := func(x float64) float64 {
//		return math.Sqrt(x)
//	}
//
//	/* 使用函数 */
//	fmt.Println(getSquareRoot(9))
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 函数闭包（匿名函数） 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func getSequence() func() int {
//	i := 0
//	return func() int {
//		i += 1
//		return i
//	}
//}
//
//func main() {
//	/* nextNumber 为一个函数，函数 i 为 0 */
//	nextNumber := getSequence()
//
//	/* 调用 nextNumber 函数，i 变量自增 1 并返回 */
//	fmt.Println(nextNumber())
//	fmt.Println(nextNumber())
//	fmt.Println(nextNumber())
//
//	/* 创建新的函数 nextNumber1，并查看结果 */
//	nextNumber1 := getSequence()
//	fmt.Println(nextNumber1())
//	fmt.Println(nextNumber1())
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 定义多个匿名函数，并展示了如何将匿名函数赋值给变量、在函数内部使用匿名函数以及将匿名函数作为参数传递给其他函数。 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	// 定义一个匿名函数并将其赋值给变量add
//	add := func(a, b int) int {
//		return a + b
//	}
//
//	// 调用匿名函数
//	result := add(3, 5)
//	fmt.Println("3 + 5 =", result)
//
//	// 在函数内部使用匿名函数
//	multiply := func(x, y int) int {
//		return x * y
//	}
//
//	product := multiply(4, 6)
//	fmt.Println("4 * 6 =", product)
//
//	// 将匿名函数作为参数传递给其他函数
//	calculate := func(operation func(int, int) int, x, y int) int {
//		return operation(x, y)
//	}
//
//	sum := calculate(add, 2, 8)
//	fmt.Println("2 + 8 =", sum)
//
//	// 也可以直接在函数调用中定义匿名函数
//	difference := calculate(func(a, b int) int {
//		return a - b
//	}, 10, 4)
//	fmt.Println("10 - 4 =", difference)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
