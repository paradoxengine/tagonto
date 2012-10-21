-- MySQL dump 10.11
--
-- Host: localhost    Database: tagonto
-- ------------------------------------------------------
-- Server version	5.0.38-Ubuntu_0ubuntu1-log

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
-- Temporary table structure for view `concrete_mappings`
--


DROP TABLE IF EXISTS `ontologies`;
CREATE TABLE `ontologies` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `url` varchar(256) NOT NULL,
  `last_update` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uri_index` USING BTREE (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `ontology_concepts`;
CREATE TABLE `ontology_concepts` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(128) NOT NULL,
  `ontology` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `ontology_fkey` (`ontology`),
  KEY `concept_name` (`name`),
  CONSTRAINT `ontology_fkey` FOREIGN KEY (`ontology`) REFERENCES `ontologies` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `instances`;
CREATE TABLE `instances` (
  `concept` bigint(20) unsigned NOT NULL,
  `instanceUri` varchar(255) NOT NULL,
  PRIMARY KEY  (`concept`,`instanceUri`),
  CONSTRAINT `concept_fkey` FOREIGN KEY (`concept`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `username` varchar(32) NOT NULL,
  `password` varchar(64),
  `significance` decimal(3,2),
  PRIMARY KEY  (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `mappings`;
CREATE TABLE `mappings` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `tag` varchar(64) NOT NULL,
  `concept` bigint(20) unsigned NOT NULL,
  `user` bigint(20) unsigned NOT NULL,
  `significance` float unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `tag_concept_unique_constraint` USING BTREE (`tag`,`concept`,`user`),
  KEY `user_key_constraint` (`user`),
  KEY `concept_fkey_constraint` (`concept`),
  CONSTRAINT `concept_fkey_constraint` FOREIGN KEY (`concept`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_key_constraint` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 3072 kB; (`user`) REFER `tagonto/users`(`id`) O';

DROP TABLE IF EXISTS `mapped_by`;
CREATE TABLE `mapped_by` (
  `mapping_id` bigint(20) unsigned NOT NULL,
  `mapper` varchar(120) NOT NULL,
  PRIMARY KEY  (`mapping_id`,`mapper`),
  CONSTRAINT `mapping_id_fkey` FOREIGN KEY (`mapping_id`) REFERENCES `mappings` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;



LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'#TAGONTO#','','');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `ontology_declared_properties`;
CREATE TABLE `ontology_declared_properties` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `ontology` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `fkey_ontology` (`ontology`),
  CONSTRAINT `fkey_ontology` FOREIGN KEY (`ontology`) REFERENCES `ontologies` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `ontology_properties`;
CREATE TABLE `ontology_properties` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `firstConcept` bigint(20) unsigned NOT NULL,
  `secondConcept` bigint(20) unsigned NOT NULL,
  `ontology` bigint(20) unsigned NOT NULL,
  `count` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `property_uniqueness` (`firstConcept`,`secondConcept`),
  KEY `ont_fkey` (`ontology`),
  KEY `second_concept_fkey` (`secondConcept`),
  CONSTRAINT `first_concept_fkey` FOREIGN KEY (`firstConcept`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ont_fkey` FOREIGN KEY (`ontology`) REFERENCES `ontologies` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `second_concept_fkey` FOREIGN KEY (`secondConcept`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `reachable_instances`;
CREATE TABLE `reachable_instances` (
  `subject` bigint(20) unsigned NOT NULL,
  `property` bigint(20) unsigned NOT NULL,
  `instanceUri` varchar(255) NOT NULL,
  PRIMARY KEY  (`subject`,`property`,`instanceUri`),
  KEY `property_fkey` (`property`),
  CONSTRAINT `property_fkey` FOREIGN KEY (`property`) REFERENCES `ontology_declared_properties` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subject_fkey` FOREIGN KEY (`subject`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `subclassof`;
CREATE TABLE `subclassof` (
  `subclass` bigint(20) unsigned NOT NULL,
  `superclass` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`subclass`,`superclass`),
  KEY `fkey_superclass` (`superclass`),
  CONSTRAINT `fkey_subclass` FOREIGN KEY (`subclass`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkey_superclass` FOREIGN KEY (`superclass`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;



DROP TABLE IF EXISTS `domain_range`;
CREATE TABLE `domain_range` (
  `subject` bigint(20) unsigned NOT NULL,
  `property` bigint(20) unsigned NOT NULL,
  `object` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`subject`,`property`,`object`),
  KEY `fkey_property` (`property`),
  KEY `fkey_object` (`object`),
  CONSTRAINT `fkey_object` FOREIGN KEY (`object`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkey_property` FOREIGN KEY (`property`) REFERENCES `ontology_declared_properties` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkey_subject` FOREIGN KEY (`subject`) REFERENCES `ontology_concepts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;


DROP TABLE IF EXISTS `concrete_mappings`;
/*!50001 DROP VIEW IF EXISTS `concrete_mappings`*/;
CREATE VIEW
`concrete_mappings` AS select `mappings`.`tag` AS `tag`,`mappings`.`concept` AS `concept`,avg(`mappings`.`significance`) AS `significance` from `mappings` group by `mappings`.`tag`,`mappings`.`concept` ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2007-09-17 13:57:45
