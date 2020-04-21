package com.wemedia.mapper;

import com.wemedia.model.BizLove;
import com.wemedia.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface BizLoveMapper extends MyMapper<BizLove> {
    BizLove checkLove(@Param("bizId") Integer bizId, @Param("userIp") String userIp);
}
