package _04_12_;

/*
 *
 *
 * */
public class _06_class_ {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static void main(String[] args) {
        _06_class_ user = new _06_class_();
        user.setName("xiaohe");
        System.out.println(user.getName());
        user.setAge(20);
        System.out.println(user.getAge());
    }
}
