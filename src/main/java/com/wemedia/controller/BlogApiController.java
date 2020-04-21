package com.wemedia.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wemedia.model.BizArticleLook;
import com.wemedia.model.BizComment;
import com.wemedia.model.BizLove;
import com.wemedia.service.BizArticleLookService;
import com.wemedia.service.BizCommentService;
import com.wemedia.service.BizLoveService;
import com.wemedia.util.*;
import com.wemedia.vo.CommentConditionVo;
import com.wemedia.vo.base.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("blog/api")
public class BlogApiController {
    @Autowired
    private BizCommentService commentService;
    @Autowired
    private BizArticleLookService articleLookService;
    @Autowired
    private BizLoveService loveService;

    @PostMapping("comments")
    public PageInfo<BizComment> getComments(CommentConditionVo vo){
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<BizComment> comments = commentService.selectComments(vo);
        //分页封装评论集合
        PageInfo<BizComment> pages = new PageInfo<>(comments);
        return pages;
    }

    @PostMapping("comment/save")
    public ResponseVo saveComment(HttpServletRequest request, BizComment comment) throws UnsupportedEncodingException {
        //后台判空
        if (org.springframework.util.StringUtils.isEmpty(comment.getNickname())) {
            return ResultUtil.error("请输入昵称");
        }
        //保存评论创建时间，更新时间，和评论人ip
        String content = comment.getContent();
        Date date = new Date();
        comment.setContent(content);
        comment.setIp(IpUtil.getIpAddr(request));
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        //通过qq号获得qq头像
        if(StringUtils.isNotBlank(comment.getQq())){
            comment.setAvatar("http://q1.qlogo.cn/g?b=qq&nk="+comment.getQq()+"&s=100");
        }
        //插入数据库,只对有值的添加
        int a = commentService.insertSelective(comment);
        if(a>0){
            return ResultUtil.success("评论提交成功,系统正在审核");
        }else{
            return ResultUtil.error("评论提交失败");
        }
    };
    @PostMapping("article/look")
    public ResponseVo checkLook(HttpServletRequest request, Integer articleId){
        /*浏览次数*/
        Date date = new Date();
        //获取用户ip
        String ip = IpUtil.getIpAddr(request);
        //count(0) 通过文章id，用户ip，lookTime>当前时间1h前的时间,没到1h不增加， 判断浏览量是否增加
        int checkCount = articleLookService.checkArticleLook(articleId, ip, DateUtil.addHours(date,-1));
        if(checkCount==0){
            BizArticleLook articleLook = new BizArticleLook();
            articleLook.setArticleId(articleId);
            articleLook.setUserIp(ip);
            //设置最新浏览时间
            articleLook.setLookTime(date);
            articleLook.setCreateTime(date);
            articleLook.setUpdateTime(date);
            articleLookService.insert(articleLook);
            return ResultUtil.success("浏览次数+1");
        }else{
            return ResultUtil.error("一小时内重复浏览不增加次数哦");
        }
    };
    @PostMapping("love")
    public ResponseVo love(HttpServletRequest request, Integer bizId, Integer bizType){
        Date date = new Date();
        //获取用户ip
        String ip = IpUtil.getIpAddr(request);
        //查询用户ip和赞id，根据查询是否为空判定是否点赞
        BizLove bizLove = loveService.checkLove(bizId, ip);
        if (bizLove == null) {
            bizLove=new BizLove();
            bizLove.setBizId(bizId);
            //点赞类型，1：文章点赞  2：留言点赞
            bizLove.setBizType(bizType);
            bizLove.setUserIp(ip);
            bizLove.setStatus(CoreConst.STATUS_VALID);
            bizLove.setCreateTime(date);
            bizLove.setUpdateTime(date);
            loveService.insert(bizLove);
            return ResultUtil.success("点赞成功");
        }else{
            return ResultUtil.error("您已赞过了哦~");
        }
    }

}
