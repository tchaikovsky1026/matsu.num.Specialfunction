/**
 * 2023.12.5
 */
package matsu.num.specialfunction.gamma;

import java.util.Objects;

import matsu.num.commons.Exponentiation;

/**
 * ガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public final class GammaCalculation {

    private static final GammaCalculation INSTANCE = new GammaCalculation();

    private static final int GAMMA_MAX = 171;
    private static final double GAMMA_THRESHOLD = 40;

    private static final double[] intGammaTable = {
            1d, 1d, 2d, 6d, 24d,
            120d, 720d, 5040d, 40320d, 362880d,
            3628800d, 39916800d, 479001600d, 6227020800d, 87178291200d,
            1307674368000d, 20922789888000d, 355687428096000d, 6402373705728000d, 121645100408832000d,
            2432902008176640000d, 51090942171709440000d, 1124000727777607680000d, 2.585201673888497664E+22d,
            6.2044840173323943936E+23d,
            1.5511210043330985984E+25d, 4.03291461126605635584E+26d, 1.088886945041835216077E+28d,
            3.048883446117138605015E+29d, 8.841761993739701954544E+30d,
            2.652528598121910586363E+32d, 8.222838654177922817726E+33d, 2.631308369336935301672E+35d,
            8.683317618811886495518E+36d, 2.952327990396041408476E+38d,
            1.033314796638614492967E+40d, 3.71993326789901217468E+41d, 1.376375309122634504632E+43d,
            5.2302261746660111176E+44d, 2.039788208119744335864E+46d,
            8.159152832478977343456E+47d, 3.345252661316380710817E+49d, 1.405006117752879898543E+51d,
            6.041526306337383563736E+52d, 2.658271574788448768044E+54d,
            1.19622220865480194562E+56d, 5.50262215981208894985E+57d, 2.58623241511168180643E+59d,
            1.241391559253607267086E+61d, 6.082818640342675608723E+62d,
            3.041409320171337804361E+64d, 1.551118753287382280224E+66d, 8.065817517094387857166E+67d,
            4.274883284060025564298E+69d, 2.308436973392413804721E+71d,
            1.269640335365827592597E+73d, 7.10998587804863451854E+74d, 4.052691950487721675568E+76d,
            2.350561331282878571829E+78d, 1.386831185456898357379E+80d,
            8.320987112741390144276E+81d, 5.075802138772247988009E+83d, 3.146997326038793752565E+85d,
            1.982608315404440064116E+87d, 1.268869321858841641034E+89d,
            8.247650592082470666723E+90d, 5.443449390774430640037E+92d, 3.647111091818868528825E+94d,
            2.480035542436830599601E+96d, 1.711224524281413113725E+98d,
            1.197857166996989179607E+100d, 8.504785885678623175212E+101d, 6.123445837688608686152E+103d,
            4.470115461512684340891E+105d, 3.30788544151938641226E+107d,
            2.480914081139539809195E+109d, 1.885494701666050254988E+111d, 1.451830920282858696341E+113d,
            1.132428117820629783146E+115d, 8.946182130782975286851E+116d,
            7.156945704626380229481E+118d, 5.79712602074736798588E+120d, 4.753643337012841748421E+122d,
            3.94552396972065865119E+124d, 3.314240134565353266999E+126d,
            2.817104114380550276949E+128d, 2.422709538367273238177E+130d, 2.107757298379527717214E+132d,
            1.854826422573984391148E+134d, 1.650795516090846108122E+136d,
            1.48571596448176149731E+138d, 1.352001527678402962552E+140d, 1.243841405464130725548E+142d,
            1.156772507081641574759E+144d, 1.087366156656743080274E+146d,
            1.03299784882390592626E+148d, 9.916779348709496892096E+149d, 9.619275968248211985333E+151d,
            9.426890448883247745626E+153d, 9.33262154439441526817E+155d,
            9.33262154439441526817E+157d, 9.425947759838359420852E+159d, 9.614466715035126609269E+161d,
            9.902900716486180407547E+163d, 1.029901674514562762385E+166d,
            1.081396758240290900504E+168d, 1.146280563734708354534E+170d, 1.226520203196137939352E+172d,
            1.3246418194518289745E+174d, 1.443859583202493582205E+176d,
            1.588245541522742940425E+178d, 1.762952551090244663872E+180d, 1.974506857221074023537E+182d,
            2.231192748659813646597E+184d, 2.54355973347218755712E+186d,
            2.925093693493015690688E+188d, 3.393108684451898201198E+190d, 3.969937160808720895402E+192d,
            4.684525849754290656574E+194d, 5.574585761207605881323E+196d,
            6.689502913449127057588E+198d, 8.094298525273443739682E+200d, 9.875044200833601362412E+202d,
            1.214630436702532967577E+205d, 1.506141741511140879795E+207d,
            1.882677176888926099744E+209d, 2.372173242880046885677E+211d, 3.01266001845765954481E+213d,
            3.856204823625804217357E+215d, 4.97450422247728744039E+217d,
            6.466855489220473672507E+219d, 8.471580690878820510985E+221d, 1.11824865119600430745E+224d,
            1.487270706090685728909E+226d, 1.992942746161518876737E+228d,
            2.690472707318050483595E+230d, 3.65904288195254865769E+232d, 5.012888748274991661035E+234d,
            6.917786472619488492228E+236d, 9.615723196941089004197E+238d,
            1.346201247571752460588E+241d, 1.898143759076170969429E+243d, 2.695364137888162776589E+245d,
            3.854370717180072770522E+247d, 5.550293832739304789551E+249d,
            8.047926057471991944849E+251d, 1.174997204390910823948E+254d, 1.727245890454638911204E+256d,
            2.556323917872865588581E+258d, 3.808922637630569726986E+260d,
            5.713383956445854590479E+262d, 8.627209774233240431623E+264d, 1.311335885683452545607E+267d,
            2.006343905095682394778E+269d, 3.089769613847350887959E+271d,
            4.789142901463393876336E+273d, 7.471062926282894447084E+275d, 1.172956879426414428192E+278d,
            1.853271869493734796544E+280d, 2.946702272495038326504E+282d,
            4.714723635992061322407E+284d, 7.590705053947218729075E+286d, 1.22969421873944943411E+289d,
            2.0044015765453025776E+291d, 3.287218585534296227263E+293d,
            5.423910666131588774984E+295d, 9.003691705778437366474E+297d, 1.503616514864999040201E+300d,
            2.526075744973198387538E+302d, 4.269068009004705274939E+304d,
            7.257415615307998967397E+306d
    };

    private GammaCalculation() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    /**
     * ガンマ関数 &Gamma;(<i>x</i>) を計算する.
     *
     * @param x x, 引数
     * @return &Gamma;(x)
     */
    public double gamma(double x) {
        /*
         * x < 0: NaN
         * x = 0: +inf
         * x = +inf: +inf
         */
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x > GAMMA_THRESHOLD) {
            return Exponentiation.exp(LGammaCalculation.instance().lgamma(x));
        }

        //x = 0における1/Γ(x)の級数展開を使う
        double value = 1;
        while (x > 4) {
            value *= ((x - 1) * (x - 2)) * ((x - 3) * (x - 4));
            x -= 4;
        }
        while (x > 1) {
            value *= x - 1;
            x -= 1;
        }
        return value * gamma_smallkernel(x);
    }

    /**
     * 0≦x≦1におけるΓ(x)
     */
    private double gamma_smallkernel(double x) {

        //1/Γ(x)を計算し, 逆数をとって返す.
        double GS1 = 1;
        double GS2 = 0.5772156649015238830941825047995205;
        double GS3 = -0.6558780715194271311905445528280728;
        double GS4 = -0.04200263506068912249136114614773858;
        double GS5 = 0.1665386118137646712762881561739282;
        double GS6 = -0.04219773869985819648250288093243171;
        double GS7 = -0.009621945792574902843361766824272098;
        double GS8 = 0.007218834322573813253151308851088317;
        double GS9 = -0.001164843227062886222715396585129185;
        double GS10 = -0.0002159328405011461404463856976358929;
        double GS11 = 0.0001291084968731937103581841272117679;
        double GS12 = -0.00002128810826537580222562658541710358;
        double GS13 = -3.806933070349227688428280597293888E-7;
        double GS14 = 7.068118361409347040325775519785248E-7;
        double GS15 = -9.040488593688208747165525475953710E-8;

        final double x2 = x * x;
        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        final double value0 = x * GS1 + x2 * (GS2 + x * GS3);
        final double value4 = (GS4 + x * GS5) + x2 * (GS6 + x * GS7);
        final double value8 = (GS8 + x * GS9) + x2 * (GS10 + x * GS11);
        final double value12 = GS12 + x * GS13 + x2 * (GS14 + x * GS15);

        return 1 / (value0 + x4 * value4 + x8 * (value8 + x4 * value12));
    }

    /**
     * 整数引数のガンマ関数 &Gamma;(<i>n</i>) を計算する.
     * 
     * @param n
     * @return gamma(n)
     */
    public double gamma(int n) {
        /*
         * n < 0: NaN.
         * n = 0 or n >> 1: +inf
         */
        if (n >= 1 && n <= GAMMA_MAX) {
            return intGammaTable[n - 1];
        } else if (n >= 0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return Double.NaN;
        }
    }

    /**
     * 
     * @return インスタンス
     */
    public static GammaCalculation instance() {
        return INSTANCE;
    }

}
