package matsu.num.specialfunction.subpj.icgamma;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Temme による漸近展開法の係数を計算する.
 * 
 * <p>
 * N. M. Temme, The asymptotic expansions of the incomplete gamma functions,
 * SIAM
 * J. Math. Anal. 10 (1979) 757-766.
 * </p>
 * 
 * <p>
 * Temme 法: <br>
 * &lambda; = x/a, &mu; = &lambda;-1, &eta; = sgn(&mu;)
 * &radic;[2(&mu;-log(1+&mu;))]
 * として, <br>
 * P(a,x) = (1/2)erfc(-&eta;&radic;(a/2)) - R(a,&eta;) <br>
 * Q(a,x) = (1/2)erfc(&eta;&radic;(a/2)) + R(a,&eta;) <br>
 * とし, <br>
 * R(a,&eta;) = (1/&radic;(2&pi;a)) exp[-(1/2)a&eta;<sup>2</sup>]
 * &times;
 * &Sigma;<sub>k&ge;0</sub> &Sigma;<sub>n&ge;0</sub>
 * c<sub>n</sub><sup>k</sup> &eta;<sup>n</sup> a<sup>-k</sup> <br>
 * としたときの,
 * c<sub>n</sub><sup>k</sup> を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class TemmeCoefficientRepository {

    public static void main(String[] args) {
        int nMax = 7;
        int kMax = 4;

        TemmeCoefficientRepository repository = new TemmeCoefficientRepository(nMax, kMax);

        // 値の表示
        System.out.println("n\tk\tC");
        for (int n = 0; n <= nMax; n++) {
            for (int k = 0; k <= kMax; k++) {
                System.out.println(n + "\t" + k + "\t" + repository.getCnk(n, k));
            }
        }
        System.out.println();

        // プログラムで使うための表示
        // c_nk[n][k] で扱えるようにする.
        System.out.println("double[][] c_nk = {");
        for (int n = 0; n <= nMax; n++) {
            System.out.print("{");
            for (int k = 0; k <= kMax; k++) {
                System.out.print(repository.getCnk(n, k));
                if (k < kMax) {
                    System.out.print(",");
                }
            }
            System.out.print("}");
            if (n < nMax) {
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println("};");
        System.out.println();
    }

    private final int nMax;
    private final int kMax;

    /**
     * alpha_1,...,alpha_(nMax+2kMax+2)
     */
    private final double[] alpha;

    /**
     * gamma_0,...,gamm_(kMax)
     */
    private final double[] gamma;

    private final Map<NKPair, Double> cMap = new HashMap<>();

    /**
     * 唯一のコンストラクタ.
     * 
     * @param nMax 1以上
     * @param kMax 1以上
     */
    TemmeCoefficientRepository(int nMax, int kMax) {
        super();

        this.nMax = nMax;
        this.kMax = kMax;

        this.alpha = new double[nMax + kMax * 2 + 2];
        this.computeAlphas();

        this.gamma = new double[kMax + 1];
        this.computeGammas();
    }

    private void computeAlphas() {
        alpha[0] = 1d;
        for (int k = 2, len = nMax + kMax * 2 + 2; k <= len; k++) {
            double sum = 0d;
            for (int j = 2; j < k; j++) {
                sum += j * alpha[j - 1] * alpha[k - j];
            }
            alpha[k - 1] = (alpha[k - 2] - sum) / (k + 1);
        }
    }

    private double getAlpha(int k) {
        return alpha[k - 1];
    }

    private void computeGammas() {
        double prod = 1d;
        for (int k = 0; k <= kMax; k++) {
            gamma[k] = prod * getAlpha(2 * k + 1);
            prod *= -(2 * k + 3);
        }
    }

    private double getGamma(int k) {
        return gamma[k];
    }

    /**
     * c_n^{(k)} を返す.
     * 
     * @param n n
     * @param k k
     * @return c_n^{(k)}
     */
    double getCnk(int n, int k) {
        if (!(0 <= n && n <= nMax
                && 0 <= k && k <= kMax)) {
            throw new IllegalArgumentException();
        }

        return this.getCnkInner(n, k);
    }

    private double getCnkInner(int n, int k) {
        NKPair pair = new NKPair(n, k);
        Double out = this.cMap.get(pair);

        if (Objects.nonNull(out)) {
            return out.doubleValue();
        }
        out = this.computeCnk(pair);
        this.cMap.put(pair, out);
        return out.doubleValue();
    }

    private Double computeCnk(NKPair nk) {
        int n = nk.n;
        int k = nk.k;

        if (k == 0) {
            return n == 0
                    ? -1d / 3
                    : (n + 2) * getAlpha(n + 2);
        }

        return getGamma(k) * getCnkInner(n, 0) + (n + 2) * getCnkInner(n + 2, k - 1);
    }

    /**
     * (n,k)ペア, 値に基づく等価性を提供.
     */
    private static final class NKPair {

        final int n;
        final int k;

        NKPair(int n, int k) {
            super();
            this.n = n;
            this.k = k;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof NKPair target)) {
                return false;
            }

            return this.n == target.n && this.k == target.k;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + Integer.hashCode(this.n);
            result = 31 * result + Integer.hashCode(this.k);
            return result;
        }
    }

}
