package com.biubiu.dao;

import com.biubiu.pojo.Ecs;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface EcsDao {
    @Insert({
            "<script>",
                "insert into ecs",
                "<trim prefix='(' suffix=')' suffixOverrides=','>",
                    "`name`,",
                    "<if test='description != null'>",
                        "description,",
                    "</if>",
                    "<if test='config != null'>",
                        "`config`,",
                    "</if>",
                    "`parent_id`,",
                    "`type`,",
                "</trim>",
                "<trim prefix='values (' suffix=')' suffixOverrides=','>",
                    "#{name},",
                    "<if test='description != null'>",
                        "#{description},",
                    "</if>",
                    "<if test='config != null'>",
                        "#{config},",
                    "</if>",
                    "#{parentId},",
                    "#{type},",
                "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Ecs ecs);

    @Update({
            "<script>",
            "update ecs ",
                "<set>",
                    "<if test='parentId != null'>",
                        "`parent_id` = #{parentId},",
                    "</if>",
                    "<if test='type != null'>",
                        "`type` = #{type},",
                    "</if>",
                    "<if test='name != null'>",
                        "`name` = #{name},",
                    "</if>",
                    "<if test='description != null'>",
                        "`description` = #{description},",
                    "</if>",
                    "<if test='config != null'>",
                        "`config` = #{config},",
                    "</if>",
                "</set>",
                "where id = #{id}",
            "</script>"
    })
    int update(Ecs ecs);

    @Select({
            "select * from ecs"
    })
    List<Ecs> list();

    @Select({
            "select * from ecs where id = #{id}"
    })
    List<Ecs> listById(@Param("id") Long id);

    @Select({
            "<script>",
                "select * from ecs where id in ",
                "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>",
    })
    List<Ecs> listByIds(@Param("ids") Set<Long> ids);

    @Delete({
            "<script>",
                "delete from ecs where id in ",
                "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>",
    })
    int batchDelete(@Param("ids") Set<Long> ids);

}
