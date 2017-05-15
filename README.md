# spark-examples

I love the question `why ?` because it is asking for the purpose of something.
That is why I would like to start with answering `why` :). 

  So let me tell you a story. A story which will give answers to your questions - why spark is out there and why we are interested in it. We wont go too deep into the technical details ( Stories usually do not have a lot of technical stuff, right?). But I want you to imagine that you are the lead character in this fairy tale. The `Hero`!

  The story begins with a simple, single machine called computer. The three major resources of the computer are CPU, Memory and Physical  Storage. You can use this machine to carry out your data processing needs. Over time, you need to scale out your machine so that you can persist and process more data. But there is definitely a practical limit to the capacity of these three resources in a single machine. Now, what could be the solution to this problem? The Answer is simple. Use a cluster or a collection of computers.

  But wait! Take a step back and think about it. As soon as you have more than one computer to store and process your data, things are getting complicated. For example, think of a file that doesn't fit in a single machine. What will you do? One solution is to split that file into two and store one part in one machine and the other part in another machine. Does that solve the problem? And what about processing that file? You can't treat that file as a single entity anymore. They are actually two individual, physically separated files now.So, what can you do? What if you have an interface on top of your physical machines, which will enable you to treat the cluster of machines as a single entity; in other words, a single more powerful machine? That is exactly want we wanted in the beginning. 
 
 `Hadoop` is here for your rescue. It give you an interface through which you can access a cluster of computers as if it is a single machine. Another advantage is that we can easily scale out both storage and processing capacity without any disturbance to our interface, i.e scaling can take place under the hood.
 
Hadoop offers us two things: 
1) HDFS, which is an abstraction over the physical storage of your cluster (Not necessarily a cluster always).
2) Map-Reduce, a programming framework using which you can process the data you have stored in HDFS in parallel.
 
  The names itself are interesting. First, HDFS, the abbreviation for Hadoop Distributed File System, is self explanatory. Second, Map-Reduce, as the name implies the processing framework has two main phases map and reduce. It is interesting to see what kind of problems we can solve using a map-reduce framework. Problems which can be solved using divide and conqure method can be implemented in Map-Reduce. The classic example is the word count problem. (You can read more about Map-Reduce [here (https://en.wikipedia.org/wiki/MapReduce)).  
 
  `Hadoop` excels in batch processing. To be more precise, HDFS + Map-Reduce excels in batch processing. But definitely there are more tasks you would like to carry out with Hadoop than just batch processing.
 
  Some example of tasks you would like to carry out on your massive amount of data are data exploration (most time interactive querying) and iterative processing (for machine learning). With your processing capacity and memory, you may be interested in stream processing.
Thats where Spark shines, or thats why Spark came into existence.
