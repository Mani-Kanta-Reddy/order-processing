package com.orderprocessing.orderprocessing.dto;

import java.util.List;

public class PagedResponseDTO<T>
{
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public PagedResponseDTO()
    {
    }

    public PagedResponseDTO(List<T> data, int currentPage, int pageSize, long totalElements, int totalPages, boolean isLast)
    {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public long getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(long totalElements)
    {
        this.totalElements = totalElements;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public boolean isLast()
    {
        return isLast;
    }

    public void setLast(boolean last)
    {
        isLast = last;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }
}

