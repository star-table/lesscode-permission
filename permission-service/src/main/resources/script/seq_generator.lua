if (redis.call('exists', KEYS[1]) == 1) then
    return redis.call('incrby', KEYS[1], ARGV[1]);
end
return -1;