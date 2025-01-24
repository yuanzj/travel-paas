package com.jxmk.device.cabinet.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.common.core.util.R;
import com.jxmk.device.cabinet.dto.CabinetImport;
import com.jxmk.device.cabinet.dto.CabinetOperateRequest;
import com.jxmk.device.cabinet.dto.CabinetOperateResponse;
import com.jxmk.device.cabinet.entity.Cabinet;
import com.jxmk.device.cabinet.service.CabinetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;

import java.io.IOException;
import java.util.List;

@Slf4j
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

    /**
     * 取电池
     */
    @PostMapping("/get-battery")
    public R<CabinetOperateResponse> getBattery(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.getBattery(request));
    }

    /**
     * 换电
     */
    @PostMapping("/exchange-battery")
    public R<CabinetOperateResponse> exchangeBattery(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.exchangeBattery(request));
    }

    /**
     * 归还电池
     */
    @PostMapping("/return-battery")
    public R<CabinetOperateResponse> returnBattery(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.returnBattery(request));
    }

    /**
     * 电柜启用
     */
    @PostMapping("/enable")
    public R<CabinetOperateResponse> enableCabinet(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.enableCabinet(request));
    }

    /**
     * 电柜禁用
     */
    @PostMapping("/disable")
    public R<CabinetOperateResponse> disableCabinet(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.disableCabinet(request));
    }

    /**
     * 电柜开门
     */
    @PostMapping("/open")
    public R<CabinetOperateResponse> openCabinet(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.openCabinet(request));
    }

    /**
     * 电柜仓门启用
     */
    @PostMapping("/gate/enable")
    public R<CabinetOperateResponse> enableGate(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.enableGate(request));
    }

    /**
     * 电柜仓门禁用
     */
    @PostMapping("/gate/disable")
    public R<CabinetOperateResponse> disableGate(@RequestBody CabinetOperateRequest request) {
        return R.ok(cabinetService.disableGate(request));
    }

    @PostMapping("/import")
    public R<String> importCabinet(@RequestPart("file") MultipartFile file) throws IOException {
        List<CabinetImport> list = EasyExcel.read(file.getInputStream())
                .head(CabinetImport.class)
                .sheet()
                .doReadSync();
                
        cabinetService.importCabinet(list);
        return R.ok("导入成功");
    }
} 