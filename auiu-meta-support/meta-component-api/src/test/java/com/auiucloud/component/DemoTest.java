package com.auiucloud.component;

import cn.hutool.core.convert.Convert;
import org.junit.jupiter.api.Test;

/**
 * @author dries
 **/
public class DemoTest {

    @Test
    void toSBC() {
        String dbc = Convert.toDBC("aaaa,sssss，我的哈哈哈？");
        System.out.println(dbc);
    }
}
