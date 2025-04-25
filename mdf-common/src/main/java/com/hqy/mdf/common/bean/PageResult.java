package com.hqy.mdf.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hqy
 */
@Data
public class PageResult<T> implements Serializable {
    /**
     * 当前页
     */
    private long page;

    /**
     * 当前记录数
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 分页结果集
     */
    private List<T> list;

}
