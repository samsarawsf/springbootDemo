package com.wsf.springbootdemo.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 * @TableName sys_user
 */

@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="sys_user")
@Data
@ApiModel(value = "用户类",description = "用户")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 主键
     */
    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;

    /**
     * 昵称
     */
    @Schema(description = "姓名")
    private String nickName;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phonenumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 账号状态（0正常 1停用）
     */
    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    /**
     * 用户类型（0超级管理员,1管理员,2普通用户）
     */
    @Schema(description = "用户类型（0超级管理员,1管理员,2普通用户）")
    private String userType;

    /**
     * 创建人的用户id
     */
    @Schema(description = "创建人的用户id")
    private Long createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private Long updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableLogic
    @Schema(description = "删除标志（0代表未删除，1代表已删除）")
    private Integer delFlag;

    @TableField(exist = false)
    private Role role;
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
