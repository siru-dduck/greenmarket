const { Emitter } = require("@socket.io/redis-emitter");
const { createClient } = require("redis");

const redisClient = createClient(
    Number(process.env.REDIS_MASTER_SERVICE_PORT), 
    process.env.REDIS_MASTER_SERVICE_HOST);

const io = new Emitter(redisClient);

export const emit = (room, event, message) => {
    io.emit(event, message);
};
