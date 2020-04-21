package com.wemedia.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wemedia.model.BizArticle;
import com.wemedia.service.BizArticleService;
import com.wemedia.service.BizThemeService;
import com.wemedia.util.CopyUtil;
import com.wemedia.util.CoreConst;
import com.wemedia.vo.ArticleConditionVo;
import com.wemedia.vo.base.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BlogWebController {
    private static final Logger log = LoggerFactory.getLogger(BlogWebController.class);
    private static final String THEME_PREFIX = "theme/";
    @Autowired
    private BizArticleService bizArticleService;
    @Autowired
    private BizThemeService bizThemeService;

    /**
     * 客户端按给定条件分页查询方法
     * @param model
     * @param vo
     */
    private void loadMainPage(Model model, ArticleConditionVo vo) {
        //文章当前状态为1
        vo.setStatus(CoreConst.STATUS_VALID);
        //分页方法入参：页码，每页数据量
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        //按条件（部分可空）查询文章信息（点赞评论浏览，，，）列表
        List<BizArticle> articleList =  bizArticleService.findByCondition(vo);
        //pageInfo包装分页查询
        PageInfo<BizArticle> pageInfo = new PageInfo<>(articleList);
        // 属性名相同的情况下的把pageInfo的属性赋值到pageVo中，保留PageInfo有用的属性与前台交互
        PageVo pageVo = CopyUtil.getCopy(pageInfo,PageVo.class);
        model.addAttribute("page",JSONObject.toJSON(pageVo));
        model.addAttribute("articleList",articleList);//文章列表
    }

    /**
     * 主页显示
     * @param model
     * @param vo
     * @return
     */
    @GetMapping("/")
    public String index(Model model, ArticleConditionVo vo){
        model.addAttribute("pageUrl","blog/index");
        model.addAttribute("categoryId","index");
        model.addAttribute("sliderList",bizArticleService.sliderList());
        loadMainPage(model,vo);
        log.info("名字" + THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index");
        //客户端每一个功能按照主题名字返回路径，查询次数多，存入redis缓存中
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }

    /**
     * 点击页码翻页
     * @param pageNumber 参数绑定
     * @param vo
     * @param model
     * @return
     */
    @RequestMapping("/blog/index/{pageNumber}")
    public String index(@PathVariable("pageNumber") Integer pageNumber, ArticleConditionVo vo, Model model) {
        vo.setPageNumber(pageNumber);
        model.addAttribute("pageUrl","blog/index");
        model.addAttribute("categoryId","index");
        loadMainPage(model, vo);
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }
    /**
     * 点击分类阅览文章
     *
     * @param categoryId
     * @param model
     * @return
     */
    @GetMapping("/blog/category/{categoryId}")
    public String category(@PathVariable("categoryId") Integer categoryId, Model model) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setCategoryId(categoryId);
        model.addAttribute("pageUrl", "blog/category/" + categoryId);
        model.addAttribute("categoryId",categoryId);
        loadMainPage(model, vo);
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }

    /**
     * 在分类中点击页码翻页
     * @param categoryId
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/blog/category/{categoryId}/{pageNumber}")
    public String category(@PathVariable("categoryId") Integer categoryId, @PathVariable("pageNumber") Integer pageNumber, Model model) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setCategoryId(categoryId);
        vo.setPageNumber(pageNumber);
        model.addAttribute("pageUrl", "blog/category/" + categoryId);
        model.addAttribute("categoryId",categoryId);
        loadMainPage(model, vo);
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }


    /**
     * 点击标签阅览文章
     *
     * @param tagId
     * @param model
     * @return
     */
    @GetMapping("/blog/tag/{tagId}")
    public String tag(@PathVariable("tagId") Integer tagId, Model model) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setTagId(tagId);
        model.addAttribute("pageUrl", "blog/tag/" + tagId);
        loadMainPage(model,vo);
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }

    /**
     * 在标签中点击页码翻页
     *
     * @param tagId
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/blog/tag/{tagId}/{pageNumber}")
    public String tag(@PathVariable("tagId") Integer tagId, @PathVariable("pageNumber") Integer pageNumber, Model model) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setTagId(tagId);
        vo.setPageNumber(pageNumber);
        model.addAttribute("pageUrl", "blog/tag/" + tagId);
        loadMainPage(model,vo);
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/index";
    }

    /**
     * 文章详情
     *
     * @param model
     * @param articleId
     * @return
     */
    @GetMapping("/blog/article/{articleId}")
    public String article(HttpServletRequest request, Model model, @PathVariable("articleId") Integer articleId) {
        BizArticle article = bizArticleService.selectById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("categoryId",article.getCategoryId());
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/article";
    }

    /**
     * 评论页
     *
     * @param model
     * @return
     */
    @GetMapping("/blog/comment")
    public String comment(Model model) {
        model.addAttribute("categoryId","comment");
        return THEME_PREFIX + bizThemeService.selectCurrent().getName()+ "/comment";
    }
}
