package com.zsw.springboot_consumer.entity;

import java.io.Serializable;

/**
 * @author 卓少武
 * @date 2019/11/18
 */
public class Order implements Serializable {

    private Integer id;

    private String name;

    private Integer number;

    public Order() {
    }

    public Order(Integer id, String name, Integer number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
