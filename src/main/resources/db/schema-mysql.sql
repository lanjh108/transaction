drop database transaction_db;
create database transaction_db;



-- transaction_db.balance definition

CREATE TABLE `balance` (
                           `account` varchar(128) NOT NULL COMMENT '事务id',
                           `amount` decimal(10,0) DEFAULT NULL COMMENT '金额',
                           `uptime` datetime DEFAULT NULL COMMENT '创建时间',
                           PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- transaction_db.transactions definition

CREATE TABLE `transactions` (
                                `transaction_id` varchar(128) NOT NULL COMMENT '事务id',
                                `source_account` varchar(100) DEFAULT NULL COMMENT '源账户',
                                `destination_account` varchar(100) DEFAULT NULL COMMENT '目标账户',
                                `amount` decimal(10,0) DEFAULT NULL COMMENT '金额',
                                `uptime` datetime DEFAULT NULL COMMENT '创建时间',
                                PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;