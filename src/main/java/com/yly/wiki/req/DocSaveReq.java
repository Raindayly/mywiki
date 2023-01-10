package com.yly.wiki.req;

import javax.validation.constraints.NotNull;

public class DocSaveReq {
    private String id;

    @NotNull(message = "[电子书id]不能为空")
    private String ebookId;

    @NotNull(message = "[父级]不能为空")
    private String parent;

    @NotNull(message = "[名称]不能为空")
    private String name;

    @NotNull(message = "[排序]不能为空")
    private Integer sort;

    @NotNull(message = "[内容]不能为空")
    private String content;

    private Integer viewCount = 0;

    private Integer voteCount = 0;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEbookId() {
        return ebookId;
    }

    public void setEbookId(String ebookId) {
        this.ebookId = ebookId;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id='" + id + '\'' +
                ", ebookId='" + ebookId + '\'' +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", content='" + content + '\'' +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                '}';
    }
}