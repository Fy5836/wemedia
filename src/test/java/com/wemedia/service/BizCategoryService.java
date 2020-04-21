package com.wemedia.service;

import com.wemedia.model.BizCategory;

import java.util.List;

public interface BizCategoryService extends BaseService<BizCategory> {

    List<BizCategory> selectCategories(BizCategory bizCategory);
    int deleteBatch(Integer[] ids);
    BizCategory selectById(Integer id);
    List<BizCategory> selectByPid(Integer pid);

}
