package com.comprehensivetutorial.udemyroomdemo.database

class SubscriberRepository(private val dao: SubscriberDao) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber): Long = dao.insertSubscriber(subscriber)

    suspend fun update(subscriber: Subscriber) = dao.updateSubscriber(subscriber)

    suspend fun delete(subscriber: Subscriber) = dao.deleteSubscriber(subscriber)

    suspend fun deleteAll() = dao.deleteAll()

}