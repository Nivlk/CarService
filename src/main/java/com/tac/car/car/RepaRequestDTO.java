package com.tac.car.car;

import java.util.Date;
import java.util.List;

public class RepaRequestDTO {
    private String category;
    private String subs;
    private Date fechain ;
    private Date fechasal;
    private String status;
    private String priorioty;
    private String comments;
    private Long id_auto;
    private List<String> images;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubs() {
        return subs;
    }

    public void setSubs(String subs) {
        this.subs = subs;
    }

    public Date getFechain() {
        return fechain;
    }

    public void setFechain(Date fechain) {
        this.fechain = fechain;
    }

    public Date getFechasal() {
        return fechasal;
    }

    public void setFechasal(Date fechasal) {
        this.fechasal = fechasal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriorioty() {
        return priorioty;
    }

    public void setPriorioty(String priorioty) {
        this.priorioty = priorioty;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getId_auto() {
        return id_auto;
    }

    public void setId_auto(Long id_auto) {
        this.id_auto = id_auto;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


}
