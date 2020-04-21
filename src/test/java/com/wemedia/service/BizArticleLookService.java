package com.wemedia.service;

import com.wemedia.model.BizArticleLook;

import java.util.Date;

public interface BizArticleLookService extends BaseService<BizArticleLook> {
    int checkArticleLook(Integer articleId, String userIp, Date lookTime);
}
