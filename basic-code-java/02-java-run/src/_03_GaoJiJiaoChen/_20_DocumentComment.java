package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 18:19
 */

import java.io.*;

/**
 * 这个类演示了文档注释
 *
 * @author Ayan Amhed
 * @version 1.2
 */
public class _20_DocumentComment {
    /**
     * This method returns the square of num.
     * This is a multiline description. You can use
     * as many lines as you like.
     *
     * @param num The value to be squared.
     * @return num squared.
     */
    public double square(double num) {
        return num * num;
    }

    /**
     * This method inputs a number from the user.
     *
     * @return The value input as a double.
     * @throws IOException On input error.
     * @see IOException
     */
    public double getNumber() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader inData = new BufferedReader(isr);
        String str;
        str = inData.readLine();
        return (new Double(str)).doubleValue();
    }

    /**
     * This method demonstrates square().
     *
     * @param args Unused.
     * @return Nothing.
     * @throws IOException On input error.
     * @see IOException
     */
    public static void main(String args[]) throws IOException {
        _20_DocumentComment ob = new _20_DocumentComment();
        double val;
        System.out.println("Enter value to be squared: ");
        val = ob.getNumber();
        val = ob.square(val);
        System.out.println("Squared value is " + val);
    }
}
