package com.student.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 李123
 * 返回给前端element ui适配联级选择框的数据结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoElement<T> {
    private T value;
    private String label;
    private List<VoElement<T>> children;
}
