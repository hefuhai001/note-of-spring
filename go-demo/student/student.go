package main

// 定义一个学生类型结构体
type student struct {
	id    int    //学号
	name  string //姓名
	class string
}

// 构造函数
func newStudent(id int, name, class string) *student {
	return &student{
		id:    id,
		name:  name,
		class: class,
	}
}
