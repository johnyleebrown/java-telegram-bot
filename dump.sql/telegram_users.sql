-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: telegram
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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `idusers` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `chat_id` int(11) NOT NULL DEFAULT '0',
  `state` int(11) DEFAULT NULL,
  `price` varchar(45) DEFAULT 'cheapest',
  `dist` varchar(45) DEFAULT 'closest',
  `lat` double DEFAULT '0',
  `lon` double DEFAULT '0',
  `city` varchar(45) DEFAULT NULL,
  `street` varchar(45) DEFAULT NULL,
  `build` int(11) DEFAULT NULL,
  `movie_state` int(11) DEFAULT '1',
  `sessions_state` int(11) DEFAULT '1',
  `restricted` varchar(1500) CHARACTER SET utf8 DEFAULT 'none',
  `username` varchar(45) DEFAULT 'noname',
  `lastseen` varchar(50) DEFAULT '1970-01-01 00:00:00.000',
  `chosenflag` int(11) DEFAULT '0',
  `venue_info` varchar(45) DEFAULT 'null',
  PRIMARY KEY (`idusers`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2779 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2672,240134145,240134145,1,'cheapest','closest',59.956962,30.3060245,NULL,'markina',5,3,1,'Райское озеро#','noname','2016-09-16',0,'null'),(2685,123,0,1,'cheapest','closest',0,0,NULL,NULL,NULL,1,1,NULL,'qwert','2016-09-16',0,'null'),(2686,1231,0,1,'cheapest','closest',0,0,NULL,NULL,NULL,1,1,NULL,'qweqewe','2016-09-16',0,'null'),(2687,1234,0,1,'cheapest','closest',0,0,NULL,NULL,NULL,1,1,NULL,'qw','2016-09-16',0,'null'),(2688,104490161,104490161,7,'cheapest','closest',0,0,NULL,'Markina',5,10,1,'none','noname','2016-10-25',0,'null');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
