#SAD2 Project - Group A

Problem 1: Better actor ratings
-------------------------------
In exercise III we calculated the rating of each actor, based on the number *and* rating of other actors that this actor has collaborated with.

We want to extend this calculation to also incorporate the movie ratings into this calcuation.

In essence we are adding weights to the collaboration graph, such that each collaboration is an edge in the graph, and the weight of an edge is the rating of the movie that was collaborated on.
![](weighted3.png)  
In this graph parallel edges are possible, since it is important to capture the fact that some actors collaborated on multiple (better or worse) movies.

A part of the equation from exercise III was to compute the average rating of collaborating actors. What we want to do is to compute a weighted average instead, such that less successful movies have less influence on the final rating.


* Actors are rated based on the rating of adjacent actors
* Actor adjacencies are weighted based on the rating of the movie. 
* Collaborating on a bad movie makes the collaboration count less when calculating the actor rating

Problem 2: Movie budget
-----------------------
From the output of problem 1 we want to be able to cast movies such that:

* The movie has a budget (some maximum allowed sum of actor-ratings)
* We want to maximize the utilization of this budget, i.e. have the least possible amount actor-rating left after assigning roles.  

Ideally we would have liked to have information about salary for actors such that the budget would be measured on a more fitting scale. The solution to this problem would be to do knapsack.
Since we do not have this information and makes the cost and the value of an actor the same, the solution to this problem is to solve subset-sum. 
