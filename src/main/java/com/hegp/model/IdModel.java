package com.hegp.model;

/**
 * @author hgp
 * @date 20-5-20
 */
public class IdModel {
    private Object id;

    public IdModel() { }

    public IdModel(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public static IdModel build(Object id) {
        return new IdModel(id);
    }
}
