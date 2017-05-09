# spark-examples

I love the question `why ?` because its asking for the purpose of something.
That's `why` I would like to start with answering `why` :). 

So here, am going to tell you a story to answer why spark is out there and why we are interested in it. 

Again, it's going to be a story and I assume you to play the lead character in our fiary tale. we don't go much deeper 
into the technical details. (Stories usually don't have much technical stuff , right?)
 The story begins with a simple, single machine called computer. Following figure that depicts that computer.
 
 
 
 As you can see the key resources of this machine are CPU, Memory and Physical Storage. Now, you can use this machine to 
 carry out your data processing needs. Overtime you need out scale out your machine so that you can process more data and 
 persist more data. But, there is definitely a practical limit to the capacity of these three resources in a single 
 machine. Now, what could be the solution to this problem. Answer is simple right ? use a cluster or a collection of computers.
 But, I think about. As soon as you have more than one computer to store and process your data things are getting 
 complicated. For example, think  of file that that doesn't fit in a single machine. what will you do ? one soultion
 to split that file and into two and store one part in one machine and the other part in another machine. Does that solve
 the problem ? ok, what about processing that file ? You can't anymore treat that file as a single entity. They are 
 actually two files now and physically separated too. 
 
 Now, what if you have a interface on top of the physical machines you have, which will enable you to treat the cluster 
 of machines as a sinlge entity or in other words, a single more powerful machine? That is exactly want we wanted in the 
 beginning. 
 
 `Hadoop` is here for our rescue. Its give as an interface through which you can access a cluster of computer as if it is 
 a single machine. Another advantage is , we can easily scale out both storage and processing capacity without any 
 distrubance to our interface, scaling can take place under the hood. 
 
 Hadoop offers us two things. HDFS, which is an abstraction over the physical storage of your cluster (Not necessarily 
 a cluster always, I believe you guys are smart enough to understand it) and Map-Reduce a programming framework using which 
 you can process the data you have stored in HDFS in parallel The names itself is interesting  first HDFS: short 
 form for Hadoop Distributed File System. It is self describing right ? second Map-Reduce: as the name implies the processing 
 framework has two main phases map and reduce. Its interesting to note that what kind of problems we can solve using map-reduce 
 framework. Problems of kind divide and conquire can be implemented in Map-Reduce. The classical example is word count problem.
 You can read more about Map-Reduce [here](https://en.wikipedia.org/wiki/MapReduce).
 
`Hadoop` excels at batch processing. To be more precise HDFS + Map-Reduce, excels batch processing. But definetly there are more 
tasks you would like to carry out with Hadoop.  
 
 