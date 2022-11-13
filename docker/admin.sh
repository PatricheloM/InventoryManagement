redis-server --daemonize yes && sleep 1; 
redis-cli < /root/admin.redis ;
redis-cli save;
redis-cli shutdown; 
redis-server /etc/redis/redis.conf;
