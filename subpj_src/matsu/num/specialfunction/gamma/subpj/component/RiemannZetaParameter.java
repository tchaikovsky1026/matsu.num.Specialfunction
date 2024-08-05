package matsu.num.specialfunction.gamma.subpj.component;

/**
 * Riemannゼータ関数の値を扱う.
 * 
 * @author Matsuura Y.
 */
public final class RiemannZetaParameter {

    /**
     * &zeta;(<i>m</i>) - 1 を持つ配列
     * (<i>m</i> &ge; 2). <br>
     */
    private static final double[] ZETA_M1 = {
            0.644934066848226437,
            0.202056903159594285, 0.0823232337111381915, 0.0369277551433699263,
            0.0173430619844491397, 0.00834927738192282684, 0.00407735619794433938,
            0.00200839282608221442, 9.94575127818085337E-4, 4.94188604119464559E-4,
            2.46086553308048299E-4, 1.22713347578489147E-4, 6.12481350587048293E-5,
            3.05882363070204936E-5, 1.52822594086518717E-5, 7.63719763789976227E-6,
            3.81729326499983986E-6, 1.90821271655393893E-6, 9.53962033872796113E-7,
            4.76932986787806463E-7, 2.3845050272773299E-7, 1.19219925965311073E-7,
            5.96081890512594796E-8, 2.98035035146522802E-8, 1.49015548283650412E-8,
            7.45071178983542949E-9, 3.72533402478845705E-9, 1.86265972351304901E-9,
            9.31327432419668183E-10, 4.65662906503378407E-10, 2.32831183367650549E-10,
            1.16415501727005198E-10, 5.82077208790270089E-11, 2.91038504449709969E-11,
            1.45519218910419842E-11, 7.27595983505748101E-12, 3.63797954737865119E-12,
            1.81898965030706595E-12, 9.09494784026388928E-13, 4.54747378304215403E-13,
            2.27373684582465252E-13, 1.13686840768022785E-13, 5.68434198762758561E-14,
            2.84217097688930186E-14, 1.42108548280316068E-14, 7.10542739521085271E-15,
            3.55271369133711367E-15, 1.77635684357912033E-15, 8.8817842109308159E-16,
            4.44089210314381336E-16, 2.22044605079804198E-16, 1.11022302514106613E-16,
            5.55111512484548124E-17, 2.77555756213612417E-17, 1.38777878097252328E-17,
            6.9388939045441537E-18, 3.46944695216592262E-18, 1.73472347604757657E-18,
            8.67361738011993373E-19, 4.33680869002065049E-19, 2.16840434499721979E-19,
            1.08420217249424141E-19, 5.42101086245664541E-20, 2.71050543122346883E-20,
            1.35525271561011646E-20, 6.7762635780451891E-21, 3.38813178902079682E-21,
            1.69406589450979917E-21, 8.47032947254699835E-22, 4.23516473627283335E-22,
            2.11758236813619473E-22, 1.05879118406802339E-22, 5.29395592033987032E-23,
            2.64697796016985296E-23, 1.32348898008489908E-23, 6.61744490042440407E-24,
            3.30872245021217159E-24, 1.65436122510607565E-24, 8.2718061255303444E-25,
            4.13590306276516093E-25, 2.0679515313825767E-25, 1.0339757656912871E-25,
            5.16987882845643132E-26, 2.58493941422821427E-26, 1.29246970711410667E-26,
            6.4623485355705318E-27, 3.23117426778526539E-27, 1.61558713389263252E-27,
            8.07793566946316203E-28, 4.03896783473158083E-28, 2.01948391736579035E-28,
            1.00974195868289515E-28, 5.0487097934144757E-29, 2.52435489670723782E-29,
            1.2621774483536189E-29, 6.3108872417680945E-30, 3.15544362088404724E-30,
            1.57772181044202362E-30, 7.88860905221011807E-31 };

    /**
     * サポートしているmの最小値.
     */
    public static final int MIN_M = 2;

    /**
     * サポートしているmの最大値.
     */
    public static final int MAX_M = 2 + ZETA_M1.length - 1;

    private RiemannZetaParameter() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * mがサポートされているかを判定する.
     * 
     * @param m m
     * @return サポートされているならtrue
     */
    public static boolean accepts(int m) {
        return m >= MIN_M && m <= MAX_M;
    }

    /**
     * &zeta;(<i>m</i>) - 1 を返す.
     * 
     * @param m m
     * @return &zeta;(<i>m</i>) - 1
     * @throws IllegalArgumentException mがサポート外
     */
    public static double zetam1(int m) {
        if (!accepts(m)) {
            throw new IllegalArgumentException("サポート外のm");
        }
        return ZETA_M1[m - 2];
    }

    /**
     * &zeta;(<i>m</i>) を返す.
     * 
     * @param m m
     * @return &zeta;(<i>m</i>)
     * @throws IllegalArgumentException mがサポート外
     */
    public static double zeta(int m) {
        return zetam1(m) + 1d;
    }
}
