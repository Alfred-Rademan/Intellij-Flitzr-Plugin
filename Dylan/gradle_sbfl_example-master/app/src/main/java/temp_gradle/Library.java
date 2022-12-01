package temp_gradle;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class Library {
    public static double gRatio() {

        double epsilon = Math.pow(10, -15);

        int count = 1;
        int f_n = Helper.fib(count);
        count++;
        int f_np1 = Helper.fib(count);
        count++;

        double phi_n = 0;
        double phi_np1 = 1.0*f_np1/f_n;

        while (Math.abs(phi_np1 - phi_n) >= epsilon) {
            f_n = f_np1;
            f_np1 = Helper.fib(count);

            phi_n = phi_np1;
            phi_np1 = 1.0*f_np1/f_n;

            count++;
        }
        return phi_np1;
    }

    public static double x_Root10(double x) {
        /* Possible bug: change degree of root */
        return Helper.root(x, 10);
    }

    public static double simpOfX_Root10(int a, int b, int n) {
        double[] x = new double[n+1];
        /* Decent bug: Remove '1.0*' in next line */
        double deltaX = 1.0*(b-a)/n;
        for (int i = 0; i < n+1; i++) {
            if (i == 0) {
                x[0] = a;
            } else {
                x[i] = x[i-1] + deltaX;
            }
        }
        double S_n = 0;
        for (int i = 0; i < n+1; i++) {
            /* Decent bug is to mess with one of these conditions */
            if (i == 0) {
                S_n += x_Root10(x[i]);
            } else if (i == n) {
                S_n += x_Root10(x[i]);
            } else if (i % 2 == 1) {
                S_n += 4.0*x_Root10(x[i]);
            } else if (i % 2 == 0) {
                S_n += 2.0*x_Root10(x[i]);
            }
        }
        /* Decent bug: Comment out this line */
        S_n = S_n*deltaX/3;
        return S_n;
    }
}