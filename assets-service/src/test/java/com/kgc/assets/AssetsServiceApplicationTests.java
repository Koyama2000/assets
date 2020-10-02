package com.kgc.assets;

import com.kgc.assets.assetsservice.mapper.AssetsMapper;
import com.kgc.assets.bean.Assets;
import com.kgc.assets.service.AssetsService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AssetsServiceApplicationTests {
    @Reference
    AssetsService assetsService;
    @Resource
    AssetsMapper assetsMapper;
    @Resource
    JestClient jestClient;
    @Test
    void contextLoads() {
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
