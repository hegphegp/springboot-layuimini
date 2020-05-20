package com.hegp.model;

/**
 * @author hgp
 * @date 20-5-20
 */
public class Param {
    private String name;
    private String type;
    private String format; // 只有时间的需要格式化, 其他的动态写SQL就可以了
    private String description;
    private String defaultValue;

    public Param() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
