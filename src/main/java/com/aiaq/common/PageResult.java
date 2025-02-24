package com.aiaq.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/12 9:41
 * @Description 分页结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 总数
     */
    private long total;

    /**
     * 每页显示条数，默认 10
     */
    private long size;

    public PageResult(Page<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.size = page.getSize();
    }
}
