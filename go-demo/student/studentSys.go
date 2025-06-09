package main

import "fmt"

type studentSys struct {
	num      int        //学生的数量
	students []*student //存储学生实体的切片
}

// 构造函数
func newStudentSys() *studentSys {
	return &studentSys{
		num:      0,
		students: make([]*student, 0, 100),
	}
}

// 1）这个管理系统所具备的方法：添加学生
func (sys *studentSys) addStudent(stu *student) {
	sys.students = append(sys.students, stu)
	sys.num += 1
	fmt.Println("************添加成功**********")
}

// 2）编辑学生
func (sys *studentSys) updateStudent(stu *student) {
	for i, v := range sys.students {
		if stu.id == v.id { //当学号相同时，找到要修改学生
			sys.students[i] = stu
			fmt.Println("***************修改成功***************")
			return
		}
	}
	fmt.Println("*********找不到该学号学生，修改失败*********")

}

// 3)展示学生
func (sys *studentSys) showStudents() {
	for _, v := range sys.students {
		fmt.Println("学号:", v.id, "姓名:", v.name, "班级", v.class)
	}
}
