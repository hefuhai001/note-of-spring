package _04_12_;

import java.util.Arrays;

public class _07_2_weeks_ {
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] a = {1, 2, 3, 4};
        _07_2_weeks_.swap(a, 0, 1);
        System.out.println(Arrays.toString(a));
        String[] s = {"1:", "dfg2", "3", "4sdf"};
        _07_2_weeks_.swap(s, 0, 1);
        System.out.println(Arrays.toString(s));
    }
}
