package matsu.num.specialfunction.err.subpj.erfi.demo;

/**
 * スケーリング虚数誤差関数を表す.
 * 
 * @author Matsuura Y.
 */
interface Erfix {

    /**
     * 与えられた x に対する erfix(x) の値を返す.
     * 
     * @param x x
     * @return erfix(x)
     */
    public abstract double erfix(double x);
}
