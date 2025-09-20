package io.github.xxyopen.novel.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemDoc {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String category;
    private String brand;
    private Integer sold;
    private Integer commentCount;
    private Long updateTime;
    private Boolean ad;


    public ItemDoc(String id, String name, BigDecimal price, Integer stock,
                   String image, String category, String brand, Integer sold,
                   Integer commentCount, Long updateTime, Boolean ad) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.category = category;
        this.brand = brand;
        this.sold = sold;
        this.commentCount = commentCount;
        this.updateTime = updateTime;
        this.ad = ad;
    }

    public ItemDoc() {
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getAd() {
        return ad;
    }

    public void setAd(Boolean ad) {
        this.ad = ad;
    }


    @Override
    public String toString() {
        return "ItemDoc{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", sold=" + sold +
                ", commentCount=" + commentCount +
                ", updateTime=" + updateTime +
                ", ad=" + ad +
                '}';
    }
}
