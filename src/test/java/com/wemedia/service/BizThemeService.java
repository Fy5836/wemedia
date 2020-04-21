package com.wemedia.service;

import com.wemedia.model.BizTheme;

public interface BizThemeService  extends BaseService<BizTheme> {
    int useTheme(Integer id);

    BizTheme selectCurrent();

    int deleteBatch(Integer[] ids);

}
