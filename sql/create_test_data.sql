use spa_db;

-- 插入测试用户数据
INSERT INTO user (userAccount, userPassword, userName, userAvatar, userProfile, userRole)
VALUES
    ('admin', '734d2f66b73652996d2104ac306a33fe', 'Admin User', NULL, 'Admin of the system', 'admin'),
    ('user1', '734d2f66b73652996d2104ac306a33fe', 'John Doe', NULL, 'Regular user', 'user'),
    ('user2', '734d2f66b73652996d2104ac306a33fe', 'Jane Smith', NULL, 'Regular user', 'user'),
    ('user3', '734d2f66b73652996d2104ac306a33fe', 'Mike Johnson', NULL, 'Regular user', 'user');
COMMIT; -- 强制提交事务

-- 获取已插入用户的实际 ID，确保只获取一行
SELECT id INTO @user1Id FROM user WHERE userAccount = 'user1' LIMIT 1;
SELECT id INTO @user2Id FROM user WHERE userAccount = 'user2' LIMIT 1;
SELECT id INTO @user3Id FROM user WHERE userAccount = 'user3' LIMIT 1;

-- 插入 body_data 数据
INSERT INTO body_data (userId, height_cm, weight_kg, createTime, updateTime)
VALUES
    (1, 175.00, 70.00, NOW(), NOW()),  -- Admin 用户的 body_data
    (2, 180.00, 85.00, NOW(), NOW()),  -- user1 的 body_data
    (3, 165.00, 60.00, NOW(), NOW()),  -- user2 的 body_data
    (4, 170.00, 75.00, NOW(), NOW());  -- user3 的 body_data

-- 插入 Health Monitoring Data
INSERT INTO health_data (userId, heartRate, averageHeartRate, stepsPerMinute, sleepTime, deepSleep, lightSleep, remSleep, caloriesBurned, doctorName, createTime, updateTime)
VALUES
    (@user1Id, '68,70,72,71,73,70,72,73,69,70', 72, 105, 7.3, 28.00, 56.00, 16.00, 350, 'Dr. Smith', NOW(), NOW()),
(1, '70,72,73,71,74', 72.0, 78, 6.8, 30.0, 45.0, 25.0, 2100, 'Dr. Smith', '2024-10-25 08:00:00', '2024-10-25 08:00:00');
-- 插入 Medicine Plan 数据
INSERT INTO medicine_plan (userId, medicineName, dosage, alarmTime)
VALUES
    (@user1Id, 'Paracetamol', '1 times/day', '08:00:00'),
    (@user1Id, 'Ibuprofen', '1 time/day', '12:00:00'),
    (@user1Id, 'Vitamin C', '1 time/day', '18:00:00');

INSERT INTO schedules (content, title, userId, createTime, updateTime, startTime, endTime, isDelete) VALUES
     ('Complete project report', 'Work', 1, '2024-10-25 08:00:00', '2024-10-25 08:00:00', '2024-10-25 09:00:00', '2024-10-25 10:30:00', 0),
     ('Go to the gym', 'Exercise', 1, '2024-10-25 08:00:00', '2024-10-25 08:00:00', '2024-10-25 18:00:00', '2024-10-25 19:00:00', 0),
     ('Doctor appointment', 'Health', 2, '2024-10-25 08:00:00', '2024-10-25 08:00:00', '2024-10-25 14:00:00', '2024-10-25 15:00:00', 0),
     ('Dinner with friends', 'Leisure', 1, '2024-10-25 08:00:00', '2024-10-25 08:00:00', '2024-10-25 20:00:00', '2024-10-25 22:00:00', 0),
     ('Read a book', 'Personal Development', 3, '2024-10-25 08:00:00', '2024-10-25 08:00:00', '2024-10-25 15:00:00', '2024-10-25 16:00:00', 0);
