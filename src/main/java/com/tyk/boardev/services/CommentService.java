package com.tyk.boardev.services;

import com.tyk.boardev.entities.ArticleEntity;
import com.tyk.boardev.entities.CommentEntity;
import com.tyk.boardev.mappers.ArticleMapper;
import com.tyk.boardev.mappers.CommentMapper;
import com.tyk.boardev.results.CommentResult;
import com.tyk.boardev.results.ResultTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(ArticleMapper articleMapper, CommentMapper commentMapper) {
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
    }

    public static boolean isNicknameValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z가-힣]{2,10})$");
    }

    public static boolean isContentValid(String input) {
        return input != null && input.matches("^(.{1,100000})$");
    }

    public ResultTuple<CommentEntity[]> getByArticleId(int articleId) {
        if (articleId < 1) {
            return ResultTuple.<CommentEntity[]>builder()
                    .result(CommentResult.FAILURE)
                    .build();
        }
        ArticleEntity dbArticle = this.articleMapper.selectById(articleId);
        if (dbArticle == null || dbArticle.isDeleted()) {
            return ResultTuple.<CommentEntity[]>builder()
                    .result(CommentResult.FAILURE)
                    .build();
        }
        return ResultTuple.<CommentEntity[]>builder()
                .result(CommentResult.SUCCESS)
                .payload(this.commentMapper.selectAllByArticleId(articleId))
                .build();
    }

    public CommentResult write(CommentEntity comment) {
        if (comment == null ||
                comment.isDeleted() ||
                !CommentService.isNicknameValid(comment.getNickname()) ||
                !CommentService.isContentValid(comment.getContent())) {
            return CommentResult.FAILURE;
        }
        comment.setNickname(comment.getNickname());
        comment.setContent(comment.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDeleted(false);
        return this.commentMapper.insert(comment) > 0
                ? CommentResult.SUCCESS
                : CommentResult.FAILURE;
    }

    public CommentResult delete(CommentEntity comment) {
        if (comment == null || comment.isDeleted()) {
            return CommentResult.FAILURE;
        }
        comment.setDeleted(true);
        return this.commentMapper.update(comment) > 0
                ? CommentResult.SUCCESS
                : CommentResult.FAILURE;
    }

}













