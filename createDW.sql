-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ELECTRONICA-DW
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ELECTRONICA-DW
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ELECTRONICA-DW`;
CREATE SCHEMA IF NOT EXISTS `ELECTRONICA-DW` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema ELECTRONICA-DW
-- -----------------------------------------------------
USE `ELECTRONICA-DW` ;

-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Customers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Customers`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Customers` (
  `Customer_ID` INT NOT NULL,
  `Customer_Name` VARCHAR(50) NULL,
  `Gender` CHAR(1) NULL,
  PRIMARY KEY (`Customer_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Products`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Products` (
  `Product_ID` INT NOT NULL,
  `Product_Name` VARCHAR(50) NULL,
  `Product_Price` FLOAT NULL,
  PRIMARY KEY (`Product_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Time`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Time`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Time` (
  `Time_ID` INT NOT NULL AUTO_INCREMENT,
  `Day` INT,
  `Week` INT,
  `Quarter` INT,
  `Month` INT,
  `Year` YEAR,
  PRIMARY KEY (`Time_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Store`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Store` (
  `Store_ID` INT NOT NULL,
  `Store_Name` VARCHAR(45) NULL,
  PRIMARY KEY (`Store_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Supplier`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Supplier`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Supplier` (
  `Supplier_ID` INT NOT NULL,
  `Supplier_Name` VARCHAR(45) NULL,
  PRIMARY KEY (`Supplier_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ELECTRONICA-DW`.`Sales`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ELECTRONICA-DW`.`Sales`;
CREATE TABLE IF NOT EXISTS `ELECTRONICA-DW`.`Sales` (
  `Transaction_ID` INT NOT NULL AUTO_INCREMENT,
  `Customer_ID` INT NOT NULL,
  `Product_ID` INT NOT NULL,
  `Time_ID` INT NOT NULL,
  `Store_ID` INT NOT NULL,
  `Supplier_ID` INT NOT NULL,
  `Quantity` INT NOT NULL,
  `Sale` FLOAT8 NOT NULL,
  PRIMARY KEY (`Transaction_ID`),
    FOREIGN KEY (`Customer_ID`)
    REFERENCES `ELECTRONICA-DW`.`Customers` (`Customer_ID`),
    FOREIGN KEY (`Product_ID`)
    REFERENCES `ELECTRONICA-DW`.`Products` (`Product_ID`),
    FOREIGN KEY (`Time_ID`)
    REFERENCES `ELECTRONICA-DW`.`Time` (`Time_ID`),
    FOREIGN KEY (`Store_ID`)
    REFERENCES `ELECTRONICA-DW`.`Store` (`Store_ID`),
    FOREIGN KEY (`Supplier_ID`)
    REFERENCES `ELECTRONICA-DW`.`Supplier` (`Supplier_ID`)
)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
