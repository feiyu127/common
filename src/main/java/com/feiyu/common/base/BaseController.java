package com.feiyu.common.base;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:38
 */
public class BaseController<T> {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected BaseService<T> baseService;

    @PostMapping("insert")
    @ResponseBody
    protected ApiResult insert(T entity) {
        int insert = baseService.insert(entity);
        return ApiResult.buildCondition(insert > 0, entity, "insert error");
    }

    @PostMapping("updateById")
    @ResponseBody
    protected ApiResult updateById(T entity) {
        int updateNum = baseService.updateById(entity);
        return ApiResult.buildCondition(updateNum > 0, entity, "update error");
    }

    @PostMapping("deleteById")
    @ResponseBody
    protected ApiResult deleteById(Serializable id) {
        int deleteNum = baseService.deleteById(id);
        return ApiResult.buildCondition(deleteNum > 0, "delete success", "delete error");
    }

    @GetMapping("selectById")
    @ResponseBody
    protected ApiResult selectById(Serializable id) {
        T entity = baseService.selectById(id);
        return ApiResult.buildCondition(entity != null, entity, "data not exists");
    }

    @GetMapping("selectPageByEntity")
    @ResponseBody
    protected ApiResult selectPageByEntity(T entity) {
        Page<T> page = baseService.selectPageByEntity(entity);
        return ApiResult.buildCondition(page != null, page, "get Page Data error");
    }

}
