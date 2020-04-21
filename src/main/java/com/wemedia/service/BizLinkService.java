package com.wemedia.service;

import com.wemedia.model.BizLink;

import java.util.List;

public interface BizLinkService extends BaseService<BizLink> {
    List<BizLink> selectLinks(BizLink bizLink);

    int deleteBatch(Integer[] ids);

}
