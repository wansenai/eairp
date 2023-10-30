import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static com.wansenai.utils.CommonTools.*;

@Slf4j
public class CommonToolsTest {

    @Test
    public void stringToArray() {
        String str = "这是一个字符串的测试";
        char[] array = str.toCharArray();
        for (char item : array) {
            log.info(String.valueOf(item));
        }
    }

    @Test
    public void getBeforeMonthTest() {
        log.info(getBeforeMonth(1));
    }

    @Test
    public void md5EncrypTest() {
        log.info(md5Encryp("guest"));
        log.info(md5Encryp("admin"));
    }

    @Test
    public void checkNumber() {
        String value = "2333";
        Assert.isTrue(checkStrIsNum(value), "checkStrIsNumber(value) 返回 false");
    }

    @Test
    public void RandomCharTest() {
        for (int i = 0; i < 100; i++) {
            log.info(getRandomChar() + "||");
        }
    }

    @Test
    public void getUUIDTest() {
        log.info(getUUID_32());
    }
}
