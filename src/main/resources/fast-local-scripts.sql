-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema fast
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `fast` ;

-- -----------------------------------------------------
-- Schema fast
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fast` DEFAULT CHARACTER SET utf8 ;
USE `fast` ;

-- -----------------------------------------------------
-- Table `fast`.`authentication`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`authentication` ;

CREATE TABLE IF NOT EXISTS `fast`.`authentication` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(200) NULL DEFAULT NULL,
  `token` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `UK_otqifl6rcl0u87c0ftkpn4b1` (`email` ASC),
  UNIQUE INDEX `UK_1onqo0ddylb38iwxpjw31w8ly` (`username` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `fast`.`office`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`office` ;

CREATE TABLE IF NOT EXISTS `fast`.`office` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `address1` VARCHAR(45) NULL DEFAULT NULL,
  `address2` VARCHAR(45) NULL DEFAULT NULL,
  `icon` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `fast`.`skill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`skill` ;

CREATE TABLE IF NOT EXISTS `fast`.`skill` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `skill` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `fast`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`user` ;

CREATE TABLE IF NOT EXISTS `fast`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NULL DEFAULT NULL,
  `lastname` VARCHAR(45) NULL DEFAULT NULL,
  `address1` VARCHAR(45) NULL DEFAULT NULL,
  `address2` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NOT NULL,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  `experience` VARCHAR(45) NULL DEFAULT NULL,
  `icon` VARCHAR(45) NULL DEFAULT NULL,
  `username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `fast`.`user_office`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`user_office` ;

CREATE TABLE IF NOT EXISTS `fast`.`user_office` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userid` INT(11) NOT NULL,
  `officeid` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `office_fk_user_idx` (`userid` ASC),
  INDEX `office_fk_offices_idx` (`officeid` ASC),
  CONSTRAINT `user_office_fk_offices`
    FOREIGN KEY (`officeid`)
    REFERENCES `fast`.`office` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_office_fk_users`
    FOREIGN KEY (`userid`)
    REFERENCES `fast`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `fast`.`user_skill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fast`.`user_skill` ;

CREATE TABLE IF NOT EXISTS `fast`.`user_skill` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userid` INT(11) NOT NULL,
  `skillid` INT(11) NOT NULL,
  `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `userid_fk_user_idx` (`userid` ASC),
  INDEX `skillid_fk_skill_idx` (`skillid` ASC),
  CONSTRAINT `user_skill_fk_skill`
    FOREIGN KEY (`skillid`)
    REFERENCES `fast`.`skill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_skill_fk_user`
    FOREIGN KEY (`userid`)
    REFERENCES `fast`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
