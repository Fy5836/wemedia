package com.wemedia.service;

import com.wemedia.model.BizComment;
import com.wemedia.vo.CommentConditionVo;

import java.util.List;

public interface BizCommentService extends BaseService<BizComment> {
    List<BizComment> selectComments(CommentConditionVo vo);

    int deleteBatch(Integer[] ids);

}
