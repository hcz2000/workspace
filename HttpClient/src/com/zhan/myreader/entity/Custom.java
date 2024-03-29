package com.zhan.myreader.entity;

import java.io.Serializable;

/**
 * Created by zhan on 2016/11/2.
 */

public class Custom implements Serializable {

    private static final long serialVersionUID = 5088810102696918656L;

    private String id;
    private String type;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
