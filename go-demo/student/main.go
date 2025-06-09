package main

import (
	"fmt"
	"os"
)

func showMenu() {
	info := `
**********欢迎来到学生管理系统***********
	1、添加学员信息
	2、编辑学员信息
	3、展示所有学员信息
	4、退出系统
*****************************************
`
	fmt.Print(info)
}

// 获取用户输入的信息，创造学员的函数
func newStuByUser() (*student, error) {
	var (
		id    int
		name  string
		class string
	)
	//读取用户输入
	_, err := fmt.Scanln(&id, &name, &class)
	//出错，给出错误信息，并且将错误返回
	if err != nil {
		fmt.Println("输入出错,err:", err)
	}
	return newStudent(id, name, class), err
}

func main() {
	//构造一个学生管理系统
	stuSys := newStudentSys() //指针类型的，只创建一次，因为只有一个系统

	for {
		//打印菜单
		showMenu()
		//接收用户输入数据
		var input int
		fmt.Scanf("%d\n", &input)
		//switch-case进行流程空指
		switch input {
		case 1: //添加学生
			fmt.Print("请输入学员的学号、名字、班级：(以空格隔开）")
			s1, err := newStuByUser()
			//调用学生管理系统所具备的方法，像其中添加学生
			//没有出错才添加
			if err == nil {
				stuSys.addStudent(s1)
			}
		case 2:
			fmt.Println("请输入该学生的学号，及其要修改的名称和班级：（用空格隔开）")
			s1, err := newStuByUser()
			if err == nil {
				stuSys.updateStudent(s1)
			}
		case 3:
			fmt.Println("以下是所有学生信息展示:")
			stuSys.showStudents()
		case 4:
			os.Exit(0) //退出系统
		default:
			fmt.Println("err:你输入选项格式有误，请重新输入！")
		}
	}
}
