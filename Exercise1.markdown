Exercise 1
==========
    
1. Can you find information about this problem, or a generalization, or related problems, in the Compendium of NP optimization problems?
----------------------------------------------------------------------------------------------------------------------------------------
Maximum Subset Sum


2. An approximate solution follows by considering movies in order of decreasing running times. Describe the algorithm and show that, in the worst case, this algorithm leaves up to W/2 minutes unused, while an optimal solution would use all the available minutes.
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     Start by sorting movies in descending order by run time
     foreach movie
        if movie run time is less than remaining space in W
            select it

The worst case is that all videos are `(W/2)+1` minutes.
This would only allow 1 video, and at most allow for `O(W/2)` space to be used.


3. Suggest a dynamic programming exact algorithm for this problem.
------------------------------------------------------------------
Maximum knapsack from the compendium


4. What happens if Alan wants to select movies in order to maximize the sum of the movie ratings, without exceeding the available W minutes? Propose and implement an algorithm. Can your algorithm guarantee a certain quality of answer?
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	Knapsack problem
	wi : time in minutes
	vi : movie rating


5. Compare the sorting algorithm and the dynamic programming algorithm on the real data sets
--------------------------------------------------------------------------------------------
