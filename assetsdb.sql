/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.5.28 : Database - assetsdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`assetsdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `assetsdb`;

/*Table structure for table `assets` */

DROP TABLE IF EXISTS `assets`;

CREATE TABLE `assets` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '标识编号',
  `assetId` varchar(5) NOT NULL COMMENT '资产编号',
  `assetName` varchar(100) NOT NULL COMMENT '资产名称',
  `assetType` varchar(10) NOT NULL COMMENT '资产类型',
  `intoDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `assets` */

insert  into `assets`(`id`,`assetId`,`assetName`,`assetType`,`intoDate`) values 
(2,'d0002','台式机','机械设备','2020-02-03 11:12:13'),
(8,'d0001','大奔驰','运输设备','2020-09-20 12:23:32');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
