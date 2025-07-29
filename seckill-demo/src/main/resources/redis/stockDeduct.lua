-- KEYS[1] 库存 key
-- ARGV[1] 本次购买数量
local stock = tonumber(redis.call('GET', KEYS[1]))
if (not stock) then return -1 end
if (stock < tonumber(ARGV[1])) then return -2 end
redis.call('DECRBY', KEYS[1], ARGV[1])
return stock - tonumber(ARGV[1])