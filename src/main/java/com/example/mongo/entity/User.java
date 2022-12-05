package com.example.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author away
 * @date 2022/12/5
 */
@Document("users") // 这个类实例代表 mongo 中的一条
public class User {

    @Id // 用来映射这个 id 和 文档中的 _id
    private Integer id;
    @Field("username")
    private String name;
    @Field
    private Double salary;
    @Field
    private Date brithday;

    public User() {
    }

    public User(Integer id, String name, Double salary, Date brithday) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.brithday = brithday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", brithday=" + brithday +
                '}';
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getBrithday() {
        return brithday;
    }

    public void setBrithday(Date brithday) {
        this.brithday = brithday;
    }
}
