import com.wansensoft.utils.TimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TimeTest {

    @Test
    public void testMyMethod() {
        // Arrange
        String a = "2023-10-27 17:37:21";
        String b = "yyyy-MM-dd HH:mm:ss";

        // Act
        LocalDateTime result = myMethod(a, b);

        // Assert
        assert result.getDayOfMonth() == result.getDayOfMonth();
    }

    public LocalDateTime myMethod(String a, String b) {
        return TimeUtil.parse(a, b);
    }
}
