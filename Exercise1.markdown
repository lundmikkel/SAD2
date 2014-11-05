Exercise 1
==========
    
Algorithm design case study: On-demand video streaming
------------------------------------------------------

**1. Can you find information about this problem, or a generalization, or related problems, in the Compendium of NP optimization problems?**

Maximum Subset Sum


**2. An approximate solution follows by considering movies in order of decreasing running times. Describe the algorithm and show that, in the worst case, this algorithm leaves up to W/2 minutes unused, while an optimal solution would use all the available minutes.**

     Start by sorting movies in descending order by run time
     foreach movie
        if movie run time is less than remaining space in W
            select it

The worst case is that all videos are `(W/2)+1` minutes.
This would only allow 1 video, and at most allow for `O(W/2)` space to be used.


**3. Suggest a dynamic programming exact algorithm for this problem.**

Maximum knapsack from the compendium


**4. What happens if Alan wants to select movies in order to maximize the sum of the movie ratings, without exceeding the available W minutes? Propose and implement an algorithm. Can your algorithm guarantee a certain quality of answer?**

	Knapsack problem
	wi : time in minutes
	vi : movie rating


**5. Compare the sorting algorithm and the dynamic programming algorithm on the real data sets**

Algorithm design case study: Organizing a movie club
----------------------------------------------------

**1. Can you find information about this problem, or a generalization, or related problems, in the Compendium of NP optimization problems?**

Vertex cover: Every edge is an actor connecting his two best-rated movies. Find a subset of vertices that covers all edges.

**2. Describe the problem as an Integer Program. How can this formulation be used for solving the problem?**

Let x<sub>i</sub> denote the inclusion of vertex *i*. If x<sub>i</sub> is 1, vertex *i* is included, if it is 0 it is not.
 Minimize the **sum of x<sub>i</sub>** for all vertices, such that **x<sub>i</sub> + x<sub>j</sub> &ge; 1** for all (i,j) in E.  

**3. Suggest a greedy approximation algorithm for selecting the movies.**

Sort movies in descending order based on the number of actors in the movies. Choose each movie if it covers an actor not previously covered.

**4. Find the set using the IMDB dataset D1. Report the size of the set you found on learnIT.**

