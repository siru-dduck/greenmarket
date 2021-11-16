import mongoose from 'mongoose';
import { MongoMemoryReplSet } from'mongodb-memory-server';

/**
 * Connect to the in-memory database.
 */
export const connect = async () => {
    const replica = await MongoMemoryReplSet.create({
        replSet: {count: 3}, 
        instanceOpts: [
            { port: 62510}, {port: 62511}, {port: 62512}
        ]
    });
    const uri = replica.getUri();
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
export const closeDatabase = async () => {
    await mongoose.connection.dropDatabase();
    await mongoose.connection.close();
    await mongod.stop();
}

/**
 * Remove all the data for all db collections.
 */
export const clearDatabase = async () => {
    const collections = mongoose.connection.collections;

    for (const key in collections) {
        const collection = collections[key];
        await collection.deleteMany();
    }
}