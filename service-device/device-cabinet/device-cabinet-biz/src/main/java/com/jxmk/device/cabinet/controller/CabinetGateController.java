package com.jxmk.device.cabinet.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.common.core.util.R;
import com.jxmk.device.cabinet.api.entity.CabinetGate;
import com.jxmk.device.cabinet.service.CabinetGateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cabinet/gate")
public class CabinetGateController {

    private final CabinetGateService cabinetGateService;

    @GetMapping("/{id}")
    public R<CabinetGate> getById(@PathVariable("id") Long id) {
        return R.ok(cabinetGateService.getById(id));
    }

    @GetMapping("/page")
    public R<IPage<CabinetGate>> page(Page<CabinetGate> page) {
        return R.ok(cabinetGateService.page(page));
    }

    @PostMapping
    public R<Boolean> save(@RequestBody CabinetGate cabinetGate) {
        return R.ok(cabinetGateService.save(cabinetGate));
    }

    @PutMapping
    public R<Boolean> update(@RequestBody CabinetGate cabinetGate) {
        return R.ok(cabinetGateService.updateById(cabinetGate));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(cabinetGateService.removeById(id));
    }
} 