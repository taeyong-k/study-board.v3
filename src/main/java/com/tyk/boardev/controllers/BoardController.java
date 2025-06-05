package com.tyk.boardev.controllers;

import com.tyk.boardev.entities.ArticleEntity;
import com.tyk.boardev.services.ArticleService;
import com.tyk.boardev.vos.PageVo;
import com.tyk.boardev.vos.SearchVo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
    private final ArticleService articleService;

    @Autowired
    public BoardController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           SearchVo searchVo,
                           Model model) {
        Pair<ArticleEntity[], PageVo> result;
        if (searchVo != null && searchVo.getKeyword() != null && !searchVo.getKeyword().isEmpty()) {
            result = this.articleService.getBySearch(searchVo, page);
        } else {
            result = this.articleService.getByBoardId(page);
        }
        model.addAttribute("articles", result.getLeft());
        model.addAttribute("pageVo", result.getRight());
        model.addAttribute("searchVo", searchVo);
        return "board/index";
    }
}














