package com.kgc.assets.assetsweb.controller;

import com.kgc.assets.bean.Assets;
import com.kgc.assets.service.AssetsService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AssetsController {
    @Reference
    AssetsService assetsService;

    @GetMapping("/ass/list")
    @ResponseBody
    public List<Assets> assetsList(@RequestParam(value = "assetName",required = false,defaultValue = "") String assetName,
                                   @RequestParam(value = "assetType",required = false,defaultValue = "")String assetType){
        List<Assets> assets = assetsService.SelectList(assetName, assetType);
        return assets;
    }

    @PostMapping("/ass/del")
    @ResponseBody
    public int delass(Integer id){
        int i=assetsService.Del_Assets(id);
        return i;
    }

    @PostMapping("/saveass")
    @ResponseBody
    public int saveass(@RequestBody Assets assets){

        if(assetsService.SelectByassetId(assets.getAssetid())!=null){
            return 3;
        }
        int i=assetsService.InSert_Assets(assets);
        return i;
    }


}
