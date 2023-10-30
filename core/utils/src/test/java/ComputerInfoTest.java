import com.wansenai.utils.ComputerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
public class ComputerInfoTest {

    @Test
    public void getMacAddress() throws IOException {
        log.info(ComputerInfo.getMacAddress());
        log.info(ComputerInfo.getComputerName());
        log.info(ComputerInfo.getIpAddr());
        log.info(ComputerInfo.getIpAddrAndName());
    }
}
