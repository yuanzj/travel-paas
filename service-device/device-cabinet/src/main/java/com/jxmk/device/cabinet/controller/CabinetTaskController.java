package com.jxmk.device.cabinet.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.common.core.util.R;
import com.jxmk.device.cabinet.entity.CabinetTask;
import com.jxmk.device.cabinet.service.CabinetTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cabinet/task")
public class CabinetTaskController {

    private final CabinetTaskService cabinetTaskService;

    @GetMapping("/{id}")
    public R<CabinetTask> getById(@PathVariable("id") Long id) {
        return R.ok(cabinetTaskService.getById(id));
    }

    @GetMapping("/page")
    public R<IPage<CabinetTask>> page(Page<CabinetTask> page) {
        return R.ok(cabinetTaskService.page(page));
    }

    @PostMapping
    public R<Boolean> save(@RequestBody CabinetTask cabinetTask) {
        return R.ok(cabinetTaskService.save(cabinetTask));
    }

    @PutMapping
    public R<Boolean> update(@RequestBody CabinetTask cabinetTask) {
        return R.ok(cabinetTaskService.updateById(cabinetTask));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(cabinetTaskService.removeById(id));
    }
} 