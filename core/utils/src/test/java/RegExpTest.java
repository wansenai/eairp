import com.wansenai.utils.RegExpTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RegExpTest {

    @Test
    public void exp() {
        List<String> values = new ArrayList<>();

        values.add("310");
        values.add(String.valueOf(2));
        values.add(String.valueOf(3));

        RegExpTools.RegExp exp = new RegExpTools.RegExp();

        exp.any();
        exp.quote("fullKbNum").colon()
                .quote()
                .value("[a-zA-Z0-9]*").or(values).value("[a-zA-Z0-9]*")
                .quote();
        exp.or();
        exp.quote("gbId[a-f0-9-]{36}").colon()
                .quote()
                .value("[0-9]*").or(values).value("[0-9]*")
                .quote();
        exp.any();

        log.info(String.valueOf(exp));
    }

    @Test
    public void testRegExpTools() {
        List<String> search = new ArrayList<>();
        search.add("a");
        search.add("b");
        search.add("c");
        String regexp = RegExpTools.regexp(search);
        log.info(regexp);
        Assert.isTrue(regexp.equals(".*a.*|.*b.*|.*c.*"), "RegExpTools.regexp(List<String> search)方法测试失败");
        String key = "key";
        regexp = RegExpTools.regexp(key, search);
        log.info(regexp);
        Assert.isTrue(regexp.equals(".*\\\"key\\\":\\\"[a-zA-Z0-9]*(a|b|c)[a-zA-Z0-9]*\\\".*"), "RegExpTools.regexp(String key, List<String> search)方法测试失败");
    }

}
