package com.wemedia.mapper;

import com.wemedia.model.BizTags;
import com.wemedia.util.MyMapper;

import java.util.List;

public interface BizTagsMapper extends MyMapper<BizTags> {

    List<BizTags> selectTags(BizTags bizTags);

    int deleteBatch(Integer[] ids);
}
