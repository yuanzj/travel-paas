package com.jxmk.connection.cabinet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeReport extends BaseMessage {
    private List<Attribute> attrList;
    private Integer isFull; // 0：增量，1：全量
}