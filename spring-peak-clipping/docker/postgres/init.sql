CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS activities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_stock INTEGER NOT NULL,
    available_stock INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_activities_status ON activities(status);
CREATE INDEX idx_activities_start_time ON activities(start_time);
CREATE INDEX idx_activities_is_active ON activities(is_active);
CREATE INDEX idx_activities_status_active ON activities(status, is_active);

INSERT INTO activities (name, description, status, start_time, end_time, total_stock, available_stock, is_active, created_at, updated_at)
VALUES 
    ('双十一预热活动', '双十一预热，提前锁定优惠', 'not_started', NOW() + INTERVAL '1 hour', NOW() + INTERVAL '5 hours', 1000, 1000, true, NOW(), NOW()),
    ('限时秒杀', '每日限时秒杀活动', 'in_progress', NOW() - INTERVAL '1 hour', NOW() + INTERVAL '3 hours', 500, 320, true, NOW(), NOW()),
    ('新品首发', '新品首发限量抢购', 'not_started', NOW() + INTERVAL '30 minutes', NOW() + INTERVAL '6 hours', 200, 200, true, NOW(), NOW());
