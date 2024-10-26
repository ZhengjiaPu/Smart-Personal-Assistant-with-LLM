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
    (1845693955367346178, 175.00, 70.00, NOW(), NOW()),  -- Admin 用户的 body_data
    (1845693955367346179, 180.00, 85.00, NOW(), NOW()),  -- user1 的 body_data
    (1845693955367346180, 165.00, 60.00, NOW(), NOW()),  -- user2 的 body_data
    (1845693955367346181, 170.00, 75.00, NOW(), NOW());  -- user3 的 body_data
