package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-06.
 */

public class CollectionType implements Serializable {
    private String value;
    private String name;

    public CollectionType(String value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
