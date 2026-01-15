package com.fatigue.monitoring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果VO
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    public PageVO() {
    }

    public PageVO(Long total, Long current, Long size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
        this.records = records;
    }
}
