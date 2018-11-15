package com.rongke.commons;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class PageDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private int pageNum;
    /**
     * 每页数据条数
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 返回的数据结果集
     */
    private List list;
    /**
     * 是否是第一页
     */
    private boolean isFirstPage;
    /**
     * 是否是最后页
     */
    private boolean isLastPage;
    /**
     * 是否还有前页
     */
    private boolean hasPreviousPage;
    /**
     * 是否还有后面页
     */
    private boolean hasNextPage;

    public PageDto() {
        this.isFirstPage = false;
        this.isLastPage = false;
        this.hasPreviousPage = false;
        this.hasNextPage = false;
    }

    public PageDto(int pageNum, int pageSize, List list, long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.total = total;
        this.pages = this.getPageSize() == 0 ? 1 : (int) Math.ceil((double) this.getTotal() / (double) this.getPageSize());
        this.hasPreviousPage = this.getPageNum() - 1 > 0;
        this.hasNextPage = this.getPageNum() < this.getPages();
        this.isFirstPage = !(this.getPageNum() - 1 > 0);
        this.isLastPage = !(this.getPageNum() < this.getPages());
    }

    public PageDto(Page page) {
        this.pageNum = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.total = page.getTotalElements();
        this.pages = page.getTotalPages();
        this.list = page.getContent();
        this.isFirstPage = page.isFirst();
        this.isLastPage = page.isLast();
        this.hasPreviousPage = page.hasPrevious();
        this.hasNextPage = page.hasNext();
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public boolean getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
