package com.wemedia.service;

import com.wemedia.model.BizLove;

/**
 * Created by Administrator on 2018-9-4.
 */
public interface BizLoveService extends BaseService<BizLove> {
    BizLove checkLove(Integer bizId, String userIp);
}
