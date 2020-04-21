package com.wemedia.mapper;

import com.wemedia.model.BizComment;
import com.wemedia.util.MyMapper;
import com.wemedia.vo.CommentConditionVo;

import java.util.List;

public interface BizCommentMapper extends MyMapper<BizComment> {

    /**
     * 分页查询
     *
     * @param vo
     * @return
     */
    List<BizComment> selectComments(CommentConditionVo vo);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);
}
