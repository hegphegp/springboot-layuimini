package com.hegp.core.id.generator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author hgp
 * @date 20-5-22
 */
public class SnowflakeId {
    public static Snowflake snowflake = IdUtil.createSnowflake(31, 31);

    public static Long id() {
        return snowflake.nextId();
    }

}
