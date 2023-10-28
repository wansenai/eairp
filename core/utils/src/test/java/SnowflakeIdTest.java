import com.wansenai.utils.SnowflakeIdUtil;
import org.junit.jupiter.api.Test;

public class SnowflakeIdTest {

    @Test
    public void generateId() {
        long start = System.currentTimeMillis();
        SnowflakeIdUtil idWorker = new SnowflakeIdUtil(5, 9);
        for (int i = 0; i < 50; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
