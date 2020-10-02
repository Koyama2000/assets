package com.kgc.assets.assetsservice.service;

import com.alibaba.fastjson.JSON;
import com.kgc.assets.assetsservice.mapper.AssetsMapper;
import com.kgc.assets.bean.Assets;
import com.kgc.assets.service.AssetsService;
import com.kgc.assets.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


    @Override
    public int InSert_Assets(Assets assets) {
        int result=assetsMapper.insertSelective(assets);
        if(result>0){
            //es
            this.setEs();
            //redis
            String asskey="stu:"+assets.getId()+":info";
            //随机时间，防止缓存雪崩
            Random random=new Random();
            int i = random.nextInt(10);
            Jedis jedis=redisUtil.getJedis();
            jedis.del(asskey);
            jedis.setex(asskey,i*60*10,JSON.toJSONString(assets));
        }
        return result;
    }

    @Override
    public List<Assets> SelectList(String assetsName, String assetsType) {
        List<Assets> assetslist=new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if(assetsName!=null&&assetsName!=""){
            TermQueryBuilder termQueryBuilder=new TermQueryBuilder("assetsName",assetsName);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(assetsType!=null&&assetsType!=""){
            TermQueryBuilder termQueryBuilder=new TermQueryBuilder("assetsType",assetsType);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("assetsName");
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("ass").addType("assetsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Assets,Void>> hits=searchResult.getHits(Assets.class);
            for (SearchResult.Hit<Assets,Void> hit: hits){
                Assets assets=hit.source;
                assetslist.add(assets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetslist;
    }

    @Override
    public int Del_Assets(int id) {
        int result=assetsMapper.deleteByPrimaryKey(id);
        if(result>0){
            //es
            this.setEs();
            //redis
            String asskey="stu:"+id+":info";
            Jedis jedis=redisUtil.getJedis();
            jedis.del(asskey);
        }
        return result;
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
