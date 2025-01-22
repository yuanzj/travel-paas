package com.jxmk.common.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jxmk.common.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * MybatisPlus 自动填充配置
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.debug("mybatis plus start insert fill ....");
		LocalDateTime now = LocalDateTime.now();

		fillValIfNullByName("createTime", now, metaObject);
		fillValIfNullByName("updateTime", now, metaObject);
		fillValIfNullByName("createBy", getUserName(), metaObject);
		fillValIfNullByName("updateBy", getUserName(), metaObject);

		// 删除标记自动填充
		fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.debug("mybatis plus start update fill ....");
		fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject);
		fillValIfNullByName("updateBy", getUserName(), metaObject);
	}

	/**
	 * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
	 *
	 * @param fieldName  属性名
	 * @param fieldVal   属性值
	 * @param metaObject MetaObject
	 */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject) {
		// 0. 如果填充值为空
		if (fieldVal == null) {
			return;
		}

		// 1. 没有 set 方法
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 如果用户有手动设置的值
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		StrUtil.isNotBlank(setValueStr);
		// 3. field 类型相同时设置
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	/**
	 * 获取 spring security 当前的用户名
	 *
	 * @return 当前用户名
	 */
	private String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 匿名接口直接返回
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}

		if (Optional.ofNullable(authentication).isPresent()) {
			return authentication.getName();
		}

		return null;
	}

}
