import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TotalPriceTest {

    @Test
    public void testTotalPrice() {
        var a = BigDecimal.valueOf(83.75);
        var b = BigDecimal.valueOf(21.352).negate();

        System.out.println(a);
        System.out.println(b);
        System.out.println(a.add(b));
    }
}
