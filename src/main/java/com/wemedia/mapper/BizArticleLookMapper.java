package com.wemedia.mapper;

import com.wemedia.model.BizArticleLook;
import com.wemedia.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BizArticleLookMapper extends MyMapper<BizArticleLook> {

    int checkArticleLook(@Param("articleId") Integer articleId, @Param("userIp") String userIp, @Param("lookTime") Date lookTime);
}
