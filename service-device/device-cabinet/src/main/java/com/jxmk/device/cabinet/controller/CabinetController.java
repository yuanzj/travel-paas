package com.jxmk.device.cabinet.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.common.core.util.R;
import com.jxmk.device.cabinet.entity.Cabinet;
import com.jxmk.device.cabinet.service.CabinetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cabinet")
public class CabinetController {

    private final CabinetService cabinetService;

    @GetMapping("/{id}")
    public R<Cabinet> getById(@PathVariable("id") Long id) {
        return R.ok(cabinetService.getById(id));
    }

    @GetMapping("/page")
    public R<IPage<Cabinet>> page(Page<Cabinet> page) {
        return R.ok(cabinetService.page(page));
    }

    @PostMapping
    public R<Boolean> save(@RequestBody Cabinet cabinet) {
        return R.ok(cabinetService.save(cabinet));
    }

    @PutMapping
    public R<Boolean> update(@RequestBody Cabinet cabinet) {
        return R.ok(cabinetService.updateById(cabinet));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(cabinetService.removeById(id));
    }
} 