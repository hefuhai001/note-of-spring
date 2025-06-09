package _02_Java_JingJie;

public class _16_ArrayToString {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        //调用
        String s=arrayToString(arr);

        System.out.println("s:"+s);
    }

    //方法
    /*
     * 两个明确：
     *   返回值类型
     *   参数
     * */
    public static String arrayToString(int[] arr) {
        String s = "";

        s += "[";
        //遍历
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                s+=arr[i];

            }else {
                s+=arr[i];
                s+="， ";
            }
        }
        s += "]";
        return s;
    }
}
