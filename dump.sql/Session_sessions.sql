-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: Session
-- ------------------------------------------------------
-- Server version	5.7.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sessions` (
  `idSession` int(11) NOT NULL AUTO_INCREMENT,
  `idCinema` int(11) NOT NULL,
  `idMovie` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `time` time NOT NULL,
  `idFormat` int(11) DEFAULT NULL,
  PRIMARY KEY (`idSession`),
  UNIQUE KEY `idSession_UNIQUE` (`idSession`),
  KEY `idFormat_idx` (`idFormat`),
  KEY `idCinema_idx` (`idCinema`),
  KEY `idMovie_idx` (`idMovie`),
  CONSTRAINT `idCinema` FOREIGN KEY (`idCinema`) REFERENCES `Cinema`.`Cinemas` (`idCinema`) ON UPDATE CASCADE,
  CONSTRAINT `idFormat` FOREIGN KEY (`idFormat`) REFERENCES `Formats` (`idFormat`) ON UPDATE CASCADE,
  CONSTRAINT `idMovie` FOREIGN KEY (`idMovie`) REFERENCES `movie`.`movies` (`idMovie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=623 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES (1,3,726794,251,'23:25:00',1),(2,5,726794,390,'20:20:00',1),(3,5,726794,250,'00:50:00',1),(4,8,726794,270,'19:45:00',1),(5,20,726794,270,'22:10:00',1),(6,20,726794,250,'00:35:00',1),(7,26,726794,100,'20:55:00',1),(8,26,726794,100,'23:20:00',1),(9,26,726794,200,'23:20:00',1),(10,27,726794,100,'20:00:00',1),(11,28,726794,100,'21:20:00',1),(12,28,726794,200,'21:20:00',1),(13,29,726794,310,'22:10:00',1),(14,30,726794,240,'22:50:00',1),(15,33,726794,250,'21:00:00',1),(16,54,726794,310,'21:50:00',1),(17,55,726794,320,'23:10:00',1),(18,56,726794,280,'22:30:00',1),(19,61,726794,330,'20:25:00',1),(20,61,726794,330,'00:40:00',1),(21,62,726794,320,'00:20:00',1),(22,63,726794,270,'23:10:00',1),(23,64,726794,280,'21:10:00',1),(24,64,726794,280,'23:25:00',1),(25,66,726794,280,'22:45:00',1),(26,67,726794,300,'22:10:00',1),(27,67,726794,300,'00:10:00',1),(28,68,726794,270,'21:00:00',1),(29,68,726794,270,'23:35:00',1),(30,69,726794,280,'23:45:00',1),(31,8,756799,350,'19:10:00',2),(32,18,756799,270,'19:30:00',1),(33,26,756799,200,'20:20:00',1),(34,26,756799,200,'22:20:00',1),(35,26,756799,200,'00:20:00',1),(36,27,756799,200,'22:00:00',1),(37,27,756799,200,'01:10:00',1),(38,27,756799,300,'01:10:00',1),(39,28,756799,200,'21:50:00',1),(40,28,756799,300,'21:50:00',1),(41,28,756799,200,'23:45:00',1),(42,28,756799,300,'23:45:00',1),(43,30,756799,280,'22:20:00',2),(44,54,756799,310,'21:05:00',1),(45,54,756799,310,'23:20:00',1),(46,1,463464,300,'22:55:00',1),(47,2,463464,2000,'19:45:00',1),(48,5,463464,390,'22:25:00',1),(49,5,463464,150,'00:40:00',1),(50,16,463464,270,'21:35:00',1),(51,16,463464,250,'00:20:00',1),(52,18,463464,270,'21:50:00',1),(53,18,463464,250,'00:30:00',1),(54,19,463464,270,'22:20:00',1),(55,19,463464,270,'22:20:00',1),(56,20,463464,270,'21:40:00',1),(57,20,463464,250,'00:20:00',1),(58,26,463464,100,'19:35:00',1),(59,27,463464,100,'00:00:00',1),(60,29,463464,310,'23:50:00',1),(61,30,463464,240,'21:35:00',1),(62,34,463464,230,'22:10:00',1),(63,34,463464,260,'22:10:00',1),(64,53,463464,150,'21:10:00',1),(65,54,463464,310,'23:55:00',1),(66,55,463464,320,'23:30:00',1),(67,61,463464,330,'22:30:00',1),(68,62,463464,320,'21:25:00',1),(69,63,463464,270,'20:05:00',1),(70,67,463464,300,'00:25:00',1),(71,68,463464,270,'22:55:00',1),(72,70,463464,280,'00:15:00',1),(73,2,743088,1500,'21:45:00',2),(74,2,743088,1500,'19:20:00',3),(75,3,743088,280,'19:40:00',1),(76,3,743088,280,'20:20:00',1),(77,3,743088,280,'21:40:00',1),(78,3,743088,280,'22:20:00',1),(79,3,743088,280,'23:40:00',1),(80,3,743088,320,'21:00:00',2),(81,3,743088,320,'23:00:00',2),(82,5,743088,390,'20:35:00',1),(83,5,743088,280,'21:00:00',1),(84,5,743088,280,'22:00:00',1),(85,5,743088,280,'22:50:00',1),(86,5,743088,280,'23:50:00',1),(87,5,743088,150,'01:40:00',1),(88,5,743088,340,'20:10:00',2),(89,8,743088,270,'20:10:00',1),(90,8,743088,270,'21:30:00',1),(91,8,743088,750,'21:30:00',1),(92,8,743088,270,'22:10:00',1),(93,8,743088,210,'00:10:00',1),(94,8,743088,350,'19:30:00',3),(95,8,743088,900,'19:30:00',3),(96,8,743088,350,'23:30:00',3),(97,8,743088,900,'23:30:00',3),(98,13,743088,250,'19:40:00',1),(99,13,743088,345,'20:20:00',2),(100,13,743088,345,'23:20:00',2),(101,14,743088,295,'21:10:00',2),(102,16,743088,270,'19:40:00',1),(103,16,743088,270,'20:35:00',1),(104,16,743088,270,'21:45:00',1),(105,16,743088,270,'22:40:00',1),(106,16,743088,250,'00:40:00',1),(107,16,743088,365,'20:10:00',2),(108,16,743088,365,'23:20:00',2),(109,17,743088,270,'20:10:00',1),(110,17,743088,270,'21:50:00',1),(111,17,743088,365,'19:40:00',2),(112,17,743088,365,'20:40:00',2),(113,18,743088,270,'19:40:00',1),(114,18,743088,270,'21:45:00',1),(115,18,743088,270,'22:40:00',1),(116,18,743088,365,'20:10:00',2),(117,18,743088,365,'23:20:00',2),(118,19,743088,270,'19:40:00',1),(119,19,743088,270,'20:40:00',1),(120,19,743088,270,'21:45:00',1),(121,19,743088,365,'20:10:00',2),(122,19,743088,365,'20:10:00',2),(123,19,743088,365,'21:10:00',2),(124,19,743088,345,'23:20:00',2),(125,20,743088,270,'19:20:00',1),(126,20,743088,270,'20:10:00',1),(127,20,743088,270,'21:30:00',1),(128,20,743088,270,'22:50:00',1),(129,20,743088,345,'19:40:00',2),(130,20,743088,345,'20:40:00',2),(131,20,743088,345,'23:20:00',2),(132,26,743088,100,'20:10:00',1),(133,26,743088,100,'21:55:00',1),(134,26,743088,100,'23:40:00',1),(135,26,743088,100,'19:15:00',2),(136,26,743088,200,'19:15:00',2),(137,27,743088,100,'19:40:00',1),(138,27,743088,100,'21:30:00',1),(139,27,743088,200,'21:30:00',1),(140,27,743088,100,'23:20:00',1),(141,27,743088,200,'23:20:00',1),(142,28,743088,200,'21:35:00',1),(143,28,743088,300,'21:35:00',1),(144,28,743088,200,'23:20:00',1),(145,28,743088,300,'23:20:00',1),(146,28,743088,250,'20:00:00',2),(147,28,743088,350,'20:00:00',2),(148,29,743088,310,'20:40:00',1),(149,29,743088,310,'21:20:00',1),(150,29,743088,310,'21:40:00',1),(151,29,743088,310,'22:40:00',1),(152,29,743088,310,'23:20:00',1),(153,29,743088,390,'19:40:00',2),(154,29,743088,390,'20:20:00',2),(155,29,743088,390,'21:00:00',2),(156,29,743088,390,'22:20:00',2),(157,29,743088,390,'23:00:00',2),(158,29,743088,390,'23:40:00',2),(159,30,743088,240,'19:25:00',1),(160,30,743088,240,'21:15:00',1),(161,30,743088,90,'23:05:00',1),(162,30,743088,90,'00:55:00',1),(163,30,743088,280,'20:15:00',2),(164,30,743088,790,'21:45:00',2),(165,30,743088,280,'22:05:00',2),(166,30,743088,90,'23:55:00',2),(167,34,743088,230,'20:20:00',1),(168,34,743088,260,'20:20:00',1),(169,34,743088,230,'21:20:00',1),(170,34,743088,260,'21:20:00',1),(171,34,743088,230,'22:20:00',1),(172,34,743088,260,'22:20:00',1),(173,34,743088,210,'23:20:00',1),(174,34,743088,240,'23:20:00',1),(175,34,743088,210,'00:10:00',1),(176,34,743088,240,'00:10:00',1),(177,34,743088,280,'19:50:00',2),(178,34,743088,320,'19:50:00',2),(179,34,743088,600,'20:30:00',2),(180,34,743088,280,'21:50:00',2),(181,34,743088,320,'21:50:00',2),(182,34,743088,260,'23:50:00',2),(183,34,743088,300,'23:50:00',2),(184,34,743088,290,'20:50:00',3),(185,34,743088,340,'20:50:00',3),(186,34,743088,290,'22:50:00',3),(187,34,743088,340,'22:50:00',3),(188,35,743088,200,'20:20:00',1),(189,35,743088,230,'20:20:00',1),(190,35,743088,200,'21:20:00',1),(191,35,743088,230,'21:20:00',1),(192,35,743088,200,'22:20:00',1),(193,35,743088,230,'22:20:00',1),(194,35,743088,180,'23:20:00',1),(195,35,743088,210,'23:20:00',1),(196,35,743088,180,'00:20:00',1),(197,35,743088,210,'00:20:00',1),(198,35,743088,240,'19:50:00',2),(199,35,743088,280,'19:50:00',2),(200,35,743088,500,'20:30:00',2),(201,35,743088,240,'21:50:00',2),(202,35,743088,280,'21:50:00',2),(203,35,743088,500,'22:30:00',2),(204,35,743088,220,'23:50:00',2),(205,35,743088,260,'23:50:00',2),(206,35,743088,260,'20:50:00',3),(207,35,743088,310,'20:50:00',3),(208,35,743088,260,'22:50:00',3),(209,35,743088,310,'22:50:00',3),(210,47,743088,250,'20:00:00',1),(211,47,743088,250,'21:50:00',1),(212,52,743088,350,'19:30:00',1),(213,52,743088,300,'21:15:00',1),(214,54,743088,310,'19:45:00',1),(215,54,743088,310,'20:55:00',1),(216,54,743088,310,'21:45:00',1),(217,54,743088,420,'22:00:00',1),(218,54,743088,310,'22:55:00',1),(219,54,743088,310,'23:45:00',1),(220,54,743088,310,'00:50:00',1),(221,54,743088,480,'20:00:00',2),(222,54,743088,410,'20:20:00',2),(223,54,743088,410,'21:25:00',2),(224,54,743088,410,'22:20:00',2),(225,54,743088,410,'23:25:00',2),(226,54,743088,480,'00:05:00',2),(227,54,743088,410,'00:20:00',2),(228,55,743088,320,'20:00:00',1),(229,55,743088,320,'22:00:00',1),(230,55,743088,320,'23:55:00',1),(231,55,743088,360,'19:40:00',2),(232,55,743088,380,'20:20:00',2),(233,55,743088,610,'21:00:00',2),(234,55,743088,360,'21:40:00',2),(235,55,743088,380,'22:20:00',2),(236,55,743088,610,'23:00:00',2),(237,56,743088,280,'19:40:00',1),(238,56,743088,280,'21:50:00',1),(239,56,743088,350,'20:20:00',2),(240,56,743088,350,'21:10:00',2),(241,59,743088,280,'20:20:00',1),(242,59,743088,280,'21:10:00',1),(243,59,743088,280,'22:30:00',1),(244,59,743088,280,'23:20:00',1),(245,59,743088,280,'00:40:00',1),(246,59,743088,350,'19:40:00',2),(247,59,743088,350,'21:50:00',2),(248,59,743088,350,'00:00:00',2),(249,60,743088,140,'20:25:00',1),(250,60,743088,140,'20:55:00',1),(251,60,743088,140,'21:25:00',1),(252,60,743088,140,'22:35:00',1),(253,60,743088,140,'23:05:00',1),(254,60,743088,140,'00:45:00',1),(255,60,743088,190,'19:40:00',3),(256,60,743088,190,'21:50:00',3),(257,60,743088,190,'00:00:00',3),(258,61,743088,330,'20:45:00',1),(259,61,743088,330,'21:40:00',1),(260,61,743088,330,'22:55:00',1),(261,61,743088,330,'23:50:00',1),(262,61,743088,400,'20:10:00',2),(263,61,743088,400,'22:20:00',2),(264,61,743088,400,'00:30:00',2),(265,62,743088,320,'19:40:00',1),(266,62,743088,320,'20:30:00',1),(267,62,743088,320,'21:25:00',1),(268,62,743088,320,'21:50:00',1),(269,62,743088,320,'22:40:00',1),(270,62,743088,320,'23:35:00',1),(271,62,743088,320,'00:00:00',1),(272,62,743088,320,'00:50:00',1),(273,62,743088,390,'20:05:00',2),(274,62,743088,390,'20:55:00',2),(275,62,743088,390,'22:15:00',2),(276,62,743088,390,'23:05:00',2),(277,62,743088,390,'00:25:00',2),(278,63,743088,270,'19:40:00',1),(279,63,743088,270,'20:00:00',1),(280,63,743088,270,'20:20:00',1),(281,63,743088,270,'21:10:00',1),(282,63,743088,270,'21:50:00',1),(283,63,743088,270,'22:10:00',1),(284,63,743088,270,'22:30:00',1),(285,63,743088,270,'23:20:00',1),(286,63,743088,270,'00:00:00',1),(287,63,743088,270,'00:20:00',1),(288,63,743088,350,'21:30:00',3),(289,63,743088,350,'23:40:00',3),(290,64,743088,280,'19:50:00',1),(291,64,743088,280,'20:20:00',1),(292,64,743088,280,'21:00:00',1),(293,64,743088,280,'22:00:00',1),(294,64,743088,280,'00:10:00',1),(295,64,743088,330,'21:30:00',2),(296,64,743088,330,'23:10:00',2),(297,65,743088,270,'21:15:00',1),(298,65,743088,270,'23:25:00',1),(299,66,743088,280,'20:30:00',1),(300,66,743088,280,'21:20:00',1),(301,66,743088,280,'22:40:00',1),(302,66,743088,280,'23:30:00',1),(303,66,743088,280,'00:50:00',1),(304,66,743088,320,'19:50:00',2),(305,66,743088,320,'22:00:00',2),(306,67,743088,300,'19:40:00',1),(307,67,743088,300,'20:40:00',1),(308,67,743088,300,'21:20:00',1),(309,67,743088,300,'21:50:00',1),(310,67,743088,300,'22:50:00',1),(311,67,743088,300,'23:30:00',1),(312,67,743088,300,'00:55:00',1),(313,67,743088,350,'20:10:00',2),(314,67,743088,350,'22:20:00',2),(315,67,743088,350,'00:30:00',2),(316,68,743088,270,'19:40:00',1),(317,68,743088,270,'20:10:00',1),(318,68,743088,270,'20:45:00',1),(319,68,743088,270,'21:50:00',1),(320,68,743088,270,'22:20:00',1),(321,68,743088,270,'00:00:00',1),(322,68,743088,270,'00:30:00',1),(323,68,743088,350,'21:20:00',3),(324,69,743088,280,'19:40:00',1),(325,69,743088,280,'20:10:00',1),(326,69,743088,280,'21:20:00',1),(327,69,743088,280,'21:50:00',1),(328,69,743088,280,'22:20:00',1),(329,69,743088,280,'23:30:00',1),(330,69,743088,280,'00:00:00',1),(331,69,743088,330,'20:40:00',2),(332,70,743088,280,'20:20:00',1),(333,70,743088,280,'21:20:00',1),(334,70,743088,280,'22:30:00',1),(335,70,743088,280,'23:30:00',1),(336,70,743088,320,'19:50:00',2),(337,70,743088,320,'22:00:00',2),(338,70,743088,320,'00:10:00',2),(339,2,841147,1500,'20:20:00',1),(340,30,841147,240,'19:35:00',1),(341,68,841147,270,'23:15:00',1),(342,2,602749,1500,'19:30:00',1),(343,3,602749,250,'20:35:00',1),(344,3,602749,250,'22:50:00',1),(345,3,602749,150,'00:20:00',1),(346,3,602749,150,'01:00:00',1),(347,5,602749,390,'22:20:00',1),(348,5,602749,250,'00:30:00',1),(349,5,602749,250,'02:35:00',1),(350,8,602749,2000,'20:55:00',1),(351,8,602749,270,'21:15:00',1),(352,8,602749,270,'23:35:00',1),(353,13,602749,250,'20:30:00',1),(354,13,602749,250,'22:50:00',1),(355,14,602749,180,'20:20:00',1),(356,14,602749,180,'21:50:00',1),(357,14,602749,180,'22:50:00',1),(358,16,602749,270,'19:30:00',1),(359,16,602749,270,'22:00:00',1),(360,16,602749,250,'00:30:00',1),(361,17,602749,270,'21:40:00',1),(362,19,602749,270,'19:35:00',1),(363,19,602749,650,'20:30:00',1),(364,19,602749,270,'22:10:00',1),(365,19,602749,180,'00:40:00',1),(366,20,602749,270,'19:30:00',1),(367,20,602749,270,'20:45:00',1),(368,20,602749,270,'22:00:00',1),(369,20,602749,270,'23:15:00',1),(370,20,602749,250,'00:40:00',1),(371,26,602749,100,'20:25:00',1),(372,26,602749,100,'22:35:00',1),(373,26,602749,100,'00:45:00',1),(374,27,602749,100,'21:15:00',1),(375,27,602749,200,'21:15:00',1),(376,27,602749,100,'23:30:00',1),(377,27,602749,200,'23:30:00',1),(378,28,602749,100,'19:25:00',1),(379,28,602749,200,'19:25:00',1),(380,28,602749,100,'23:20:00',1),(381,28,602749,200,'23:20:00',1),(382,29,602749,310,'19:50:00',1),(383,29,602749,310,'20:45:00',1),(384,29,602749,310,'23:05:00',1),(385,30,602749,240,'19:20:00',1),(386,30,602749,750,'19:30:00',1),(387,30,602749,240,'20:35:00',1),(388,30,602749,240,'21:50:00',1),(389,30,602749,90,'00:00:00',1),(390,30,602749,90,'00:50:00',1),(391,34,602749,600,'22:40:00',1),(392,34,602749,230,'21:40:00',4),(393,34,602749,260,'21:40:00',4),(394,34,602749,210,'00:00:00',4),(395,34,602749,240,'00:00:00',4),(396,34,602749,470,'00:00:00',4),(397,35,602749,200,'21:40:00',1),(398,35,602749,230,'21:40:00',1),(399,35,602749,180,'00:00:00',1),(400,35,602749,210,'00:00:00',1),(401,47,602749,250,'21:25:00',1),(402,54,602749,310,'19:50:00',1),(403,54,602749,310,'20:50:00',1),(404,54,602749,310,'22:10:00',1),(405,54,602749,310,'23:10:00',1),(406,54,602749,310,'00:30:00',1),(407,55,602749,320,'20:30:00',1),(408,55,602749,320,'21:30:00',1),(409,55,602749,320,'22:50:00',1),(410,55,602749,320,'23:45:00',1),(411,56,602749,280,'21:40:00',1),(412,59,602749,280,'19:50:00',1),(413,59,602749,280,'00:20:00',1),(414,60,602749,140,'20:10:00',1),(415,60,602749,140,'21:20:00',1),(416,60,602749,140,'22:45:00',1),(417,60,602749,140,'23:50:00',1),(418,61,602749,530,'19:50:00',1),(419,61,602749,330,'20:30:00',1),(420,61,602749,530,'22:15:00',1),(421,61,602749,330,'23:00:00',1),(422,61,602749,530,'00:40:00',1),(423,62,602749,320,'20:15:00',1),(424,62,602749,320,'22:45:00',1),(425,62,602749,320,'00:05:00',1),(426,63,602749,270,'20:15:00',1),(427,63,602749,270,'21:40:00',1),(428,63,602749,270,'22:45:00',1),(429,63,602749,270,'00:10:00',1),(430,64,602749,280,'20:30:00',1),(431,64,602749,280,'21:50:00',1),(432,64,602749,280,'23:00:00',1),(433,64,602749,280,'00:20:00',1),(434,65,602749,270,'20:10:00',1),(435,65,602749,270,'22:40:00',1),(436,66,602749,280,'20:35:00',1),(437,66,602749,280,'23:05:00',1),(438,66,602749,280,'00:10:00',1),(439,67,602749,300,'20:30:00',1),(440,67,602749,300,'21:40:00',1),(441,67,602749,300,'23:00:00',1),(442,67,602749,300,'00:05:00',1),(443,68,602749,270,'20:35:00',1),(444,68,602749,270,'21:45:00',1),(445,68,602749,270,'23:05:00',1),(446,68,602749,270,'00:15:00',1),(447,69,602749,280,'20:30:00',1),(448,69,602749,280,'23:00:00',1),(449,70,602749,280,'19:45:00',1),(450,70,602749,280,'23:20:00',1),(451,70,602749,280,'00:45:00',1),(452,2,468522,500,'21:10:00',3),(453,3,468522,250,'19:55:00',1),(454,3,468522,250,'20:50:00',1),(455,3,468522,250,'22:25:00',1),(456,3,468522,150,'00:50:00',1),(457,5,468522,230,'20:05:00',1),(458,5,468522,230,'22:30:00',1),(459,5,468522,150,'02:15:00',1),(460,8,468522,270,'20:10:00',1),(461,8,468522,270,'22:40:00',1),(462,8,468522,2000,'23:25:00',1),(463,13,468522,250,'22:20:00',1),(464,13,468522,250,'23:40:00',1),(465,13,468522,250,'00:50:00',1),(466,14,468522,180,'21:30:00',1),(467,14,468522,790,'22:40:00',5),(468,16,468522,270,'21:20:00',1),(469,16,468522,270,'22:10:00',1),(470,16,468522,250,'00:00:00',1),(471,16,468522,250,'00:50:00',1),(472,17,468522,270,'21:30:00',1),(473,17,468522,270,'22:20:00',1),(474,18,468522,270,'21:30:00',1),(475,18,468522,270,'22:10:00',1),(476,18,468522,250,'00:10:00',1),(477,18,468522,250,'00:50:00',1),(478,19,468522,270,'20:20:00',1),(479,19,468522,270,'21:50:00',1),(480,19,468522,180,'23:00:00',1),(481,20,468522,270,'23:10:00',1),(482,20,468522,250,'00:30:00',1),(483,26,468522,100,'21:55:00',1),(484,26,468522,100,'00:15:00',1),(485,26,468522,100,'21:00:00',2),(486,26,468522,200,'21:00:00',2),(487,27,468522,100,'21:00:00',1),(488,29,468522,310,'20:30:00',1),(489,29,468522,390,'21:30:00',2),(490,29,468522,390,'23:55:00',2),(491,30,468522,90,'00:40:00',1),(492,30,468522,150,'19:50:00',2),(493,30,468522,150,'22:15:00',2),(494,30,468522,550,'23:35:00',2),(495,34,468522,210,'23:10:00',1),(496,34,468522,240,'23:10:00',1),(497,34,468522,280,'20:40:00',2),(498,34,468522,320,'20:40:00',2),(499,35,468522,180,'23:10:00',1),(500,35,468522,210,'23:10:00',1),(501,35,468522,240,'20:40:00',2),(502,35,468522,280,'20:40:00',2),(503,35,468522,240,'22:10:00',2),(504,35,468522,280,'22:10:00',2),(505,47,468522,100,'20:55:00',1),(506,51,468522,200,'20:30:00',2),(507,52,468522,300,'21:00:00',2),(508,54,468522,620,'20:35:00',1),(509,54,468522,310,'22:20:00',1),(510,54,468522,410,'19:55:00',2),(511,54,468522,770,'23:05:00',2),(512,54,468522,410,'00:45:00',2),(513,54,468522,760,'21:05:00',5),(514,54,468522,460,'23:35:00',5),(515,55,468522,360,'21:05:00',2),(516,55,468522,360,'23:25:00',2),(517,55,468522,570,'20:05:00',6),(518,55,468522,570,'22:40:00',6),(519,56,468522,280,'21:20:00',1),(520,56,468522,350,'20:00:00',2),(521,56,468522,350,'22:40:00',2),(522,59,468522,280,'20:45:00',1),(523,59,468522,280,'23:40:00',1),(524,60,468522,140,'20:20:00',1),(525,60,468522,140,'23:35:00',1),(526,61,468522,330,'22:00:00',1),(527,61,468522,330,'00:45:00',1),(528,61,468522,800,'21:15:00',5),(529,61,468522,800,'23:55:00',5),(530,61,468522,330,'19:45:00',1),(531,62,468522,320,'19:55:00',1),(532,62,468522,320,'21:30:00',1),(533,62,468522,320,'22:50:00',1),(534,62,468522,600,'21:20:00',5),(535,62,468522,600,'00:00:00',5),(536,63,468522,270,'21:10:00',1),(537,63,468522,270,'22:25:00',1),(538,63,468522,270,'23:55:00',1),(539,64,468522,280,'21:05:00',1),(540,64,468522,280,'22:30:00',1),(541,64,468522,280,'23:50:00',1),(542,66,468522,280,'21:15:00',1),(543,66,468522,280,'00:00:00',1),(544,67,468522,300,'20:20:00',1),(545,67,468522,300,'23:05:00',1),(546,67,468522,1100,'21:25:00',5),(547,67,468522,1100,'00:10:00',5),(548,68,468522,270,'20:50:00',1),(549,68,468522,270,'21:45:00',1),(550,68,468522,270,'00:30:00',1),(551,68,468522,270,'23:30:00',7),(552,69,468522,280,'21:05:00',1),(553,69,468522,280,'23:50:00',1),(554,69,468522,330,'22:50:00',2),(555,70,468522,280,'21:15:00',1),(556,70,468522,280,'00:00:00',1),(557,5,899500,150,'00:45:00',1),(558,8,899500,270,'21:45:00',1),(559,8,899500,270,'23:25:00',1),(560,13,899500,250,'23:50:00',1),(561,16,899500,270,'23:50:00',1),(562,17,899500,270,'22:50:00',1),(563,18,899500,270,'23:50:00',1),(564,19,899500,180,'23:50:00',1),(565,20,899500,270,'23:40:00',1),(566,26,899500,100,'22:55:00',1),(567,26,899500,100,'00:30:00',1),(568,27,899500,100,'23:25:00',1),(569,27,899500,100,'01:00:00',1),(570,27,899500,100,'02:35:00',1),(571,28,899500,100,'01:30:00',1),(572,28,899500,200,'01:30:00',1),(573,28,899500,100,'03:05:00',1),(574,28,899500,200,'03:05:00',1),(575,29,899500,310,'22:05:00',1),(576,30,899500,90,'00:05:00',1),(577,54,899500,310,'21:10:00',1),(578,54,899500,310,'23:00:00',1),(579,55,899500,320,'20:50:00',1),(580,59,899500,129,'22:20:00',1),(581,60,899500,140,'23:10:00',1),(582,61,899500,219,'23:50:00',1),(583,62,899500,219,'00:15:00',1),(584,63,899500,149,'19:45:00',1),(585,63,899500,149,'00:15:00',1),(586,64,899500,139,'19:50:00',1),(587,64,899500,139,'23:40:00',1),(588,66,899500,119,'00:55:00',1),(589,68,899500,119,'22:10:00',1),(590,69,899500,129,'21:35:00',1),(591,70,899500,119,'22:15:00',1),(592,30,425673,90,'00:20:00',2),(593,55,425673,360,'22:30:00',2),(594,63,425673,149,'21:45:00',1),(595,66,425673,119,'20:15:00',1),(596,67,425673,129,'20:15:00',1),(597,70,425673,119,'20:50:00',1),(598,2,893362,2000,'22:10:00',1),(599,6,893362,300,'21:20:00',1),(600,6,893362,700,'21:20:00',1),(601,19,893362,180,'00:50:00',1),(602,53,893362,150,'19:20:00',1),(603,62,893362,320,'22:05:00',1),(604,67,893362,300,'00:40:00',1),(605,6,882569,300,'21:00:00',1),(606,30,882569,100,'20:25:00',1),(607,53,762750,150,'21:20:00',1),(608,53,623721,150,'21:15:00',1),(609,34,919515,100,'23:30:00',1),(610,35,919515,100,'23:50:00',1),(611,53,557787,150,'19:15:00',1),(612,61,954504,330,'22:40:00',1),(613,67,954504,300,'22:45:00',1),(614,69,954504,280,'23:35:00',1),(615,29,786958,310,'22:50:00',1),(616,34,786958,100,'20:50:00',1),(617,35,786958,100,'21:10:00',1),(618,55,786958,320,'20:40:00',1),(619,63,786958,149,'22:45:00',1),(620,53,814482,150,'19:35:00',1),(621,5,461939,150,'20:45:00',1),(622,5,461939,150,'00:35:00',1);
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-25 22:21:44