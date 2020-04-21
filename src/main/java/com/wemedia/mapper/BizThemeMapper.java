package com.wemedia.mapper;

import com.wemedia.model.BizTheme;
import com.wemedia.util.MyMapper;

public interface BizThemeMapper extends MyMapper<BizTheme> {
    int setInvaid();
    int updateStatusById(Integer id);
    int deleteBatch(Integer[] ids);
}