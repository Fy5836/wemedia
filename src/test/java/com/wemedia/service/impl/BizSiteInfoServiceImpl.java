package com.wemedia.service.impl;

import com.wemedia.mapper.BizArticleMapper;
import com.wemedia.service.BizSiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BizSiteInfoServiceImpl implements BizSiteInfoService {
    @Autowired
    private BizArticleMapper bizArticleMapper;
    @Override
    public Map<String, Object> getSiteInfo() {
        //对应数量的名字在mapper层中已经被命名as后  在前端直接取
        Map<String, Object> map = bizArticleMapper.getSiteInfo();
        return map;
    }
}
