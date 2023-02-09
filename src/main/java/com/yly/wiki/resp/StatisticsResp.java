package com.yly.wiki.resp;

import java.util.Date;

public class StatisticsResp {

    Date date;

    Integer voteCount;

    Integer viewCount;

    Integer voteIncrease;

    Integer viewIncrease;



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(Integer voteIncrease) {
        this.voteIncrease = voteIncrease;
    }

    public Integer getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(Integer viewIncrease) {
        this.viewIncrease = viewIncrease;
    }


    @Override
    public String toString() {
        return "StatisticsResp{" +
                "date=" + date +
                ", voteCount=" + voteCount +
                ", viewCount=" + viewCount +
                ", voteIncrease=" + voteIncrease +
                ", viewIncrease=" + viewIncrease +
                '}';
    }
}