package com.wemedia.service;

import com.wemedia.model.BizTags;

import java.util.List;

public interface BizTagsService extends BaseService<BizTags>{
    List<BizTags> selectTags(BizTags bizTags);

    int deleteBatch(Integer[] ids);
}
