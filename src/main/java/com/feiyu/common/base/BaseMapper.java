package com.feiyu.common.base;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:27
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

    /**
     * 批量添加数据列表
     * @param list 数据列
     * @return
     */
    int batchInsert(Collection<T> list);
}
