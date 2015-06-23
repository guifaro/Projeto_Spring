-- Create syntax for TABLE 'tipoproduto'
CREATE TABLE `tipoproduto` (
  `tipoproduto_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tipoproduto_pagamento` varchar(100) NOT NULL,
  `tipoproduto_name` varchar(200) NOT NULL,
  `tipoproduto_unico` bit(1) NOT NULL,
  PRIMARY KEY (`tipoproduto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'user'
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_admin` bit(1) DEFAULT NULL,
  `user_login` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_LOGIN` (`user_login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;