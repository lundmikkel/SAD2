Actor Rank
----------


**1. Design and analyse a MapReduce algorithm that transforms this set of pairs into another set of pairs that is more suitable for computing ActorRank.**

The input consists of a set of pairs `(movie id; author list)` where movie id is the identifier of a movie, and author list contains the identifiers of all actors that played in the movie
	
	//Emit adjacency lists(edges) for all authors
	Map(movie_id, author_list)
		for each author x in author_list
			for each author y in author_list
				emit(x, <y, 1/|V|>)
	
	//Emit result with no further action 
	Reduce(author x, author_ranks[<y1, ar1>, <y2, ar2>, ..., <yn, arn>])			
		emit(x, author_ranks)

**2. Design and analyse a MapReduce algorithm for computing the approximate ActorRank for given values of α and M**

	Map(author v, authors[<u1, ARu1>, <u2, ARu2>, ..., <un, ARun>])
		
		sum = 0
		for each <author u, rank ARu> in authors
			sum += ARu
		
		ARv = alpha / |V| + (1 - alpha) * sum / |authors|
		
		for each <author u, rank ARu> in authors
			emit(u, <v, ARv>)

	Reduce(<author u, authors[<v1, ARv1>, <v2, ARv2>, ... ,<vn, ARvn>]>)
		emit(u, authors)

Actor Triplets
----------

**IMDb would like to find all groups of three actors that have played in at least one movie.**
Design and analyse a MapReduce algorithm that find all triplets of actors that have played together at least once. The input consists in a set of pairs `(movie id; author list)` where movie id is the identifier of a movie, and author list contains the identifiers of all actors that played in the movie. The required output is a set of pairs `(< actor1, actor2, actor3 >; $)` containing all triplets of actors that played togetehr in at least one movie; an actor
triplet must appear exactly once.


Assumption: Authors can be compared (v<u) firstly by degree, and secondly by some other measure for tie breaking (unique id?)

___

**Round 1**

    Map(movie id, author list)
    	for each author u in author list
    		for each author v in author list
    			if u < v then emit(<u, $>; v) //u is responsible for the triangle
    			else emit(<v, $>; u)          //v is responsible for the triangle
    			emit(<u,v>, $)                //propagate the input graph to the next round
    
    Reduce(pair <author u, author v>; authors[u1, u2, ..., un])
        if(v != $) emit(<u,v>; $) and return; //propagate the input graph to the next round
        
        for each author x in authors
            for each author y in authors
                if (x < y) emit((x,y); u) //path x-u-y

**Round 2**

    Map(edge <author u, author v>; author x)
        emit(<u,v>;x) //Just propagate the pairs
    
    Reduce(edge <author u, author v>; authors[x1, x2, ..., xn])
        if($ in authors) //Meaning there is a connection between author u and v
            for each author x in authors
                if (x != $)                
                    emit(<u, v, x>, $)
