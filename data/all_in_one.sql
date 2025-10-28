CREATE DATABASE  IF NOT EXISTS `spkt_connect` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spkt_connect`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: spkt_connect
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_members`
--

DROP TABLE IF EXISTS `chat_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_members` (
  `chat_id` int NOT NULL,
  `user_id` int NOT NULL,
  `role_in_chat` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`chat_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `chat_members_ibfk_1` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`) ON DELETE CASCADE,
  CONSTRAINT `chat_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_members`
--

LOCK TABLES `chat_members` WRITE;
/*!40000 ALTER TABLE `chat_members` DISABLE KEYS */;
INSERT INTO `chat_members` VALUES (1,4,'member'),(2,2,'member'),(3,4,'member'),(4,2,'member'),(4,4,'member'),(5,4,'member'),(5,8,'member');
/*!40000 ALTER TABLE `chat_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `chat_id` int NOT NULL AUTO_INCREMENT,
  `chat_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chat_avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_group` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`chat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
INSERT INTO `chats` VALUES (1,NULL,NULL,0,'2025-10-24 09:28:14'),(2,'nhóm 1',NULL,1,'2025-10-24 10:49:56'),(3,NULL,NULL,0,'2025-10-24 11:17:04'),(4,'MarketChat 2 4',NULL,0,'2025-10-24 20:12:14'),(5,NULL,NULL,0,'2025-10-28 01:23:45');
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL,
  `author_id` int NOT NULL,
  `parent_comment_id` int DEFAULT NULL,
  `content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `post_id` (`post_id`),
  KEY `author_id` (`author_id`),
  KEY `parent_comment_id` (`parent_comment_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`parent_comment_id`) REFERENCES `comments` (`comment_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (7,32,8,NULL,'bl2','2025-10-28 00:48:50','2025-10-28 00:49:05'),(9,30,8,NULL,'c','2025-10-28 01:02:18','2025-10-28 01:02:18'),(10,30,3,NULL,'f','2025-10-28 01:07:15','2025-10-28 01:07:15'),(11,25,3,NULL,'g','2025-10-28 01:07:20','2025-10-28 01:07:20'),(13,21,3,NULL,'h','2025-10-28 01:07:33','2025-10-28 01:07:33'),(14,21,3,NULL,'f','2025-10-28 01:07:35','2025-10-28 01:07:35'),(15,14,3,NULL,'b','2025-10-28 01:07:39','2025-10-28 01:07:39');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`department_id`),
  UNIQUE KEY `department_name` (`department_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `user_id_1` int NOT NULL,
  `user_id_2` int NOT NULL,
  `status` enum('pending','accepted','blocked','refused') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id_1`,`user_id_2`),
  KEY `user_id_2` (`user_id_2`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`user_id_1`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`user_id_2`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
INSERT INTO `friends` VALUES (2,8,'accepted','2025-10-28 01:13:49','2025-10-28 01:14:00'),(3,2,'accepted','2025-10-24 08:36:59','2025-10-28 01:13:39'),(4,2,'accepted','2025-10-24 10:48:45','2025-10-24 10:49:25'),(8,4,'accepted','2025-10-28 00:38:04','2025-10-28 00:38:26');
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_read_status`
--

DROP TABLE IF EXISTS `message_read_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_read_status` (
  `message_id` bigint NOT NULL,
  `user_id` int NOT NULL,
  `read_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `message_read_status_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`) ON DELETE CASCADE,
  CONSTRAINT `message_read_status_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_read_status`
--

LOCK TABLES `message_read_status` WRITE;
/*!40000 ALTER TABLE `message_read_status` DISABLE KEYS */;
INSERT INTO `message_read_status` VALUES (3,4,'2025-10-24 11:16:27'),(4,4,'2025-10-24 11:17:08'),(5,4,'2025-10-27 19:54:45'),(6,4,'2025-10-27 19:54:53'),(9,4,'2025-10-28 01:24:55'),(9,8,'2025-10-28 01:24:56');
/*!40000 ALTER TABLE `message_read_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `chat_id` int NOT NULL,
  `sender_id` int NOT NULL,
  `content` tinytext COLLATE utf8mb4_unicode_ci,
  `media_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `chat_id` (`chat_id`),
  KEY `sender_id` (`sender_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`) ON DELETE CASCADE,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (3,1,4,'a',NULL,'2025-10-24 11:16:27'),(4,3,4,'a',NULL,'2025-10-24 11:17:08'),(5,1,4,'','/api/media/files/4_35996e9b-2e52-4ad4-9949-ddd6b085f188_ute_logo.png','2025-10-27 19:54:45'),(6,1,4,'lo','/api/media/files/4_a718f432-2541-4e6d-a1f4-ddb4932b530f_bàn phím.jpg','2025-10-27 19:54:53'),(9,5,4,'hi','/api/media/files/4_2ae56a4a-5f5c-44de-a361-7db72f95bd34_ute_logo.png','2025-10-28 01:24:55');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `recipient_id` int NOT NULL,
  `sender_id` int DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_id` int DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `recipient_id` (`recipient_id`),
  KEY `sender_id` (`sender_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (16,4,NULL,'friend_request_accepted',NULL,NULL,NULL,'Vu Anh Quoc đã chấp nhận lời mời kết bạn. Bây giờ hai bạn đã là bạn bè!'),(18,4,NULL,'new_post',10,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(33,4,NULL,'friend_request_accepted',NULL,NULL,NULL,'Khoa Công Nghệ Thông Tin đã chấp nhận lời mời kết bạn. Bây giờ hai bạn đã là bạn bè!'),(35,4,NULL,'new_reaction',13,NULL,NULL,'Điệp đã phản ứng về bài viết của bạn.'),(36,4,NULL,'friend_request',NULL,NULL,NULL,'Bạn có lời mời kết bạn đến từ: Điệp'),(38,4,NULL,'new_comment',16,NULL,NULL,'Điệp đã bình luận về bài viết của bạn.'),(40,4,NULL,'new_post',19,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(41,4,NULL,'new_reaction',16,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(42,4,NULL,'new_reaction',14,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(43,4,NULL,'new_reaction',13,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(44,4,NULL,'new_reaction',11,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(52,3,NULL,'important_post',20,NULL,NULL,'Người dùng Tôn Hoàng Cầm (Quản trị viên) vừa đăng một bài viết quan trọng.'),(54,4,NULL,'new_post',22,NULL,NULL,'Bạn bè của bạn Khoa Công Nghệ Thông Tin vừa đăng một bài viết mới.'),(55,4,NULL,'new_post',23,NULL,NULL,'Bạn bè của bạn Khoa Công Nghệ Thông Tin vừa đăng một bài viết mới.'),(58,3,NULL,'new_post',26,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(59,4,NULL,'new_post',26,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(60,3,NULL,'new_post',27,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(61,4,NULL,'new_post',27,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(62,4,NULL,'new_reaction',25,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(65,3,NULL,'new_reaction',21,NULL,NULL,'Vu Anh Quoc đã phản ứng về bài viết của bạn.'),(68,3,NULL,'important_post',28,NULL,NULL,'Người dùng Tôn Hoàng Cầm (Quản trị viên) vừa đăng một bài viết quan trọng.'),(71,4,NULL,'friend_request',NULL,NULL,NULL,'Bạn có lời mời kết bạn đến từ: Vũ Anh Quốc'),(72,8,NULL,'friend_request_accepted',NULL,NULL,NULL,'Tôn Hoàng Cầm (Quản trị viên) đã chấp nhận lời mời kết bạn. Bây giờ hai bạn đã là bạn bè!'),(73,4,NULL,'new_post',30,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(74,4,NULL,'new_post',31,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(75,4,NULL,'new_post',32,NULL,NULL,'Bạn bè của bạn Vu Anh Quoc vừa đăng một bài viết mới.'),(76,8,NULL,'new_comment',30,NULL,NULL,'hoa quả sơn đã bình luận về bài viết của bạn.'),(77,4,NULL,'new_comment',25,NULL,NULL,'hoa quả sơn đã bình luận về bài viết của bạn.'),(81,4,NULL,'new_comment',14,NULL,NULL,'hoa quả sơn đã bình luận về bài viết của bạn.'),(82,3,NULL,'friend_request_accepted',NULL,NULL,NULL,'Khoa Công Nghệ Thông Tin đã chấp nhận lời mời kết bạn. Bây giờ hai bạn đã là bạn bè!'),(83,8,NULL,'friend_request',NULL,NULL,NULL,'Bạn có lời mời kết bạn đến từ: Khoa Công Nghệ Thông Tin'),(86,3,NULL,'important_post',33,NULL,NULL,'Người dùng Tôn Hoàng Cầm (Quản trị viên) vừa đăng một bài viết quan trọng.'),(87,8,NULL,'important_post',33,NULL,NULL,'Người dùng Tôn Hoàng Cầm (Quản trị viên) vừa đăng một bài viết quan trọng.'),(88,3,NULL,'important_post',34,NULL,NULL,'Người dùng Khoa Công Nghệ Thông Tin vừa đăng một bài viết quan trọng.'),(89,4,NULL,'important_post',34,NULL,NULL,'Người dùng Khoa Công Nghệ Thông Tin vừa đăng một bài viết quan trọng.'),(90,8,NULL,'important_post',34,NULL,NULL,'Người dùng Khoa Công Nghệ Thông Tin vừa đăng một bài viết quan trọng.'),(91,2,NULL,'new_comment',23,NULL,NULL,'Vu Anh Quoc đã bình luận về bài viết của bạn.'),(92,4,NULL,'new_comment',13,NULL,NULL,'Vu Anh Quoc đã bình luận về bài viết của bạn.'),(93,4,NULL,'new_comment',13,NULL,NULL,'hoa quả sơn đã bình luận về bài viết của bạn.'),(94,2,NULL,'new_comment',7,NULL,NULL,'Vu Anh Quoc đã bình luận về bài viết của bạn.'),(95,2,NULL,'new_comment',7,NULL,NULL,'hoa quả sơn đã bình luận về bài viết của bạn.'),(96,2,NULL,'new_comment',7,NULL,NULL,'Vu Anh Quoc đã bình luận về bài viết của bạn.'),(97,8,NULL,'new_reaction',30,NULL,NULL,'Khoa Công Nghệ Thông Tin đã phản ứng về bài viết của bạn.'),(98,8,2,'post_share',1,0,NULL,NULL),(99,8,4,'post_share',2,0,NULL,NULL),(100,8,NULL,'new_reaction',32,NULL,NULL,'Tôn Hoàng Cầm (Quản trị viên) đã phản ứng về bài viết của bạn.');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_media`
--

DROP TABLE IF EXISTS `post_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_media` (
  `media_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL,
  `media_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `media_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`media_id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `post_media_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_media`
--

LOCK TABLES `post_media` WRITE;
/*!40000 ALTER TABLE `post_media` DISABLE KEYS */;
INSERT INTO `post_media` VALUES (6,5,'image','http://localhost:8080/api/media/files/2_daafbbce-e0f0-41dd-bdbf-9b223bcb75c6_HCMUTE-2.png','2025-10-24 09:01:15'),(7,6,'image','http://localhost:8080/api/media/files/2_ef7d9825-909c-496d-b59e-047aa15f9264_hackathon.jpg','2025-10-24 09:06:05'),(8,7,'image','http://localhost:8080/api/media/files/2_dc61820e-0388-40be-a4fc-18f782c9ba1a_ca-chua.jpg','2025-10-24 09:13:24'),(9,8,'video','http://localhost:8080/api/media/files/2_a5c14a7a-f86e-43ad-9c64-1a4f56cd7ccd_CapCut 2024-02-23 21-47-19.mp4','2025-10-24 09:13:45'),(12,11,'image','http://localhost:8080/api/media/files/4_c77f2dc1-e36e-4dc8-b5d6-426c6363f975_logo-cntt2021.png','2025-10-24 09:30:44'),(13,12,'image','http://localhost:8080/api/media/files/3_dc5f705e-1f40-430c-9771-5c2f1633ad87_OIP.webp','2025-10-24 09:31:46'),(14,13,'image','http://localhost:8080/api/media/files/4_3ade3fed-a0e0-4ab0-bcc0-dbc7ef82ea81_icons-bundle-3.png','2025-10-24 10:06:03'),(16,21,'video','http://localhost:8080/api/media/files/3_64a6a7c3-e59f-490d-9d55-59190d8031c1_CapCut 2024-02-23 21-47-19.mp4','2025-10-24 20:31:05'),(17,23,'pdf','http://localhost:8080/api/media/files/2_851da11c-6975-443a-a151-04e6e578c239_Thong_bao_demo_1.pdf','2025-10-27 10:26:29'),(18,25,'pdf','http://localhost:8080/api/media/files/4_5d89a958-9f0a-45b2-99fb-3935107fbc46_Thong_bao_demo_1.pdf','2025-10-27 11:24:21'),(19,25,'pdf','http://localhost:8080/api/media/files/4_30d75771-abf7-4a6e-91b9-8b89f365932c_Thong_bao_demo_1.pdf','2025-10-27 11:24:21'),(24,28,'image','http://localhost:8080/api/media/files/4_212752dd-d086-481d-a91b-6628aa08ce1a_logo-cntt2021.png','2025-10-27 19:37:12'),(25,29,'image','http://localhost:8080/api/media/files/4_98774649-2565-453b-b0d7-1470196af3dd_ghế.jpg','2025-10-27 20:42:48'),(26,30,'image','http://localhost:8080/api/media/files/8_f197876d-da83-4455-9cfc-bb8aa8922caf_ute_logo.png','2025-10-28 00:47:23'),(27,31,'video','http://localhost:8080/api/media/files/8_f85a4c6e-cf5c-4e2f-bdc6-3076e722fff6_Form1 2025-04-28 11-54-45.mp4','2025-10-28 00:47:52'),(28,32,'pdf','http://localhost:8080/api/media/files/8_01279521-50f0-4b20-b3d2-efcafdd8367a_Thong_bao_demo_1.pdf','2025-10-28 00:48:10'),(29,33,'image','http://localhost:8080/api/media/files/4_8f85547c-b020-45fe-aab6-d653dcb0f1e4_ute_logo.png','2025-10-28 01:20:28'),(30,34,'video','http://localhost:8080/api/media/files/2_65d5dac2-007f-4742-9b98-d0b6b3bd1684_Form1 2025-04-28 11-54-45.mp4','2025-10-28 01:21:35');
/*!40000 ALTER TABLE `post_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_reactions`
--

DROP TABLE IF EXISTS `post_reactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_reactions` (
  `reaction_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  `reaction_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`reaction_id`),
  UNIQUE KEY `post_id` (`post_id`,`user_id`),
  UNIQUE KEY `UKqx1p0wtweq8mgrtenw93jdqns` (`post_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `post_reactions_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `post_reactions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_reactions`
--

LOCK TABLES `post_reactions` WRITE;
/*!40000 ALTER TABLE `post_reactions` DISABLE KEYS */;
INSERT INTO `post_reactions` VALUES (2,8,2,'love','2025-10-24 09:14:03'),(3,8,4,'angry','2025-10-24 09:14:10'),(5,14,4,'sad','2025-10-24 10:43:30'),(20,29,4,'like','2025-10-27 20:43:02'),(22,32,8,'sad','2025-10-28 00:48:34'),(23,31,8,'haha','2025-10-28 00:50:03'),(24,23,3,'like','2025-10-28 01:07:26'),(26,21,3,'like','2025-10-28 01:07:30'),(27,30,2,'haha','2025-10-28 06:24:38'),(28,32,4,'sad','2025-10-28 06:28:14');
/*!40000 ALTER TABLE `post_reactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_shares`
--

DROP TABLE IF EXISTS `post_shares`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_shares` (
  `share_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `shared_text` tinytext COLLATE utf8mb4_unicode_ci,
  `visibility` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`share_id`),
  KEY `fk_postshares_postid` (`post_id`),
  KEY `fk_postshares_userid` (`user_id`),
  CONSTRAINT `fk_postshares_postid` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_postshares_userid` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_shares`
--

LOCK TABLES `post_shares` WRITE;
/*!40000 ALTER TABLE `post_shares` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_shares` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `author_id` int NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `visibility` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`),
  KEY `author_id` (`author_id`),
  KEY `post_type` (`post_type`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (5,2,'Ngày lễ 2/9/2025 toàn trường được nghĩ 2 ngày','public','important','pending','2025-10-24 09:01:15','2025-10-24 09:01:15'),(6,2,'? HACKATHON MỞ RỘNG LẦN THỨ VII - 2025 CHÍNH THỨC MỞ ĐƠN ĐĂNG KÝ','public','important','pending','2025-10-24 09:06:05','2025-10-24 09:06:05'),(7,2,'cà chua ngon quá','public','normal','pending','2025-10-24 09:13:24','2025-10-24 09:13:24'),(8,2,'Công nghệ cao quá','public','normal','pending','2025-10-24 09:13:45','2025-10-24 09:13:45'),(11,4,'FIT nè','friends','normal','pending','2025-10-24 09:30:44','2025-10-24 09:30:44'),(12,3,'rung','private','normal','pending','2025-10-24 09:31:46','2025-10-24 09:31:46'),(13,4,'kí hiệu đẹp quá','public','normal','pending','2025-10-24 10:06:03','2025-10-24 10:06:03'),(14,4,'Hôm nay tôi buồn','public','normal','pending','2025-10-24 10:09:47','2025-10-24 10:09:47'),(15,4,'Các bạn cẩn thận không đăng các bài đăng quá khích về vụ việc tập đoàn QDT bị phá sản, nếu vi phạm sẽ bị BAN tài khoản.','public','important','pending','2025-10-24 10:17:03','2025-10-24 10:17:03'),(18,4,'[CẬP NHẬT LẠI DS ĐỀ TÀI][BÌNH CHỌN POSTER ĐỀ TÀI NGHIÊN CỨU KHOA HỌC THAM GIA GIẢI THƯỞNG SINH VIÊN NGHIÊN CỨU KHOA HỌC - EURÉKA LẦN THỨ 27 NĂM 2025]\nThân chào các bạn sinh viên,\nNhững ý tưởng sáng tạo, những công trình nghiên cứu đầy tâm huyết từ sinh viên trên khắp cả nước đang hội tụ tại Giải thưởng Sinh viên nghiên cứu khoa học - Euréka lần thứ 27 năm 2025.\n\nĐoàn trường Đại học Sư phạm Kỹ thuật TPHCM phát động hoạt động bình chọn cho các đề tài của nhà trường tham dự Giải thưởng Sinh viên nghiên cứu khoa học - Euréka lần thứ 27 năm 2025.\n\nQuyền lợi khi tham gia bình chọn: Được cộng 03 điểm rèn luyện khi bình chọn ít nhất 10 đề tài tham gia thuộc các lĩnh vực của Trường Đại học Sư phạm Kỹ thuật TPHCM.\n\nQuý vị và các bạn sinh viên cùng tham gia bình chọn đề tài nghiên cứu khoa học tham gia Giải thưởng Sinh viên Nghiên cứu Khoa học Euréka lần thứ 27 năm 2025 theo các bước dưới đây:\n\nBước 1: Truy cập vào Website https://binhchon.khoahoctre.com.vn/, kéo xuống phần bình chọn, chọn “Tham gia bình chọn” mục Bình chọn Poster.\n\nBước 2: Tìm kiếm mã số đề tài của các đề tài từ nhóm thí sinh của trường theo danh sách đính kèm => Bấm bình chọn\n\nLưu ý: Mỗi tài khoản chỉ được bình chọn 01 (một) lần cho mỗi đề tài, không giới hạn số lượng đề tài được bình chọn.\n\nBước 3: Chụp màn hình các bình chọn cho các đề tài, tải lên folder trên google drive cá nhân của các bạn và đặt tên folder theo cú pháp “Họ và tên – MSSV”.\n\nBước 4: Điền vào link google form dưới đây để cung cấp thông tin và minh chứng đã tham gia bình chọn.\n\nhttps://link.hcmute.edu.vn/e5a49807\n\nLưu ý: Phải đảm bảo rằng link drive chứa minh chứng tham gia đã được chia sẻ và không bị xóa folder đến hết năm học 2025-2026.\n\nThời gian bình chọn Từ 12g00 ngày 22/10/2025 (thứ Tư) đến 12g00 ngày 05/11/2025 (thứ Tư). Sau thời gian này, hệ thống sẽ tự động khóa chức năng bình chọn.\n\nCảm ơn các bạn sinh viên đã tham gia bình chọn!','public','important','pending','2025-10-24 10:47:29','2025-10-24 10:48:04'),(21,3,'vidoe hya','public','normal','pending','2025-10-24 20:31:05','2025-10-24 20:31:05'),(23,2,'Thông báo 2','public','normal','pending','2025-10-27 10:26:29','2025-10-27 10:26:29'),(24,2,'Thông báo 3','private','normal','pending','2025-10-27 10:55:35','2025-10-27 10:55:35'),(25,4,'Thông báo từ Admin 1, các em chú ý file PDF bên dưới.','public','normal','pending','2025-10-27 11:24:21','2025-10-27 11:24:21'),(28,4,'Khoa công nghệ thông tin có tin tức mới.','public','important','pending','2025-10-27 19:37:12','2025-10-27 19:37:12'),(29,4,'Ghế mới mua','friends','normal','pending','2025-10-27 20:42:48','2025-10-27 20:42:48'),(30,8,'anhe 1','public','normal','pending','2025-10-28 00:47:23','2025-10-28 00:47:23'),(31,8,'video 2','friends','normal','pending','2025-10-28 00:47:52','2025-10-28 00:47:52'),(32,8,'thong báo 3','public','normal','pending','2025-10-28 00:48:10','2025-10-28 00:48:10'),(33,4,'Ngày 28/10 bắt đầu đăng ký học phần đợt 2.','public','important','pending','2025-10-28 01:20:28','2025-10-28 01:20:28'),(34,2,'Sắp tới sẽ các cuộc thi hackathon.','public','important','pending','2025-10-28 01:21:35','2025-10-28 01:21:35');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_media`
--

DROP TABLE IF EXISTS `product_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_media` (
  `media_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `media_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`media_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_media_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_media`
--

LOCK TABLES `product_media` WRITE;
/*!40000 ALTER TABLE `product_media` DISABLE KEYS */;
INSERT INTO `product_media` VALUES (1,1,'/api/media/market/files/2_4cd16d99-cc47-4729-aefe-66e4f3befbde_but-bi-2.jpg','2025-10-24 09:09:07'),(13,4,'/api/media/market/files/4_38367ff6-224b-4b68-a726-66c6a6a1321f_arduinouno.jpg','2025-10-27 20:32:22'),(16,9,'/api/media/market/files/4_904d5546-980f-41dd-836e-f1f4c8df0e4a_ghế.jpg','2025-10-28 01:00:30'),(17,10,'/api/media/market/files/3_9e366711-5a99-43e6-bd72-b6d31b7c664e_chuột.jpg','2025-10-28 01:08:45');
/*!40000 ALTER TABLE `product_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `seller_id` int NOT NULL,
  `product_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` tinytext COLLATE utf8mb4_unicode_ci,
  `price` double NOT NULL,
  `quantity` int DEFAULT '1',
  `status` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  KEY `seller_id` (`seller_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,2,'Bút bi còn thừa','Bút còn thừa muốn Pass lại á',10000,1,'available','Thủ Đức','2025-10-24 09:09:07','2025-10-24 09:09:07'),(4,4,'arduino uno 3','mới dùng có 1 đồ án thôi còn mới lắm hihi',70000,1,'available','số 1, Quận Ba Đình, Thành phố Hà Nội','2025-10-24 10:58:27','2025-10-27 20:32:22'),(9,4,'ghế','ngon',100000,10,'available','1, Quận Ba Đình, Thành phố Hà Nội','2025-10-28 01:00:30','2025-10-28 01:00:30'),(10,3,'chuột','aaaa',100000,12,'available','','2025-10-28 01:08:45','2025-10-28 01:08:45');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `report_id` int NOT NULL AUTO_INCREMENT,
  `reporter_id` int NOT NULL,
  `reason` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reported_comment_id` int DEFAULT NULL,
  `reported_post_id` int DEFAULT NULL,
  `reported_product_id` int DEFAULT NULL,
  `reported_share_id` int DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  KEY `reporter_id` (`reporter_id`),
  KEY `fk_reports_post` (`reported_post_id`),
  KEY `fk_reports_comment` (`reported_comment_id`),
  KEY `fk_reports_product` (`reported_product_id`),
  KEY `fk_reports_share` (`reported_share_id`),
  CONSTRAINT `fk_reports_comment` FOREIGN KEY (`reported_comment_id`) REFERENCES `comments` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reports_post` FOREIGN KEY (`reported_post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reports_product` FOREIGN KEY (`reported_product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reports_share` FOREIGN KEY (`reported_share_id`) REFERENCES `post_shares` (`share_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `avatar_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '/defaults/avatar.png',
  `cover_photo_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '/defaults/cover.png',
  `bio` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `school_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `academic_year` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `education` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `workplace` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `instagram_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `linkedin_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `twitter_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_seen_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `oauth_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `oauth_provider` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_banned` tinyint(1) NOT NULL DEFAULT '0',
  `profile_visibility` enum('PUBLIC','FRIENDS','PRIVATE') COLLATE utf8mb4_unicode_ci DEFAULT 'PUBLIC',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `school_id` (`school_id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'23110359@student.hcmute.edu.vn','$2a$10$VSgwI33DS6/8K40NjtVqzOajfIWTxJottDpYbMMojEPxUOw5ora.S','Khoa Công Nghệ Thông Tin',NULL,NULL,NULL,NULL,'http://localhost:8080/api/media/files/2_3171d459-2894-40f3-99af-ad8173a223b6_logo-cntt2021.png','http://localhost:8080/api/media/files/2_903a5d53-0d6d-4c3b-9068-97beeb00e719_hackathon.jpg',NULL,NULL,NULL,NULL,'special',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-10-28 13:24:49','2025-10-24 08:46:49','2025-10-24 10:43:08',NULL,NULL,0,'PUBLIC'),(3,'23110370@student.hcmute.edu.vn','$2a$10$HC.thv2WJiS3/17MfcfUGeosT0EnH8mchj112akzIaFKyzjywReSC','hoa quả sơn',NULL,NULL,NULL,NULL,'http://localhost:8080/api/media/files/3_815d4f93-a15a-46b7-a152-573a8105ff6a_z7152488224724_592cb39512965c8e05cfc6b852641e1d.jpg','http://localhost:8080/api/media/files/3_e9fbb678-e238-4ed0-8422-f2e4ad47ca29_z7152488313742_268286adae46dfbeb85efcf3ace16f39.jpg','Tôi là Hòa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Tiến Sĩ','hcmute',NULL,NULL,NULL,NULL,'2025-10-28 13:25:05','2025-10-24 08:49:32','2025-10-26 16:41:57',NULL,NULL,0,'FRIENDS'),(4,'23110186@student.hcmute.edu.vn','$2a$10$Bl5E1Pgwf7jGtjsEbrTuouSuuK3zBK/13RXcKqnLMeNyApHIqkraS','Tôn Hoàng Cầm (Quản trị viên)',NULL,NULL,NULL,NULL,'http://localhost:8080/api/media/files/4_d9b259c6-4b94-4698-b45f-7361b03a15a8_Avatar_A_boy_with_Green.jpg','http://localhost:8080/api/media/files/4_04f5289b-3d70-4259-ba3f-eed3f6a2648d_Ghibli.jpg','tôi là admin',NULL,NULL,NULL,'admin','891231203',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2025-10-28 13:54:50','2025-10-24 08:51:28','2025-10-28 00:38:41',NULL,NULL,0,'FRIENDS'),(8,'23110296@student.hcmute.edu.vn','$2a$10$Sxbi1ZEb1FAplN3WLTfnpOhSXf0Zvg4j9qOyQ3m9lznIDhfv6Yc6S','Vu Anh Quoc',NULL,NULL,NULL,NULL,'http://localhost:8080/api/media/files/8_3403958d-876f-4879-ab81-e4f8b8c9041b_chuột.jpg','http://localhost:8080/api/media/files/8_f5bfdd5a-cacf-40d0-b284-50323e269b8a_hackathon.jpg',NULL,NULL,NULL,NULL,'special',NULL,NULL,NULL,'Hồ chí minh',NULL,NULL,NULL,NULL,NULL,NULL,'2025-10-28 12:54:02','2025-10-28 00:34:02','2025-10-28 01:27:15','104467671800315635346','google',0,'PUBLIC');
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

-- Dump completed on 2025-10-28 20:55:38
