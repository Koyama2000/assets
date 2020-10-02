package com.kgc.assets.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Assets implements Serializable {
    private Integer id;

    private String assetid;

    private String assetname;

    private String assettype;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date intodate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid == null ? null : assetid.trim();
    }

    public String getAssetname() {
        return assetname;
    }

    public void setAssetname(String assetname) {
        this.assetname = assetname == null ? null : assetname.trim();
    }

    public String getAssettype() {
        return assettype;
    }

    public void setAssettype(String assettype) {
        this.assettype = assettype == null ? null : assettype.trim();
    }

    public Date getIntodate() {
        return intodate;
    }

    public void setIntodate(Date intodate) {
        this.intodate = intodate;
    }

    @Override
    public String toString() {
        return "Assets{" +
                "id=" + id +
                ", assetid='" + assetid + '\'' +
                ", assetname='" + assetname + '\'' +
                ", assettype='" + assettype + '\'' +
                ", intodate=" + intodate +
                '}';
    }
}