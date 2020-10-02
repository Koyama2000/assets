package com.kgc.assets.assetsservice.service;

import com.kgc.assets.assetsservice.mapper.AssetsMapper;
import com.kgc.assets.bean.Assets;
import com.kgc.assets.service.AssetsService;
import com.kgc.assets.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssetsServiceImpl implements AssetsService {
    @Resource
    AssetsMapper assetsMapper;
    @Resource
    JestClient jestClient;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedissonClient redissonClient;
    @Resource
    EsService esService;

    @Override
    public int InSert_Assets(Assets assets) {
        int result=assetsMapper.insertSelective(assets);
        if(result>0){
            //es
            this.setEs();
        }
        return result;
    }

    @Override
    public List<Assets> SelectList(String assetName, String assetType) {
        List<Assets> Assets=new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if(assetName!=null&&assetName.isEmpty()==false){
//            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("assetname",assetName);
//            boolQueryBuilder.must(matchQueryBuilder);
        } if(assetType!=null&&assetType.isEmpty()==false){

        }
            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("assettype",assetType);
            boolQueryBuilder.must(matchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("id",SortOrder.DESC);

        //高亮
//        HighlightBuilder highlightBuilder=new HighlightBuilder();
//        highlightBuilder.field("assetname");
//        highlightBuilder.preTags("<span style='color:red;'>");
//        highlightBuilder.postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);


        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("assets").addType("assetsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Assets,Void>> hits=searchResult.getHits(Assets.class);
            for (SearchResult.Hit<Assets,Void> hit: hits){

                Assets assets=hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                if (highlight!=null){
//                    String assetname = highlight.get("assetname").get(0);
                    //使用高亮的skuName替换原来的skuName
//                    assets.setAssetname(assetname);
                }
                Assets.add(assets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Assets;
    }

    @Override
    public int Del_Assets(int id) {
        int result=assetsMapper.deleteByPrimaryKey(id);
        String index=String.valueOf(id);
        try {
            esService.deleteData(index,"assets","assetsinfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Assets SelectByassetId(String assetId) {
        Assets assets=null;
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("assetid",assetId);
        boolQueryBuilder.must(matchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        System.out.println(searchSourceBuilder.toString());
        Search search=new Search.Builder(searchSourceBuilder.toString()).addIndex("assets").addType("assetsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Assets,Void>> hits=searchResult.getHits(Assets.class);
            for (SearchResult.Hit<Assets,Void> hit: hits){
                Assets AssetsInfo=hit.source;
               assets=AssetsInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assets;
    }


    public  List<Assets> getEs(){
        List<Assets> Assetslist=new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        searchSourceBuilder.sort("intodate", SortOrder.DESC);
        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("assets").addType("assetsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Assets,Void>> hits=searchResult.getHits(Assets.class);
            for (SearchResult.Hit<Assets,Void> hit: hits){
                Assets AssetsInfo=hit.source;
                Assetslist.add(AssetsInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Assetslist;
    }

    public void setEs(){
        List<Assets> allass = assetsMapper.selectByExample(null);
        System.out.println("assetslist:"+allass);
        List<Assets> assetsInfos=new ArrayList<>();
        for (Assets ass : allass) {
            Assets assets = new Assets();
            BeanUtils.copyProperties(ass,assets);
            assetsInfos.add(assets);
        }
        System.out.println(assetsInfos);
        for (Assets ass : assetsInfos) {
            Index index=new Index.Builder(ass).index("assets").type("assetsinfo").id(ass.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
