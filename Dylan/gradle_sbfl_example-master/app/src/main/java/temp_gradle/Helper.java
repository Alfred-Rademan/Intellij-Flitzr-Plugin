package temp_gradle;

public class Helper {

    
    public static int fib(int n) {
        /* Decent bug: Change one of these return values*/
        /* Decent bug: Change one of the conditions to a different (n>1: eg n==5) */
        /* Caution on second bug as it could lead to an infinite recursive call (But maybe it would be nice
         * to see how SBFL handles that :D
        */
        if (n == 1) {
            return 1;
        } else if (n == 0) {
            return 1;
        }
        return fib(n-1) + fib(n-2);
    }

    /* Function that computes an arbitrary root of a number
     * @param a: number of which root will be take
     * @param k: degree of root to be take
     * @return: a root k
     */
    public static double root(double a, double k) {
        /* Decent bug:
         * double epsilon = Math.pow(10, -9);
         */
	    double epsilon = Math.pow(10, -15);

        double x_n = a;
        double x_np1 = x_n - (Math.pow(x_n, k) - a)/(k * Math.pow(x_n, k-1));

        /* Decent bug:
         * while (Math.abs(x_np1 - x_n) < epsilon) {
         */
        while (Math.abs(x_np1 - x_n) >= epsilon) {
            x_n = x_np1;
            x_np1 = x_n - (Math.pow(x_n, k) - a)/(k * Math.pow(x_n, k-1));
        }
        return x_np1;
    }
}