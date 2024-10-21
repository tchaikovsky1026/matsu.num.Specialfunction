package matsu.num.specialfunction.subpj.gamma.component;

import java.math.BigDecimal;
import java.util.Arrays;

import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;

/**
 * Riemannゼータ関数の値を扱う.
 * 
 * @author Matsuura Y.
 */
public final class RiemannZetaParameterByDoubleDoubleFloat {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();

    /**
     * &zeta;(<i>m</i>) - 1 を持つ配列
     * (<i>m</i> &ge; 2). <br>
     */
    private static final String[] ZETA_M1_STRING = {
            "0.64493406684822643647241516664602518922",
            "0.20205690315959428539973816151144999076",
            "0.082323233711138191516003696541167902775",
            "0.036927755143369926331365486457034168057",
            "0.017343061984449139714517929790920527902",
            "0.0083492773819228268397975498497967595999",
            "0.004077356197944339378685238508652465259",
            "0.0020083928260822144178527692324120604856",
            "9.9457512781808533714595890031901700602E-4",
            "4.9418860411946455870228252646993646861E-4",
            "2.4608655330804829863799804773967096042E-4",
            "1.2271334757848914675183652635739571428E-4",
            "6.1248135058704829258545105135333747482E-5",
            "3.0588236307020493551728510645062587628E-5",
            "1.5282259408651871732571487636722023237E-5",
            "7.6371976378997622736002935630292130882E-6",
            "3.8172932649998398564616446219397304547E-6",
            "1.9082127165539389256569577951013532586E-6",
            "9.5396203387279611315203868344934594379E-7",
            "4.7693298678780646311671960437304596645E-7",
            "2.3845050272773299000364818675299493504E-7",
            "1.1921992596531107306778871888232638726E-7",
            "5.9608189051259479612440207935801227504E-8",
            "2.9803503514652280186063705069366011845E-8",
            "1.4901554828365041234658506630698628865E-8",
            "7.4507117898354294919810041706041194547E-9",
            "3.7253340247884570548192040184024232329E-9",
            "1.8626597235130490064039099454169480617E-9",
            "9.3132743241966818287176473502121981357E-10",
            "4.6566290650337840729892332512200710627E-10",
            "2.3283118336765054920014559759404950248E-10",
            "1.1641550172700519775929738354563095165E-10",
            "5.8207720879027008892436859891063054173E-11",
            "2.9103850444970996869294252278840464107E-11",
            "1.4551921891041984235929632245318420984E-11",
            "7.2759598350574810145208690123380592649E-12",
            "3.6379795473786511902372363558732735126E-12",
            "1.8189896503070659475848321007300850306E-12",
            "9.0949478402638892825331183869490875386E-13",
            "4.547473783042154026799112029488570339E-13",
            "2.2737368458246525152268215779786912138E-13",
            "1.1368684076802278493491048380259064374E-13",
            "5.6843419876275856092771829675240685531E-14",
            "2.8421709768893018554550737049426620744E-14",
            "1.4210854828031606769834307141739537679E-14",
            "7.1054273952108527128773544799568000227E-15",
            "3.5527136913371136732984695340593429921E-15",
            "1.7763568435791203274733490144002795702E-15",
            "8.8817842109308159030960913863913863256E-16"
    };

    /**
     * &zeta;(<i>m</i>) - 1 を持つ配列
     * (<i>m</i> &ge; 2). <br>
     */
    private static final DoubleDoubleFloatElement[] ZETA_M1;

    /**
     * サポートしているmの最小値.
     */
    public static final int MIN_M = 2;

    /**
     * サポートしているmの最大値.
     */
    public static final int MAX_M = 2 + ZETA_M1_STRING.length - 1;

    static {
        ZETA_M1 = new DoubleDoubleFloatElement[ZETA_M1_STRING.length];

        for (int i = 0; i < ZETA_M1.length; i++) {
            BigDecimal value = new BigDecimal(ZETA_M1_STRING[i]);
            double high = value.doubleValue();
            double low = value.subtract(new BigDecimal(high)).doubleValue();

            ZETA_M1[i] = PROVIDER.fromDoubleValue(high).plus(low);
        }
    }

    private RiemannZetaParameterByDoubleDoubleFloat() {
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
    public static DoubleDoubleFloatElement zetam1(int m) {
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
    public static DoubleDoubleFloatElement zeta(int m) {
        return zetam1(m).plus(PROVIDER.one());
    }

    /**
     * リーマンゼータが計算できているかのテスト
     * 
     * @param args
     */
    public static void main(String[] args) {
        Arrays.stream(ZETA_M1).forEach(System.out::println);
    }
}
