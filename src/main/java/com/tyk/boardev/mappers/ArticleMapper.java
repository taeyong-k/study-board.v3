package com.tyk.boardev.mappers;

import com.tyk.boardev.entities.ArticleEntity;
import com.tyk.boardev.vos.PageVo;
import com.tyk.boardev.vos.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int update(@Param(value = "article") ArticleEntity article);

    int insert(@Param(value = "article") ArticleEntity article);

    ArticleEntity selectById(@Param(value = "id") int id);

    ArticleEntity[] selectAll(@Param(value = "pageVo") PageVo pageVo);      // 일반 게시글 목록

    ArticleEntity[] search(@Param(value = "pageVo") PageVo pageVo,          // 검색 결과 목록
                           @Param(value = "searchVo") SearchVo searchVo);

    int selectTotalCount();                                                 // 전체 게시글 수

    int selectSearchCount(@Param(value = "searchVo") SearchVo searchVo);    // 검색 결과 수
}