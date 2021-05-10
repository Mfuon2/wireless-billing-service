--Client Account
INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90001', '254722123451','Test Account 1', '247247', 'test1@gmail.com','PRE_PAID',0);
INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90002', '254722123452','Test Account 2', '247247', 'test2@gmail.com','PRE_PAID',0);
INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90003', '254722123453','Test Account 3', '247247', 'test3@gmail.com','PRE_PAID',0);

INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90004', '254722123454','Demo Account A', '247247', 'demo1@gmail.com','PRE_PAID',0);
INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90005', '254722123455','Demo Account B', '247247', 'demo2@gmail.com','PRE_PAID',0);
INSERT INTO client_account(account_number, msisdn,account_name,short_code,email_address,service_type, version) VALUES('VUKA90006', '254722123456','Demo Account C', '247247', 'demo3@gmail.com','PRE_PAID',0);

--Service Package / Product
INSERT INTO service_package(id, name, code, description, price, cycle, version) VALUES(991, 'VUKA 20', 'VUKA_20', '1 hour unlimited', 20, 'HOURLY', 0);
INSERT INTO service_package(id, name, code, description, price, cycle, version) VALUES(992, 'VUKA 50', 'VUKA_50', '24 hours unlimited', 50, 'DAILY', 0);
INSERT INTO service_package(id, name, code, description, price, cycle, version) VALUES(993, 'VUKA 250', 'VUKA_250', '1 week unlimited', 250, 'WEEKLY', 0);
INSERT INTO service_package(id, name, code, description, price, cycle, version) VALUES(994, 'VUKA HOME 2500', 'VUKA_HOME_2500', '1 month unlimited', 2500, 'MONTHLY', 0);

--Subscription
INSERT INTO client_subscription(id, client_account_id, service_package_id, subscription_plan, version) VALUES (99001, 'VUKA90001', 991, 'PERSONAL', 0);
INSERT INTO client_subscription(id, client_account_id, service_package_id, subscription_plan, version) VALUES (99002, 'VUKA90002', 992, 'PERSONAL', 0);
INSERT INTO client_subscription(id, client_account_id, service_package_id, subscription_plan, version) VALUES (99003, 'VUKA90003', 993, 'PERSONAL', 0);
INSERT INTO client_subscription(id, client_account_id, service_package_id, subscription_plan, version) VALUES (99004, 'VUKA90004', 994, 'PERSONAL', 0);

--Wallet
INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991001,'VUKA90001','PRE_PAID', 1000, 0);
INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991002,'VUKA90002','PRE_PAID', 10000, 0);
INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991003,'VUKA90003','PRE_PAID', 15000, 0);

INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991004,'VUKA90004','POST_PAID', 0, 0);
INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991005,'VUKA90005','POST_PAID', 0, 0);
INSERT INTO client_wallet(id, client_account_id, service_type, balance, version) VALUES(9991006,'VUKA90006','POST_PAID', 0, 0);
