package com.orderprocessing.orderprocessing.dto;

import java.io.Serializable;

public class OrderRequestDTO implements Serializable
{
    private String item;
    private int quantity;

    public OrderRequestDTO()
    {
    }

    public OrderRequestDTO(String item, int quantity)
    {
        this.item = item;
        this.quantity = quantity;
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
