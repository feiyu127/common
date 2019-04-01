package com.feiyu.common.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.feiyu.common.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:30
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    @Autowired
    protected BaseMapper<T> baseMapper;

    @Override
    public int insert(T entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int updateById(T entity) {
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteById(Serializable id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public T selectById(Serializable id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> map) {
        return baseMapper.selectByMap(map);
    }

    @Override
    public List<T> selectByEntity(T entity) {
        return baseMapper.selectList(new EntityWrapper(entity));
    }

    @Override
    public Page<T> selectPageByEntity(T entity) {
        Page<T> page = generatePage(entity);
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<T> list = baseMapper.selectList(new EntityWrapper(entity));
        page.setRecords(list);
        page.setTotal(PageHelper.getTotal());
        PageHelper.freeTotal();
        return page;
    }

    private static final String pageNum = "page";
    private static final String pageSize = "limit";

    private <E> Page<E> generatePage(E entity) {
        Page<E> page = new Page<>();
        Class<?> aClass = entity.getClass();
        if (ReflectUtils.checkFieldIsExist(aClass, pageNum)) {
            page.setCurrent((int) ReflectUtils.getValue(entity, pageNum));
        }
        if (ReflectUtils.checkFieldIsExist(aClass, pageSize)) {
            page.setSize((int) ReflectUtils.getValue(entity, pageSize));
        }
        return page;
    }
}
