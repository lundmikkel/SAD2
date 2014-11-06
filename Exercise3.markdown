Exercise 3
============


**1. Design and analyse a MapReduce algorithm that transforms this set of pairs into another set of pairs that is more suitable for computing ActorRank.**

The input consists in a set of pairs `(movie id; author list)` where movie id is the identifier of a movie, and author list contains the identifiers of all actors that played in the movie
	
	//M assumed to be |V|
	//Emit adjacency lists(edges) for all authors
	Map(movie_id, author_list)
		for each author x in author_list
			for each author y in author_list
				emit(x, <y, 1/|V|>)
	
	//Emit result with no further action 
	Reduce(author x, author_ranks[<y1, ar1>, <y2, ar2> ... <yn, arn>])			
		emit(x, author_ranks)

**2. Design and analyse a MapReduce algorithm for computing the approximate ActorRank for given values of α and M**

	Map(author v, authors[<u1, ARu1>, <u2, ARu2> ... <un, ARun>])
		
		sum = 0
		for each <author u, rank ARu> in authors
			sum += ARu / |authors|
		
		ARi = alpha / |V| + (1 - alpha) * sum
		for each <author u, rank ARu> in authors
			emit(u, <v, ARi>)
		

	Reduce(<author u, authors[<v1, ARv1>, <v2, ARv2>, ... ,<vn, ARvn>])
		for each author v, rank ARv in authors
			emit(u, <v, ar>)