package com.kgc.assets.service;

import com.kgc.assets.bean.Assets;

import java.util.List;

public interface AssetsService {
    //添加固定资产
    public int InSert_Assets(Assets assets);
    //查询固定资产
    public List<Assets> SelectList(String assetName, String assetType);
    //删除固定资产
    public int Del_Assets(int id);
    //根据assetId查询
    public Assets SelectByassetId(String assetId);
}
