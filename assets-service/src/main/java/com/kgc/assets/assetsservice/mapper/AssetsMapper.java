package com.kgc.assets.assetsservice.mapper;

import com.kgc.assets.bean.Assets;
import com.kgc.assets.bean.AssetsExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssetsMapper {
    int countByExample(AssetsExample example);

    int deleteByExample(AssetsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Assets record);

    int insertSelective(Assets record);

    List<Assets> selectByExample(AssetsExample example);

    Assets selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Assets record, @Param("example") AssetsExample example);

    int updateByExample(@Param("record") Assets record, @Param("example") AssetsExample example);

    int updateByPrimaryKeySelective(Assets record);

    int updateByPrimaryKey(Assets record);
}