# Network Speed

"You can not let hte primary get too far ahead of the backup because when the primary fails ahead, the backup will lagg behind. So every replication system has this problem that at some point the primary has to stall waiting for the backup, which is a real limit on performance."

"Even if the machines are like side-by-side and adjacent racks, it is still about **half a millisecond** or something."

"It's probably **5 milliseconds** if we replicate in the two replicas in different cities"  -- Robert Morris <in MIT 6.824 lecture 4 - primary-backup replication, Spring 2020>

<br/>

"AZs are physically separated by a meaningful distance from other AZs in the same AWS Region, although they all are within 60 miles (100 kilometers) of each other. This generally produces **single digit millisecond roundtrip latency** between AZs in the same Region. Roundtrip latency between two instances in the same AZ is generally **sub-millisecond** when using enhanced networking. This can be even lower when the instances use cluster placement groups." -- https://aws.amazon.com/blogs/architecture/improving-performance-and-reducing-cost-using-availability-zone-affinity/
