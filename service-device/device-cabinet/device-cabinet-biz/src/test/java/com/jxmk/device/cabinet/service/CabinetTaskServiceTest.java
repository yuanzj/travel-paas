package com.jxmk.device.cabinet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxmk.device.cabinet.api.entity.CabinetTask;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CabinetTaskServiceTest {

    @Resource
    private CabinetTaskService cabinetTaskService;

    @Test
    void testSave() {
        CabinetTask cabinetTask = new CabinetTask();
        cabinetTask.setTaskType(1);
        cabinetTask.setTaskStatus(0);
        cabinetTask.setTxnNo("TXN001");
        cabinetTask.setCabinetSn("TEST001");
        cabinetTask.setPutGateNo(1);
        cabinetTask.setRequestId("REQ001");
        cabinetTask.setRequestUserId("1");
        cabinetTask.setRequestBatteryVoltage(new BigDecimal("48.0"));
        cabinetTask.setCreateTime(LocalDateTime.now());
        cabinetTask.setUpdateTime(LocalDateTime.now());

        assertTrue(cabinetTaskService.save(cabinetTask));
        assertNotNull(cabinetTask.getTaskId());
    }

    @Test
    void testGetById() {
        // 先保存一条数据
        CabinetTask cabinetTask = new CabinetTask();
        cabinetTask.setTaskType(1);
        cabinetTask.setTaskStatus(0);
        cabinetTask.setTxnNo("TXN002");
        cabinetTask.setCabinetSn("TEST002");
        cabinetTask.setPutGateNo(1);
        cabinetTask.setRequestId("REQ002");
        cabinetTask.setRequestUserId("2");
        cabinetTask.setRequestBatteryVoltage(new BigDecimal("48.0"));
        cabinetTask.setCreateTime(LocalDateTime.now());
        cabinetTask.setUpdateTime(LocalDateTime.now());
        cabinetTaskService.save(cabinetTask);

        // 测试查询
        CabinetTask found = cabinetTaskService.getById(cabinetTask.getTaskId());
        assertNotNull(found);
        assertEquals("TXN002", found.getTxnNo());
        assertEquals("TEST002", found.getCabinetSn());
    }

    @Test
    void testPage() {
        // 先保存两条数据
        for (int i = 1; i <= 2; i++) {
            CabinetTask cabinetTask = new CabinetTask();
            cabinetTask.setTaskType(1);
            cabinetTask.setTaskStatus(0);
            cabinetTask.setTxnNo("TXN00" + i);
            cabinetTask.setCabinetSn("TEST00" + i);
            cabinetTask.setPutGateNo(i);
            cabinetTask.setRequestId("REQ00" + i);
            cabinetTask.setRequestUserId("1");
            cabinetTask.setRequestBatteryVoltage(new BigDecimal("48.0"));
            cabinetTask.setCreateTime(LocalDateTime.now());
            cabinetTask.setUpdateTime(LocalDateTime.now());
            cabinetTaskService.save(cabinetTask);
        }

        // 测试分页查询
        Page<CabinetTask> page = new Page<>(1, 2);
        IPage<CabinetTask> pageResult = cabinetTaskService.page(page);
        
        assertNotNull(pageResult);
        assertTrue(pageResult.getTotal() >= 2);
        assertEquals(2, pageResult.getSize());
    }

    @Test
    void testUpdate() {
        // 先保存一条数据
        CabinetTask cabinetTask = new CabinetTask();
        cabinetTask.setTaskType(1);
        cabinetTask.setTaskStatus(0);
        cabinetTask.setTxnNo("TXN003");
        cabinetTask.setCabinetSn("TEST003");
        cabinetTask.setPutGateNo(1);
        cabinetTask.setRequestId("REQ003");
        cabinetTask.setRequestUserId("3");
        cabinetTask.setRequestBatteryVoltage(new BigDecimal("48.0"));
        cabinetTask.setCreateTime(LocalDateTime.now());
        cabinetTask.setUpdateTime(LocalDateTime.now());
        cabinetTaskService.save(cabinetTask);

        // 测试更新
        cabinetTask.setTaskStatus(1);
        assertTrue(cabinetTaskService.updateById(cabinetTask));

        CabinetTask updated = cabinetTaskService.getById(cabinetTask.getTaskId());
        assertEquals(1, updated.getTaskStatus());
    }

    @Test
    void testDelete() {
        // 先保存一条数据
        CabinetTask cabinetTask = new CabinetTask();
        cabinetTask.setTaskType(1);
        cabinetTask.setTaskStatus(0);
        cabinetTask.setTxnNo("TXN004");
        cabinetTask.setCabinetSn("TEST004");
        cabinetTask.setPutGateNo(1);
        cabinetTask.setRequestId("REQ004");
        cabinetTask.setRequestUserId("4");
        cabinetTask.setRequestBatteryVoltage(new BigDecimal("48.0"));
        cabinetTask.setCreateTime(LocalDateTime.now());
        cabinetTask.setUpdateTime(LocalDateTime.now());
        cabinetTaskService.save(cabinetTask);

        // 测试删除
        assertTrue(cabinetTaskService.removeById(cabinetTask.getTaskId()));
        assertNull(cabinetTaskService.getById(cabinetTask.getTaskId()));
    }
} 