package com.hegp.controller;

import com.hegp.controller.common.BaseValidate;
import com.hegp.core.exception.ResourcesNotFoundException;
import com.hegp.core.model.Result;
import com.hegp.entity.SqlTemplate;
import com.hegp.model.SqlTemplateDTO;
import com.hegp.service.SqlTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * @author hgp
 * @date 20-5-20
 */
@RestController
@RequestMapping("/v1")
public class SqlTemplateController extends BaseValidate {
    @Autowired
    private SqlTemplateService sqlTemplateService;

    // 只添加几个字段 name, parentId
    // 在树状结构添加节点
    @PostMapping("/sql-template/tree")
    public Result creteTreeItem(@RequestBody SqlTemplateDTO sqlTemplateDTO) {
        checkParams(sqlTemplateDTO, "name");
        SqlTemplate sqlTemplate = new SqlTemplate();
        sqlTemplate.setParentId(sqlTemplateDTO.getParentId());
        sqlTemplate.setName(sqlTemplateDTO.getName());
        sqlTemplate.setUseMock(false);
        sqlTemplateService.save(sqlTemplate);
        return Result.success(sqlTemplate.getId());
    }

    @PostMapping("/sql-template/{id}/move")
    public Result sqlTemplateMove(@PathVariable String id, @RequestBody SqlTemplateDTO sqlTemplateDTO) {
        SqlTemplate sqlTemplate = Optional.of(sqlTemplateService.find(id)).orElseThrow(new ResourcesNotFoundException("该数据不存在或者已删除, 请刷新整个页面"));
        sqlTemplateService.save(sqlTemplate);
        return Result.success(sqlTemplate.getId());
    }

    // 在树状结构添加节点
    @PutMapping("/sql-template/{id}")
    public Result editSqlTemplate(@PathVariable String id, @RequestBody SqlTemplateDTO sqlTemplateDTO) {
        SqlTemplate sqlTemplate = Optional.of(sqlTemplateService.find(id)).orElseThrow(new ResourcesNotFoundException("该数据不存在或者已删除, 请刷新整个页面"));
        checkParams(sqlTemplateDTO, "url");
        // 校验URL是否重复
        sqlTemplate.setUseMock(sqlTemplateDTO.getUseMock());
        sqlTemplateService.save(sqlTemplate);
        return Result.success(sqlTemplate.getId());
    }

    // 在树状结构添加节点
    @GetMapping("/sql-template/{id}")
    public Result<SqlTemplateDTO> querySqlTemplateDetail(@PathVariable String id) {
        SqlTemplate sqlTemplate = Optional.of(sqlTemplateService.find(id)).orElseThrow(new ResourcesNotFoundException("该数据不存在或者已删除, 请刷新整个页面"));
        SqlTemplateDTO sqlTemplateDTO = new SqlTemplateDTO();
        BeanUtils.copyProperties(sqlTemplate, sqlTemplateDTO, "params", "mockData");
        return Result.success(sqlTemplateDTO);
    }

    // 在树状结构添加节点
    @GetMapping("/sql-template/{id}/{action}")
    public Result<SqlTemplate> querySqlTemplateDetail(@PathVariable String id, @PathVariable String action) {
        SqlTemplate sqlTemplate = Optional.of(sqlTemplateService.find(id)).orElseThrow(new ResourcesNotFoundException("该数据不存在或者已删除, 请刷新整个页面"));
        if ("small".equals(action)) {
            return Result.success(sqlTemplateService.queryLessOne(sqlTemplate));
        }
        return Result.success(sqlTemplateService.queryGreaterOne(sqlTemplate));
    }

    /**
     * 返回list集合,然后ztree自动封装list成树状结构
     * @return
     */
    @GetMapping("/sql-template/list")
    public Result<Map> queryList() {
        return Result.success(sqlTemplateService.queryTreeListData());
    }

    /**
     * 返回list集合,然后ztree自动封装list成树状结构
     * @return
     */
    @GetMapping("/test")
    public Result testtest() {
        return Result.success();
    }
}
