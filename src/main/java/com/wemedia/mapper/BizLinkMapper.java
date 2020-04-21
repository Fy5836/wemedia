package com.wemedia.mapper;

import com.wemedia.model.BizLink;
import com.wemedia.util.MyMapper;

import java.util.List;

public interface BizLinkMapper extends MyMapper<BizLink> {

    List<BizLink> selectLinks(BizLink bizLink);

    int deleteBatch(Integer[] ids);

}
