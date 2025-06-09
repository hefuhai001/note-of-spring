package _02_排序算法;

/**
 * 01插入排序
 */
public class _01_InsertionSort {

    public static void sort(Comparable[] arr){

        int n = arr.length;
        for (int i = 0; i < n; i++) {
            // 寻找元素arr[i]合适的插入位置
           for( int j = i ; j > 0 ; j -- )
                if( arr[j].compareTo( arr[j-1] ) < 0 )
                    swap( arr, j , j-1 );
                else
                    break;
        }
    }
    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {

        int N = 20000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100000);
        _01_InsertionSort.sort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
    }

}