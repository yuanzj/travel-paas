package com.jxmk.device.cabinet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.device.cabinet.api.entity.Cabinet;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CabinetServiceTest {

    @Resource
    private CabinetService cabinetService;

    @Test
    void testSave() {
        Cabinet cabinet = new Cabinet();
        cabinet.setCabinetSn("TEST001");
        cabinet.setCabinetName("测试电柜");
        cabinet.setCabinetStatus(0);
        cabinet.setCabinetProtocol(1);
        cabinet.setCreateTime(LocalDateTime.now());
        cabinet.setUpdateTime(LocalDateTime.now());
        cabinet.setDelFlag("0");

        assertTrue(cabinetService.save(cabinet));
        assertNotNull(cabinet.getCabinetId());
    }

    @Test
    void testGetById() {
        // 先保存一条数据
        Cabinet cabinet = new Cabinet();
        cabinet.setCabinetSn("TEST002");
        cabinet.setCabinetName("测试电柜2");
        cabinet.setCabinetStatus(0);
        cabinet.setCabinetProtocol(1);
        cabinet.setCreateTime(LocalDateTime.now());
        cabinet.setUpdateTime(LocalDateTime.now());
        cabinet.setDelFlag("0");
        cabinetService.save(cabinet);

        // 测试查询
        Cabinet found = cabinetService.getById(cabinet.getCabinetId());
        assertNotNull(found);
        assertEquals("TEST002", found.getCabinetSn());
    }

    @Test
    void testPage() {
        // 先保存两条数据
        for (int i = 1; i <= 2; i++) {
            Cabinet cabinet = new Cabinet();
            cabinet.setCabinetSn("TEST00" + i);
            cabinet.setCabinetName("测试电柜" + i);
            cabinet.setCabinetStatus(0);
            cabinet.setCabinetProtocol(1);
            cabinet.setCreateTime(LocalDateTime.now());
            cabinet.setUpdateTime(LocalDateTime.now());
            cabinet.setDelFlag("0");
            cabinetService.save(cabinet);
        }

        // 测试分页查询
        Page<Cabinet> page = new Page<>(1, 2);
        IPage<Cabinet> pageResult = cabinetService.page(page);
        
        assertNotNull(pageResult);
        assertTrue(pageResult.getTotal() >= 2);
        assertEquals(2, pageResult.getSize());
    }

    @Test
    void testUpdate() {
        // 先保存一条数据
        Cabinet cabinet = new Cabinet();
        cabinet.setCabinetSn("TEST003");
        cabinet.setCabinetName("测试电柜3");
        cabinet.setCabinetStatus(0);
        cabinet.setCabinetProtocol(1);
        cabinet.setCreateTime(LocalDateTime.now());
        cabinet.setUpdateTime(LocalDateTime.now());
        cabinet.setDelFlag("0");
        cabinetService.save(cabinet);

        // 测试更新
        cabinet.setCabinetName("更新后的电柜名称");
        assertTrue(cabinetService.updateById(cabinet));

        Cabinet updated = cabinetService.getById(cabinet.getCabinetId());
        assertEquals("更新后的电柜名称", updated.getCabinetName());
    }

    @Test
    void testDelete() {
        // 先保存一条数据
        Cabinet cabinet = new Cabinet();
        cabinet.setCabinetSn("TEST004");
        cabinet.setCabinetName("测试电柜4");
        cabinet.setCabinetStatus(0);
        cabinet.setCabinetProtocol(1);
        cabinet.setCreateTime(LocalDateTime.now());
        cabinet.setUpdateTime(LocalDateTime.now());
        cabinet.setDelFlag("0");
        cabinetService.save(cabinet);

        // 测试删除
        assertTrue(cabinetService.removeById(cabinet.getCabinetId()));
        assertNull(cabinetService.getById(cabinet.getCabinetId()));
    }
} 