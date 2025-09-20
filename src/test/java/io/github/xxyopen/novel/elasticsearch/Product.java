package io.github.xxyopen.novel.elasticsearch;

import java.util.Date;

public class Product {
    private String id;
    private String name;
    private Integer price;
    private Integer stock;
    private String image;
    private String category;
    private String brand;
    private Integer sold;
    private Integer commentCount;
    private Boolean isAD;
    private Date updateTime;

    // 构造方法
    public Product(String id, String name, Integer price, Integer stock, String image,
                   String category, String brand, Integer sold, Integer commentCount,
                   Boolean isAD, Date updateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.category = category;
        this.brand = brand;
        this.sold = sold;
        this.commentCount = commentCount;
        this.isAD = isAD;
        this.updateTime = updateTime;
    }

    // getter 和 setter 方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getAD() {
        return isAD;
    }

    public void setAD(Boolean AD) {
        isAD = AD;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
