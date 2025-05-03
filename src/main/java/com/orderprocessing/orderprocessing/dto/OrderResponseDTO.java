package com.orderprocessing.orderprocessing.dto;

public class OrderResponseDTO
{
    private long orderId;
    private String item;
    private int quantity;

    public OrderResponseDTO()
    {
    }

    public OrderResponseDTO(long orderId, String item, int quantity)
    {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
