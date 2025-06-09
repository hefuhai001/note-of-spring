package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 16:36
 */

import java.io.*;

public class _11_XuLieHua_test {
    public static void main(String[] args) {
        _10_XuLieHua e = null;
        try {
            FileInputStream fileIn = new FileInputStream("/tmp/employee.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (_10_XuLieHua) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized Employee...");
        System.out.println("Name: " + e.name);
        System.out.println("Address: " + e.address);
        System.out.println("SSN: " + e.SSN);
        System.out.println("Number: " + e.number);
    }
}


