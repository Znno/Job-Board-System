-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 25, 2024 at 01:56 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jbs`
--

-- --------------------------------------------------------

--
-- Table structure for table `applicants_details`
--

CREATE TABLE `applicants_details` (
  `id` int(11) NOT NULL,
  `cv` longblob NOT NULL,
  `jobseeker_id` int(11) NOT NULL,
  `employer_id` int(11) NOT NULL,
  `state` enum('accepted','rejected','pending') NOT NULL DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `job_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `employer`
--

CREATE TABLE `employer` (
  `id` int(5) NOT NULL,
  `user_id` int(5) NOT NULL,
  `name` varchar(20) NOT NULL,
  `companyName` varchar(50) NOT NULL,
  `History` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employer`
--

INSERT INTO `employer` (`id`, `user_id`, `name`, `companyName`, `History`) VALUES
(19, 57, 'employer', 'companyName', 'history');

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `id` int(5) NOT NULL,
  `employer_id` int(5) NOT NULL,
  `title` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `requirements` varchar(255) NOT NULL,
  `Location` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `jobs`
--
DELIMITER $$
CREATE TRIGGER `before_job_delete` BEFORE DELETE ON `jobs` FOR EACH ROW BEGIN
    -- Delete related rows in the applicant_table
    DELETE FROM applicants_details
    WHERE job_id = OLD.id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `jobseeker_profile`
--

CREATE TABLE `jobseeker_profile` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `experience` text DEFAULT NULL,
  `education` text DEFAULT NULL,
  `date_created` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jobseeker_profile`
--

INSERT INTO `jobseeker_profile` (`id`, `user_id`, `name`, `location`, `experience`, `education`, `date_created`, `date_updated`) VALUES
(16, 55, 'admin', 'location', 'experience', 'education', '2024-12-24 16:28:13', '2024-12-24 16:28:13'),
(17, 56, 'jobseeker', 'location', 'experience', 'education', '2024-12-24 16:28:48', '2024-12-24 16:29:19'),
(18, 58, 'Ibrahem', 'location', 'experience', 'education', '2024-12-24 21:32:18', '2024-12-24 21:32:18'),
(19, 59, 'hdad', 'location', 'experience', 'education', '2024-12-24 21:39:53', '2024-12-24 21:39:53'),
(20, 60, 'ziad', 'location', 'experience', 'education', '2024-12-24 21:43:49', '2024-12-24 21:43:49'),
(21, 61, 'ahmed', 'location', 'experience', 'education', '2024-12-24 21:47:01', '2024-12-24 21:47:01'),
(22, 62, 'test', 'location', 'experience', 'education', '2024-12-24 21:49:20', '2024-12-24 21:49:20'),
(23, 63, 'mohamed', '', '', '', '2024-12-24 22:01:34', '2024-12-24 22:01:34');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `userName` varchar(255) NOT NULL,
  `password` varchar(100) NOT NULL,
  `userType` enum('employer','jobSeeker','admin') NOT NULL,
  `isActive` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `email`, `userName`, `password`, `userType`, `isActive`) VALUES
(55, 'admin@gmail.com', 'admin', 'dd474e450473186ec733689b549a94a54a96f276dba76b29138c57b6afe15bf7', 'admin', 1),
(56, 'fadf@gmail.com', 'jobseeker', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(57, 'adfa@gmail.com', 'employer', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'employer', 1),
(58, 'Ibrahem@gmail.com', 'Ibrahem', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(59, 'hdad@gmail.com', 'hdad', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(60, 'ziad@gmail.com', 'ziad', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(61, 'ahmed@gmail.com', 'ahmed', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(62, 'test@gmail.com', 'test', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1),
(63, 'mohamed@gmail.com', 'mohamed', '5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3', 'jobSeeker', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `applicants_details`
--
ALTER TABLE `applicants_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`jobseeker_id`),
  ADD KEY `employer_id` (`employer_id`),
  ADD KEY `job_id` (`job_id`);

--
-- Indexes for table `employer`
--
ALTER TABLE `employer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `employer_id` (`employer_id`);

--
-- Indexes for table `jobseeker_profile`
--
ALTER TABLE `jobseeker_profile`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `userName` (`userName`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `email_2` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `applicants_details`
--
ALTER TABLE `applicants_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `employer`
--
ALTER TABLE `employer`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `jobs`
--
ALTER TABLE `jobs`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `jobseeker_profile`
--
ALTER TABLE `jobseeker_profile`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `applicants_details`
--
ALTER TABLE `applicants_details`
  ADD CONSTRAINT `applicants_details_ibfk_1` FOREIGN KEY (`jobseeker_id`) REFERENCES `jobseeker_profile` (`id`),
  ADD CONSTRAINT `applicants_details_ibfk_2` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`),
  ADD CONSTRAINT `job_id` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`);

--
-- Constraints for table `employer`
--
ALTER TABLE `employer`
  ADD CONSTRAINT `employer_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`userID`);

--
-- Constraints for table `jobs`
--
ALTER TABLE `jobs`
  ADD CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`employer_id`) REFERENCES `employer` (`id`);

--
-- Constraints for table `jobseeker_profile`
--
ALTER TABLE `jobseeker_profile`
  ADD CONSTRAINT `jobseeker_profile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`userID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
