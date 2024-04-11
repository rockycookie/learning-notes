# RFC 2328 Reading

## In Short

### What
- It is designed to be run internal to a single Autonomous System.
- Each OSPF router maintains an identical database describing the Autonomous System's topology. From this database, a routing table is calculated by constructing a shortest-path tree.

### Why
- OSPF recalculates routes quickly in the face of topological changes
- OSPF provides support for equal-cost multipath.
- An area routing capability is provided, enabling an additional level of 
    - routing protection
    - reduction in routing protocol traffic

## Terminalogies

### Transit Network vs Stub Network
```
The Autonomous System's link-state database describes a directed
graph.  The vertices of the graph consist of routers and
networks.  A graph edge connects two routers when they are
attached via a physical point-to-point network.  An edge
connecting a router to a network indicates that the router has
an interface on the network. Networks can be either transit or
stub networks. Transit networks are those capable of carrying
data traffic that is neither locally originated nor locally
destined. A transit network is represented by a graph vertex
having both incoming and outgoing edges. A stub network's vertex
has only incoming edges.
```
