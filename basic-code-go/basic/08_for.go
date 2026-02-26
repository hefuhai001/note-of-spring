package main

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 计算 1 到 10 的数字之和： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	sum := 0
//	for i := 0; i <= 10; i++ {
//		sum += i
//	}
//	fmt.Println(sum)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ sum 小于 10 的时候计算 sum 自相加后的值： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	sum := 1
//	for sum <= 10 {
//		sum += sum
//	}
//	fmt.Println(sum)
//
//	// 这样写也可以，更像 While 语句形式
//	for sum <= 10 {
//		sum += sum
//	}
//	fmt.Println(sum)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 无限循环: ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	sum := 0
//	for {
//		sum++ // 无限循环下去
//	}
//	fmt.Println(sum) // 无法输出
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ For-each range 循环 ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "fmt"
//
//func main() {
//	strings := []string{"google", "runoob"}
//	for i, s := range strings {
//		fmt.Println(i, s)
//	}
//
//	numbers := [6]int{1, 2, 3, 5}
//	for i, x := range numbers {
//		fmt.Printf("第 %d 位 x 的值 = %d\n", i, x)
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ for 循环的 range 格式可以省略 key 和 value ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "fmt"
//
//func main() {
//	map1 := make(map[int]float32)
//	map1[1] = 1.0
//	map1[2] = 2.0
//	map1[3] = 3.0
//	map1[4] = 4.0
//
//	// 读取 key 和 value
//	for key, value := range map1 {
//		fmt.Printf("key is: %d - value is: %f\n", key, value)
//	}
//
//	// 读取 key
//	for key := range map1 {
//		fmt.Printf("key is: %d\n", key)
//	}
//
//	// 读取 value
//	for _, value := range map1 {
//		fmt.Printf("value is: %f\n", value)
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 循环嵌套 ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	/* 定义局部变量 */
//	var i, j int
//
//	for i = 2; i < 100; i++ {
//		for j = 2; j <= (i / j); j++ {
//			if i%j == 0 {
//				break // 如果发现因子，则不是素数
//			}
//		}
//		if j > (i / j) {
//			fmt.Printf("%d  是素数\n", i)
//		}
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 在 for 循环中使用 break： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//	for i := 0; i < 10; i++ {
//		if i == 5 {
//			break // 当 i 等于 5 时跳出循环
//		}
//		fmt.Println(i)
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 使用标记和不使用标记的区别： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//import "fmt"
//
//func main() {
//
//	// 不使用标记
//	fmt.Println("---- break ----")
//	for i := 1; i <= 3; i++ {
//		fmt.Printf("i: %d\n", i)
//		for i2 := 11; i2 <= 13; i2++ {
//			fmt.Printf("i2: %d\n", i2)
//			break
//		}
//	}
//
//	// 使用标记
//	fmt.Println("---- break label ----")
//re:
//	for i := 1; i <= 3; i++ {
//		fmt.Printf("i: %d\n", i)
//		for i2 := 11; i2 <= 13; i2++ {
//			fmt.Printf("i2: %d\n", i2)
//			break re
//		}
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 在 select 语句中使用 break： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	ch1 := make(chan int)
//	ch2 := make(chan int)
//
//	go func() {
//		time.Sleep(2 * time.Second)
//		ch1 <- 1
//	}()
//
//	go func() {
//		time.Sleep(1 * time.Second)
//		ch2 <- 2
//	}()
//
//	select {
//	case <-ch1:
//		fmt.Println("Received from ch1.")
//	case <-ch2:
//		fmt.Println("Received from ch2.")
//		break // 跳出 select 语句
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~[ 在 select 语句中使用 return 来提前结束执行的情况： ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func process(ch chan int) {
//	for {
//		select {
//		case val := <-ch:
//			fmt.Println("Received value:", val)
//			// 执行一些逻辑
//			if val == 5 {
//				return // 提前结束 select 语句的执行
//			}
//		default:
//			fmt.Println("No value received yet.")
//			time.Sleep(500 * time.Millisecond)
//		}
//	}
//}
//
//func main() {
//	ch := make(chan int)
//
//	go process(ch)
//
//	time.Sleep(2 * time.Second)
//	ch <- 1
//	time.Sleep(1 * time.Second)
//	ch <- 3
//	time.Sleep(1 * time.Second)
//	ch <- 5
//	time.Sleep(1 * time.Second)
//	ch <- 7
//
//	time.Sleep(2 * time.Second)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 在变量 a 等于 15 的时候跳过本次循环执行下一次循环： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

//func main() {
//	/* 定义局部变量 */
//	var a int = 10
//
//	/* for 循环 */
//	for a < 20 {
//		if a == 15 {
//			/* 跳过此次循环 */
//			a = a + 1
//			continue
//		}
//		fmt.Printf("a 的值为 : %d\n", a)
//		a++
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 以下实例有多重循环，演示了使用标记和不使用标记的区别： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//
//	// 不使用标记
//	fmt.Println("---- continue ---- ")
//	for i := 1; i <= 3; i++ {
//		fmt.Printf("i: %d\n", i)
//		for i2 := 11; i2 <= 13; i2++ {
//			fmt.Printf("i2: %d\n", i2)
//			continue
//		}
//	}
//
//	// 使用标记
//	fmt.Println("---- continue label ----")
//re:
//	for i := 1; i <= 3; i++ {
//		fmt.Printf("i: %d\n", i)
//		for i2 := 11; i2 <= 13; i2++ {
//			fmt.Printf("i2: %d\n", i2)
//			continue re
//		}
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 goto 语句 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【 在变量 a 等于 15 的时候跳过本次循环并回到循环的开始语句 LOOP 处： 】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

//func main() {
//	/* 定义局部变量 */
//	var a int = 10
//
//	/* 循环 */
//LOOP:
//	for a < 20 {
//		if a == 15 {
//			/* 跳过迭代 */
//			a = a + 1
//			goto LOOP
//		}
//		fmt.Printf("a的值为 : %d\n", a)
//		a++
//	}
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~【  】~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
