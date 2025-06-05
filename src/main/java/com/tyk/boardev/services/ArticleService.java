package com.tyk.boardev.services;

import com.tyk.boardev.entities.ArticleEntity;
import com.tyk.boardev.mappers.ArticleMapper;
import com.tyk.boardev.results.ArticleResult;
import com.tyk.boardev.vos.PageVo;
import com.tyk.boardev.vos.SearchVo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public static boolean isContentValid(String input) {
        return input != null && input.matches("^(.{1,100000})$");
    }

    public static boolean isTitleValid(String input) {
        return input != null && input.matches("^(.{1,100})$");
    }

    public static boolean isNicknameValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z가-힣]{2,10})$");
    }

    public static boolean isPasswordValid(String input) {
        return input != null && input.matches("^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{4,25})$");
    }

    public ArticleResult write(ArticleEntity article) {
        if (article == null ||
                article.isDeleted() ||
                !ArticleService.isNicknameValid(article.getNickname()) ||
                !ArticleService.isPasswordValid(article.getPassword()) ||
                !ArticleService.isTitleValid(article.getTitle()) ||
                !ArticleService.isContentValid(article.getContent())) {
            return ArticleResult.FAILURE;
        }
        article.setNickname(article.getNickname());
        article.setPassword(article.getPassword());
        article.setTitle(article.getTitle());
        article.setContent(article.getContent());
        article.setView(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setDeleted(false);
        return this.articleMapper.insert(article) > 0
                ? ArticleResult.SUCCESS
                : ArticleResult.FAILURE;
    }

    public ArticleEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectById(id);
    }

    public ArticleResult incrementView(ArticleEntity article) {
        if (article == null || article.getId() < 1) {
            return ArticleResult.FAILURE;
        }
        article.setView(article.getView() + 1);
        return this.articleMapper.update(article) > 0
                ? ArticleResult.SUCCESS
                : ArticleResult.FAILURE;
    }

    public ArticleResult delete(int id, String password) {
        if (id < 1 || password == null || password.isEmpty()) {
            return ArticleResult.FAILURE;
        }
        ArticleEntity dbArticle = this.articleMapper.selectById(id);
        if (dbArticle == null || dbArticle.isDeleted()) {
            return ArticleResult.FAILURE;
        }
        if (!dbArticle.getPassword().equals(password)) {
            return ArticleResult.FAILURE;
        }
        dbArticle.setDeleted(true);
        return this.articleMapper.update(dbArticle) > 0
                ? ArticleResult.SUCCESS
                : ArticleResult.FAILURE;
    }

    public ArticleResult modify(ArticleEntity article) {
        if (article == null || article.getId() < 1 || article.isDeleted() ||
                !ArticleService.isPasswordValid(article.getPassword()) ||
                !ArticleService.isTitleValid(article.getTitle()) ||
                !ArticleService.isContentValid(article.getContent())) {
            return ArticleResult.FAILURE;
        }
        ArticleEntity dbArticle = this.articleMapper.selectById(article.getId());
        if (dbArticle == null || dbArticle.isDeleted()) {
            return ArticleResult.FAILURE;
        }
        if (!dbArticle.getPassword().equals(article.getPassword())) {
            return ArticleResult.FAILURE_PASSWORD;
        }
        dbArticle.setTitle(article.getTitle());
        dbArticle.setContent(article.getContent());
        return this.articleMapper.update(dbArticle) > 0
                ? ArticleResult.SUCCESS
                : ArticleResult.FAILURE;
    }

    public Pair<ArticleEntity[], PageVo> getBySearch(SearchVo searchVo, int page) {         // 검색 조회
        if (page < 1) {
            page = 1;
        }
        int totalCount = this.articleMapper.selectSearchCount(searchVo);                    // 전체 게시글 수 조회 (검색)
        PageVo pageVo = new PageVo(5, page, totalCount);                                    // 페이징 계산
        ArticleEntity[] articles = this.articleMapper.search(pageVo, searchVo);             // 검색된 게시글 목록 조회
        return Pair.of(articles, pageVo);
    }

    public Pair<ArticleEntity[], PageVo> getByBoardId(int page) {                           // 전체 조회
        if (page < 1) {
            page = 1;
        }
        int totalCount = this.articleMapper.selectTotalCount();                             // 전체 게시글 수 조회 (검색 없음)
        PageVo pageVo = new PageVo(5, page, totalCount);                                    // 페이징 계산
        ArticleEntity[] articles = this.articleMapper.selectAll(pageVo);                    // 전체 게시글 목록 조회
        return Pair.of(articles, pageVo);
    }
}















