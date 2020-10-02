package com.kgc.assets.assetsservice.service;

import com.kgc.assets.assetsservice.mapper.AssetsMapper;
import com.kgc.assets.bean.Assets;
import com.kgc.assets.service.AssetsService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Resource
    AssetsMapper assetsMapper;



    @Override
    public int InSert_Assets(Assets assets) {
        return 0;
    }

    @Override
    public List<Assets> SelectList(String assetsName, String assetsType) {
        return null;
    }

    @Override
    public int Del_Assets(int id) {
        return 0;
    }
}
