package com.yly.wiki.req;

public class CategoryQueryReq extends PageReq {
    private String id;

    private String parent;

    private String name;

    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{" +
                "id='" + id + '\'' +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                "} " ;
    }
}