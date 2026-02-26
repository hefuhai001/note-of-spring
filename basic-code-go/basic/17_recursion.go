package main

/*
递归，就是在运行的过程中调用自己。语法格式如下：
func recursion() {
//函数调用自身
	recursion()
}
func main() {
	recursion()
}
Go 语言支持递归。但我们在使用递归时，开发者需要设置退出条件，否则递归将陷入无限循环中。
*/
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func Factorial(n uint64) (result uint64) {
//	if n > 0 {
//		result = n * Factorial(n-1)
//		return result
//	}
//	return 1
//}
//
//func main() {
//	var i int = 15
//	fmt.Printf("%d 的阶乘是 %d\n", i, Factorial(uint64(i)))
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 斐波那契数列 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func fibonacci(n int) int {
//	if n < 2 {
//		return n
//	}
//	return fibonacci(n-2) + fibonacci(n-1)
//}
//
//func main() {
//	var i int
//	for i = 0; i < 10; i++ {
//		fmt.Printf("%d\t", fibonacci(i))
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 求平方根 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func sqrtRecursive(x, guess, prevGuess, epsilon float64) float64 {
//	if diff := guess*guess - x; diff < epsilon && -diff < epsilon {
//		return guess
//	}
//
//	newGuess := (guess + x/guess) / 2
//	if newGuess == prevGuess {
//		return guess
//	}
//
//	return sqrtRecursive(x, newGuess, guess, epsilon)
//}
//
//func sqrt(x float64) float64 {
//	return sqrtRecursive(x, 1.0, 0.0, 1e-9)
//}
//
//func main() {
//	x := 25.0
//	result := sqrt(x)
//	fmt.Printf("%.2f 的平方根为 %.6f\n", x, result)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
