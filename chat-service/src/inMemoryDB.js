// tests/db-handler.js

const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');

const mongod = new MongoMemoryServer({
    instance: {
        port: 51877
    }
});

/**
 * Connect to the in-memory database.
 */
module.exports.connect = async () => {
    await mongod.start();
    const uri = mongod.getUri();

    const mongooseOpts = {
        useNewUrlParser: true,
        useUnifiedTopology: true
    };

    mongoose.set("debug", true); 
    mongoose.connect(uri, mongooseOpts);
    const connection = mongoose.connection;
    connection.once("open", () => console.log(`✅ Embeded Mongo DB is connected ${uri}`)); 
    connection.on("error", (error) => console.log(`❌ Error on Embeded Mongo DB Connection: ${error}`));
}

/**
 * Drop database, close the connection and stop mongod.
 */
module.exports.closeDatabase = async () => {
    await mongoose.connection.dropDatabase();
    await mongoose.connection.close();
    await mongod.stop();
}

/**
 * Remove all the data for all db collections.
 */
module.exports.clearDatabase = async () => {
    const collections = mongoose.connection.collections;

    for (const key in collections) {
        const collection = collections[key];
        await collection.deleteMany();
    }
}