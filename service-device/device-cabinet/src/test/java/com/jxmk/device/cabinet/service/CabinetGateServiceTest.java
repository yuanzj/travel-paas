package com.jxmk.device.cabinet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.device.cabinet.entity.CabinetGate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CabinetGateServiceTest {

    @Resource
    private CabinetGateService cabinetGateService;

    @Test
    void testSave() {
        CabinetGate cabinetGate = new CabinetGate();
        cabinetGate.setCabinetSn("TEST001");
        cabinetGate.setGateNo(1);
        cabinetGate.setGateStatus(0);
        cabinetGate.setGateProhibit(0);
        cabinetGate.setGateStatusTime(LocalDateTime.now());
        cabinetGate.setBatteryStatus(0);
        cabinetGate.setBatteryLock(0);
        cabinetGate.setCreateTime(LocalDateTime.now());
        cabinetGate.setUpdateTime(LocalDateTime.now());
        cabinetGate.setDelFlag("0");

        assertTrue(cabinetGateService.save(cabinetGate));
        assertNotNull(cabinetGate.getCabinetGateId());
    }

    @Test
    void testGetById() {
        // 先保存一条数据
        CabinetGate cabinetGate = new CabinetGate();
        cabinetGate.setCabinetSn("TEST002");
        cabinetGate.setGateNo(1);
        cabinetGate.setGateStatus(0);
        cabinetGate.setGateProhibit(0);
        cabinetGate.setGateStatusTime(LocalDateTime.now());
        cabinetGate.setBatteryStatus(0);
        cabinetGate.setBatteryLock(0);
        cabinetGate.setCreateTime(LocalDateTime.now());
        cabinetGate.setUpdateTime(LocalDateTime.now());
        cabinetGate.setDelFlag("0");
        cabinetGateService.save(cabinetGate);

        // 测试查询
        CabinetGate found = cabinetGateService.getById(cabinetGate.getCabinetGateId());
        assertNotNull(found);
        assertEquals("TEST002", found.getCabinetSn());
        assertEquals(1, found.getGateNo());
    }

    @Test
    void testPage() {
        // 先保存两条数据
        for (int i = 1; i <= 2; i++) {
            CabinetGate cabinetGate = new CabinetGate();
            cabinetGate.setCabinetSn("TEST00" + i);
            cabinetGate.setGateNo(i);
            cabinetGate.setGateStatus(0);
            cabinetGate.setGateProhibit(0);
            cabinetGate.setGateStatusTime(LocalDateTime.now());
            cabinetGate.setBatteryStatus(0);
            cabinetGate.setBatteryLock(0);
            cabinetGate.setCreateTime(LocalDateTime.now());
            cabinetGate.setUpdateTime(LocalDateTime.now());
            cabinetGate.setDelFlag("0");
            cabinetGateService.save(cabinetGate);
        }

        // 测试分页查询
        Page<CabinetGate> page = new Page<>(1, 2);
        IPage<CabinetGate> pageResult = cabinetGateService.page(page);
        
        assertNotNull(pageResult);
        assertTrue(pageResult.getTotal() >= 2);
        assertEquals(2, pageResult.getSize());
    }

    @Test
    void testUpdate() {
        // 先保存一条数据
        CabinetGate cabinetGate = new CabinetGate();
        cabinetGate.setCabinetSn("TEST003");
        cabinetGate.setGateNo(1);
        cabinetGate.setGateStatus(0);
        cabinetGate.setGateProhibit(0);
        cabinetGate.setGateStatusTime(LocalDateTime.now());
        cabinetGate.setBatteryStatus(0);
        cabinetGate.setBatteryLock(0);
        cabinetGate.setCreateTime(LocalDateTime.now());
        cabinetGate.setUpdateTime(LocalDateTime.now());
        cabinetGate.setDelFlag("0");
        cabinetGateService.save(cabinetGate);

        // 测试更新
        cabinetGate.setGateStatus(1);
        assertTrue(cabinetGateService.updateById(cabinetGate));

        CabinetGate updated = cabinetGateService.getById(cabinetGate.getCabinetGateId());
        assertEquals(1, updated.getGateStatus());
    }

    @Test
    void testDelete() {
        // 先保存一条数据
        CabinetGate cabinetGate = new CabinetGate();
        cabinetGate.setCabinetSn("TEST004");
        cabinetGate.setGateNo(1);
        cabinetGate.setGateStatus(0);
        cabinetGate.setGateProhibit(0);
        cabinetGate.setGateStatusTime(LocalDateTime.now());
        cabinetGate.setBatteryStatus(0);
        cabinetGate.setBatteryLock(0);
        cabinetGate.setCreateTime(LocalDateTime.now());
        cabinetGate.setUpdateTime(LocalDateTime.now());
        cabinetGate.setDelFlag("0");
        cabinetGateService.save(cabinetGate);

        // 测试删除
        assertTrue(cabinetGateService.removeById(cabinetGate.getCabinetGateId()));
        assertNull(cabinetGateService.getById(cabinetGate.getCabinetGateId()));
    }
} 