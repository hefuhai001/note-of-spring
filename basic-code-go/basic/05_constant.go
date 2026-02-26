package main

/*~~~~~~~~~~~~~~~~~~~~~~~~~~语言常量~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "fmt"
//
//func main() {
//	const LENGTH int = 10
//	const WIDTH int = 5
//	var area int
//	const a, b, c = 1, false, "str" //多重赋值
//	area = LENGTH * WIDTH
//	fmt.Printf("面积为 : %d", area)
//	println()
//	println(a, b, c)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~枚举~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "unsafe"
//const (
//	a = "abc"
//	b = len(a)
//	c = unsafe.Sizeof(a)
//)
//
//func main(){
//	println(a, b, c)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~iota 用法~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "fmt"
//
//func main() {
//	const (
//		a = iota //0
//		b        //1
//		c        //2
//		d = "ha" //独立值，iota += 1
//		e        //"ha"   iota += 1
//		f = 100  //iota +=1
//		g        //100  iota +=1
//		h = iota //7,恢复计数
//		i        //8
//	)
//	fmt.Println(a, b, c, d, e, f, g, h, i)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~有趣的的 iota 实例：~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
//import "fmt"
//
//const (
//	i = 1 << iota
//	j = 3 << iota
//	k
//	l
//)
//
//func main() {
//	fmt.Println("i=", i)
//	fmt.Println("j=", j)
//	fmt.Println("k=", k)
//	fmt.Println("l=", l)
//}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`*/
