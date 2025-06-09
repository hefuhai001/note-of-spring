package _06_Phone_Extends;

public class Phone_Test {
    public static void main(String[] args) {
        Phone p=new Phone();
        p.call("hfh");
        System.out.println("---------------");

        Phone_New np=new Phone_New();
        np.call("lm");
    }
}
