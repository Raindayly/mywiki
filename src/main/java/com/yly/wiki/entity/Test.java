package com.yly.wiki.entity;

/**
 * @version 1.0
 * @Author 姚李岩
 * @Date 2022/12/26 14:30
 * 这个包可以使pojo domain entity 都表示实体类
 */
public class Test {

    private  Integer id;
    private  String name;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
