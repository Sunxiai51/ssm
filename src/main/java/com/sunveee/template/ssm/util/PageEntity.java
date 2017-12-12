package com.sunveee.template.ssm.util;

/**
 * 分页实体类
 * 
 * @author 51
 * @version $Id: PageEntity.java, v 0.1 2017年12月12日 下午3:47:14 51 Exp $
 */
public class PageEntity {
    private int pageNo;
    private int pageSize;
    private int startNo;

    private PageEntity(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.startNo = pageNo * pageSize;
    }

    public static PageEntity getInstance(int pageNo, int pageSize) {
        return new PageEntity(pageNo, pageSize);
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartNo() {
        return startNo;
    }

}
