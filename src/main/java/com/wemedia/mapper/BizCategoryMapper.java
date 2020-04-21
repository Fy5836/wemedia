package com.wemedia.mapper;

import com.wemedia.model.BizCategory;
import com.wemedia.util.MyMapper;

import java.util.List;

public interface BizCategoryMapper extends MyMapper<BizCategory> {

    List<BizCategory> selectCategories(BizCategory bizCategory);

    int deleteBatch(Integer[] ids);

    BizCategory selectById(Integer id);
}
