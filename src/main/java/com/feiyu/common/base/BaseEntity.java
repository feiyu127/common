package com.feiyu.common.base;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:34
 */
public abstract class BaseEntity implements Serializable {
    /**
     * 页码
     */
    @TableField(exist = false)
    @JsonIgnore
    protected Integer pageNum;

    /**
     * 每页数
     */
    @TableField(exist = false)
    @JsonIgnore
    protected Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
