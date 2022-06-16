/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.19 : Database - springboot_demo
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`springboot_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `springboot_demo`;

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '部门状态(0正常 1停用)',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `del_flag` int DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`id`,`name`,`create_time`,`status`,`remark`,`del_flag`) values (1,'生产部','2022-05-29 18:21:25','0','生产',0),(2,'销售部','2022-05-29 18:21:48','0','销售',0),(3,'财务部','2022-05-29 18:21:58','0','财务',0),(4,'人事部','2022-05-29 18:22:08','0','人事',0),(5,'管理部','2022-05-29 18:22:20','0','管理',0),(6,'test','2022-05-29 11:06:16','0','test',0),(7,'test1','2022-05-29 11:12:39','0','test1',1),(8,'test1213','2022-05-29 11:13:07','0','tst21231232131',1),(9,'啊实打实的','2022-05-29 11:13:11','0','asdasddasd',1),(10,'asdasdsa','2022-05-29 11:13:15','0','asdsadsaa',1),(11,'asdasdas','2022-05-29 11:13:19','0','asdasdsadas',1),(12,'市场总监','2022-05-29 13:17:44','0','卖东西',1),(13,'test13','2022-06-08 22:39:33','0','123a',1);

/*Table structure for table `sys_employee` */

DROP TABLE IF EXISTS `sys_employee`;

CREATE TABLE `sys_employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 DEFAULT 'NULL' COMMENT '姓名',
  `phone_number` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
  `sex` char(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '性别',
  `email` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '帐号状态(0正常 1停用)',
  `dept_id` bigint DEFAULT NULL COMMENT '部门编号',
  `job_id` bigint DEFAULT NULL COMMENT '职位编号',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `education` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '学历',
  `del_flag` int DEFAULT '0' COMMENT '删除标志(0代表未删除，1代表已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_employee` */

/*Table structure for table `sys_job` */

DROP TABLE IF EXISTS `sys_job`;

CREATE TABLE `sys_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '岗位名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `del_flag` int DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_job` */

insert  into `sys_job`(`id`,`name`,`create_time`,`status`,`remark`,`del_flag`) values (1,'技术总监','2022-05-29 20:56:52','0','负责技术支持',0),(2,'市场总监','2022-05-29 13:19:06','0','负责市场拓展',0),(3,'总经理','2022-05-29 13:22:46','0','负责管理',1),(4,'CEO','2022-06-13 16:42:43','0','决策',0),(5,'董事长','2022-06-13 16:42:53','0','统筹',0);

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '权限名称',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `url` varchar(100) DEFAULT NULL COMMENT '链接',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）''',
  `create_by` bigint DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '是否删除（0未删除 1已删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`menu_name`,`perms`,`icon`,`url`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`,`remark`) values (1,0,'系统管理','system:manage','li-icon-xitongguanli',NULL,'0',0,'2022-05-22 23:50:30',2,'2022-06-02 12:15:10',0,NULL),(2,1,'用户管理','system:User','icon-cus-manage','system/user','0',0,'2022-05-22 23:50:30',NULL,NULL,0,NULL),(3,1,'角色管理','system:Role','icon-news-manage','system/Role','0',0,'2022-05-22 23:50:30',NULL,NULL,0,NULL),(4,1,'权限管理','system:Menu','icon-cms-manage','system/Permission','0',0,'2022-05-22 23:50:30',NULL,NULL,0,NULL),(5,2,'用户列表','system:User:list',NULL,NULL,'0',0,'2022-05-22 00:01:55',NULL,NULL,0,NULL),(6,2,'用户修改','system:User:save',NULL,NULL,'0',0,'2022-05-20 21:50:53',NULL,NULL,0,NULL),(7,2,'用户删除','system:User:delete',NULL,NULL,'0',0,'2022-05-20 21:51:24',NULL,NULL,0,NULL),(8,2,'用户搜索','system:User:search',NULL,NULL,'0',0,'2022-05-21 13:59:00',NULL,NULL,0,NULL),(9,4,'权限列表','system:Menu:list',NULL,NULL,'0',0,'2022-05-21 19:15:36',NULL,NULL,0,NULL),(10,4,'权限修改','system:Menu:save',NULL,NULL,'0',0,'2022-05-21 19:16:30',NULL,NULL,0,NULL),(11,4,'权限搜索','system:Menu:search',NULL,NULL,'0',0,'2022-05-21 22:37:18',NULL,NULL,0,NULL),(12,4,'权限删除','system:Menu:delete',NULL,NULL,'0',0,'2022-05-21 22:37:55',NULL,NULL,0,NULL),(13,3,'角色列表','system:Role:list',NULL,NULL,'0',0,'2022-05-21 22:39:59',NULL,NULL,0,NULL),(14,3,'角色修改','system:Role:save',NULL,NULL,'0',0,'2022-05-21 22:40:49',NULL,NULL,0,NULL),(15,3,'角色删除','system:Role:delete',NULL,NULL,'0',0,'2022-05-21 22:41:16',NULL,NULL,0,NULL),(16,3,'角色搜索','system:Role:search',NULL,NULL,'0',0,'2022-05-21 22:41:53',NULL,NULL,0,NULL),(17,2,'角色分配','system:User:role',NULL,NULL,'0',0,'2022-05-25 21:58:03',NULL,NULL,0,NULL),(19,0,'公司管理','system:Company','icon-cs-manage','','0',0,'2022-05-29 18:29:52',2,'2022-05-30 18:52:08',0,NULL),(20,19,'部门管理','system:Dept','icon-cat-skuQuery','company/Dept','0',0,'2022-05-29 18:31:20',2,'2022-05-30 18:52:34',0,NULL),(21,20,'部门列表','system:Dept:list',NULL,NULL,'0',0,'2022-05-29 18:31:52',NULL,NULL,0,NULL),(22,20,'部门修改','system:Dept:save',NULL,NULL,'0',0,'2022-05-29 18:58:02',NULL,NULL,0,NULL),(23,20,'部门搜索','system:Dept:search',NULL,NULL,'0',0,'2022-05-29 18:59:37',NULL,NULL,0,NULL),(25,20,'部门删除','system:Dept:delete','','','0',2,'2022-05-29 11:03:36',NULL,NULL,0,NULL),(26,19,'职位管理','system:Job','li-icon-dingdanguanli','company/Job','0',2,'2022-05-29 13:09:21',2,'2022-05-30 18:53:18',0,NULL),(27,26,'职位列表','system:Job:list','','','0',2,'2022-05-29 13:09:53',NULL,NULL,0,NULL),(28,26,'职位修改','system:Job:save','','','0',2,'2022-05-29 13:10:25',NULL,NULL,0,NULL),(29,26,'职位搜索','system:Job:search','','','0',2,'2022-05-29 13:10:55',NULL,NULL,0,NULL),(30,26,'职位删除','system:Job:delete','','','0',2,'2022-05-29 13:11:22',NULL,NULL,0,NULL),(31,19,'员工管理','system:Emp','icon-cus-manage','company/Employee','0',2,'2022-05-29 13:39:59',2,'2022-05-30 18:52:56',0,NULL),(32,31,'员工列表','system:Emp:list','','','0',2,'2022-05-29 13:40:25',NULL,NULL,0,NULL),(33,31,'员工搜索','system:Emp:search','','','0',2,'2022-05-29 13:40:45',NULL,NULL,0,NULL),(34,31,'员工修改','system:Emp:save','','','0',2,'2022-05-29 13:41:14',NULL,NULL,0,NULL),(35,31,'员工删除','system:Emp:delete','','','0',2,'2022-05-29 13:41:29',NULL,NULL,0,NULL),(36,31,'部门分配','system:Emp:dept','','','0',2,'2022-05-30 12:55:02',NULL,NULL,0,NULL),(37,31,'岗位分配','system:Emp:job','','','0',2,'2022-05-30 12:55:26',NULL,NULL,0,NULL),(43,0,'test','etes','','','0',2,'2022-06-14 16:23:37',NULL,NULL,1,NULL),(44,43,'asd','asd','','','0',2,'2022-06-14 16:23:48',NULL,NULL,1,NULL),(45,31,'asdasd','asdsad','','','0',2,'2022-06-15 11:05:13',2,'2022-06-15 11:05:29',1,NULL),(46,45,'asdas','fasfas','','','0',2,'2022-06-15 11:05:21',NULL,NULL,1,NULL);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `role_key` varchar(100) DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` int DEFAULT '0' COMMENT 'del_flag',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`name`,`role_key`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'超级管理员','Administrator','0',0,0,'2022-05-20 21:52:18',NULL,NULL,NULL),(2,'管理员','admin','0',0,0,'2022-05-20 21:52:52',NULL,NULL,NULL),(3,'普通用户','common','0',0,0,'2022-05-20 21:52:55',2,'2022-06-02 12:18:02',NULL),(6,'test1','test1','0',1,2,'2022-05-24 06:18:12',2,'2022-05-24 06:19:36',NULL),(7,'test','test','0',1,2,'2022-05-24 06:18:38',NULL,NULL,NULL),(8,'test1','test1','0',1,2,'2022-05-24 06:28:25',2,'2022-05-24 06:28:34',NULL),(9,'test','test1','0',1,2,'2022-06-08 22:31:15',2,'2022-06-08 22:31:27',NULL),(10,'test','test','0',1,2,'2022-06-14 16:13:55',NULL,NULL,NULL),(11,'asd','asdasd','0',1,2,'2022-06-15 11:04:10',NULL,NULL,NULL);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` bigint NOT NULL DEFAULT '0' COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=786 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`role_id`,`menu_id`) values (482,1,1),(483,1,2),(484,1,5),(485,1,6),(486,1,7),(487,1,8),(488,1,17),(489,1,3),(490,1,13),(491,1,14),(492,1,15),(493,1,16),(494,1,4),(495,1,9),(496,1,10),(497,1,11),(498,1,12),(499,1,19),(500,1,20),(501,1,21),(502,1,22),(503,1,23),(504,1,25),(505,1,26),(506,1,27),(507,1,28),(508,1,29),(509,1,30),(510,1,31),(511,1,32),(512,1,33),(513,1,34),(514,1,35),(515,1,36),(516,1,37),(517,1,1),(518,1,2),(519,1,5),(520,1,6),(521,1,7),(522,1,8),(523,1,17),(524,1,3),(525,1,13),(526,1,14),(527,1,15),(528,1,16),(529,1,4),(530,1,9),(531,1,10),(532,1,11),(533,1,12),(534,1,19),(535,1,20),(536,1,21),(537,1,22),(538,1,23),(539,1,25),(540,1,26),(541,1,27),(542,1,28),(543,1,29),(544,1,30),(545,1,31),(546,1,32),(547,1,33),(548,1,34),(549,1,35),(550,1,36),(551,1,37),(638,9,1),(639,9,2),(640,9,5),(641,9,6),(642,9,1),(643,9,2),(644,9,5),(645,9,6),(696,10,19),(697,10,20),(698,10,21),(699,10,22),(700,10,23),(701,10,25),(702,10,19),(703,10,20),(704,10,21),(705,10,22),(706,10,23),(707,10,25),(708,2,1),(709,2,2),(710,2,5),(711,2,6),(712,2,7),(713,2,8),(714,2,3),(715,2,13),(716,2,4),(717,2,9),(718,2,19),(719,2,20),(720,2,21),(721,2,22),(722,2,23),(723,2,25),(724,2,26),(725,2,27),(726,2,28),(727,2,29),(728,2,30),(729,2,31),(730,2,32),(731,2,33),(732,2,34),(733,2,35),(734,2,36),(735,2,37),(736,2,1),(737,2,2),(738,2,5),(739,2,6),(740,2,7),(741,2,8),(742,2,3),(743,2,13),(744,2,4),(745,2,9),(746,2,19),(747,2,20),(748,2,21),(749,2,22),(750,2,23),(751,2,25),(752,2,26),(753,2,27),(754,2,28),(755,2,29),(756,2,30),(757,2,31),(758,2,32),(759,2,33),(760,2,34),(761,2,35),(762,2,36),(763,2,37),(764,3,9),(765,3,19),(766,3,20),(767,3,21),(768,3,23),(769,3,26),(770,3,27),(771,3,29),(772,3,31),(773,3,32),(774,3,33),(775,3,9),(776,3,19),(777,3,20),(778,3,21),(779,3,23),(780,3,26),(781,3,27),(782,3,29),(783,3,31),(784,3,32),(785,3,33);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `phonenumber` varchar(32) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `user_type` bigint NOT NULL DEFAULT '2' COMMENT '用户类型（0超级管理员,1管理员,2普通用户）',
  `create_by` bigint DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

/*Data for the table `sys_user` */


/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL DEFAULT '0' COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`) values (1,2,1),(2,5,1),(3,6,3),(4,7,3),(5,17,3),(6,3,2),(7,19,2),(8,20,10),(9,18,2),(10,14,2),(11,12,3),(12,22,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
