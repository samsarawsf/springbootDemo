package com.wsf.springbootdemo.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单表
 * @TableName sys_menu
 */
@TableName(value="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;
    /**
     * 权限名称
     */
    private String menuName;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接
     */
    private String url;

    /**
     * 菜单状态（0正常 1停用）'
     */
    private String status;

    /**
     *
     */
    private Long createBy;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Long updateBy;

    /**
     *
     */
    private Date updateTime;

    /**
     * 是否删除（0未删除 1已删除）
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<Menu> menus = new ArrayList<>();

}
