package com.tyk.boardev.controllers;

import com.tyk.boardev.entities.ArticleEntity;
import com.tyk.boardev.results.ArticleResult;
import com.tyk.boardev.services.ArticleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite() {
        return "article/write";
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(ArticleEntity article) {
        ArticleResult result = this.articleService.write(article);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        if (result == ArticleResult.SUCCESS) {
            response.put("id", article.getId());
        }
        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@RequestParam(value = "id", required = false) int id,
                           Model model) {
        ArticleEntity article = this.articleService.getById(id);
        model.addAttribute("article", article);
        if (article != null) {
            this.articleService.incrementView(article);
        }
        return "article/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteIndex(ArticleEntity article) {
        ArticleResult result = this.articleService.delete(article.getId(), article.getPassword());
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex(ArticleEntity article) {
        ArticleResult result = this.articleService.modify(article);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        if (result == ArticleResult.SUCCESS) {
            response.put("id", article.getId());
        }
        return response.toString();
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify(@RequestParam(value = "id", required = false) int id,
                            Model model) {
        ArticleEntity article = this.articleService.getById(id);
        model.addAttribute("article", article);
        return "article/modify";
    }

}
