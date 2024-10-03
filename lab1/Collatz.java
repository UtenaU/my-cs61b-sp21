/** Class that prints the Collatz sequence starting from a given number.
 *  @author nadia
 * 	1.	选择一个正整数n。
 * 	2.	如果n是偶数，则将n除以2。
 * 	3.	如果n是奇数，则将n乘以3并加1。
 * 	4.	重复步骤2和3，直到n变为1。
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
    public static int nextNumber(int n) {
//        first, judge odd or even
        if (n % 2 == 1){
            return n * 3 + 1;
        }else{
            return n / 2;
        }

    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

