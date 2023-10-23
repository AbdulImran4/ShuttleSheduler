# ShuttleSheduler

Define the structure for BinomialQueue:
class BinomialQueue:
    - Create an empty list for binomial trees
    - Define insert, union, and deleteMax methods

    method insert(node):
        - Create a new binomial queue with the node
        - Use the union method to merge it with the current queue

    method union(anotherQueue):
        - Merge the two binomial queues
        - Ensure the resulting queue satisfies the binomial queue properties

    method deleteMax():
        - Find the tree with the maximum root
        - Remove this tree from the queue
        - Create a new binomial queue with the children of the removed tree
        - Union this new queue with the current queue
        - Return the maximum node

Create a ShuttleScheduler instance using BinomialQueue for scheduling shuttle stops:
scheduler = new ShuttleScheduler<BinomialQueue>()

Define halls with varying passenger loads:
jacarandaHall = new Hall("Jacaranda Hall", 100)
cypressHall = new Hall("Cypress Hall", 50)
liveOakHall = new Hall("Live Oak Hall", 75)

Create shuttle stops based on the halls they serve and assign them priorities based on passenger count:
stop1 = new ShuttleStop(1)
stop1.addHall(jacarandaHall)
stop1.addHall(cypressHall)
totalPassengers1 = stop1.getTotalPassengers()

stop2 = new ShuttleStop(2)
stop2.addHall(liveOakHall)
totalPassengers2 = stop2.getTotalPassengers()

Insert shuttle stops into the scheduler:
scheduler.insert(stop1, totalPassengers1)
scheduler.insert(stop2, totalPassengers2)

Send shuttles to stops with the most passengers first:
while not scheduler.isEmpty():
    maxStop = scheduler.deleteMax()
    print("Sending shuttle to: " + maxStop)


