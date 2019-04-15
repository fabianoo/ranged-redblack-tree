# Ranged Red-Black Tree
Algorithm for removing and inserting ranges from a given ranges set

# The Coding Problem

Write a program that will subtract one list of time ranges from another. Formally: for two
lists of time ranges A and B, a time is in (A-B) if and only if it is part of A and not part of
B.

A time range has a start time and an end time. You can define times and time ranges
however you want (Unix timestamps, date/time objects in your preferred language, the
actual string “start-end”, etc).
Your solution shouldn’t rely on the granularity of the timestamps (so don’t, for example,
iterate over all the times in all the ranges and check to see if that time is “in”).

Examples:

- (9:00-10:00) `minus` (9:00-9:30) = (9:30-10:00)
- (9:00-10:00) `minus` (9:00-10:00) = ()
- (9:00-9:30) `minus` (9:30-15:00) = (9:00-9:30)
- (9:00-9:30, 10:00-10:30) `minus` (9:15-10:15) = (9:00-9:15, 10:15-10:30)
- (9:00-11:00, 13:00-15:00) `minus` (9:00-9:15, 10:00-10:15, 12:30-16:00) = (9:15-10:00, 10:15-11:00) 


# The Solution

As a naive linear search approach would not be acceptable due to its O(n) complexity being highly ineffective when the data gets bigger, I chose to use a Binary Search Tree based solution.
For the tree representation, I chose to use an explicit over an implicit approach since it's more readable.
Anyway, the benefits of an implicit approach, in this case, I think do not worth it.

The solution is inspired in two classical algorithms: `The Red-Black Tree` and `The Interval Tree`.
Therefore, we can benefit of a robust O(log n) algorithm with with the fail fast properties of the `Interval Tree`.
 
Below, we can compare the Linear approach with the RB Tree:

| Operation | Linear | Our RB Tree 
| --- | --- | --- |
| Space/Storage | O(n) | O(n)
| Insertion | O(1) | O(log n)
| Deletion | O(n) | O(log n)
| Finding Intersection | O(n) | O(log n)
| Finding Every Intersection | O(n) | R x O(log n)

This way, we garantee that our application

# Technology

Since I have little knowledge about Golang, I chose Java since it's a widespread language.

While I'm using Java I could use the `java.util.TreeMap` implementation from its native API that is a `Black-Red Tree` implementation but I think it would undermine the proposal of this experiment.

Also, I used generics so we can apply this `Red-Black Tree` for any kind of ranges as long as the type can be comparable (extending `Comparable` interface). 

# Running and Testing