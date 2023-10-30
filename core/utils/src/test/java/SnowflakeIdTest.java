import com.wansenai.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SnowflakeIdTest {

    @Test
    public void generateId() {
        long start = System.currentTimeMillis();
        SnowflakeIdUtil idWorker = new SnowflakeIdUtil(5, 9);
        for (int i = 0; i < 50; i++) {
            long id = idWorker.nextId();
            log.info(String.valueOf(id));
        }
        long end = System.currentTimeMillis();
        log.info(String.valueOf(end - start));
    }
}
