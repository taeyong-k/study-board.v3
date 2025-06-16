package com.tyk.boardev.mappers;

import com.tyk.boardev.entities.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    int update(@Param(value = "comment") CommentEntity comment);

    int insert(@Param(value = "comment") CommentEntity comment);

    CommentEntity[] selectAllByArticleId(@Param(value = "articleId") int articleId);
}

