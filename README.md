# Sports Elimination

The Baseball Elimination problem is an application of the Maximum Flow Problem.
In `Baseball.java`, I provide a solution to the problem as specified by `Princeton's Coursera Specification:` https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php

[![wakatime](https://wakatime.com/badge/user/1573cfda-f106-4c1b-87c2-64cdbd982d7a/project/018d845a-3fdb-4929-a71f-c1dfc86f166b.svg)](https://wakatime.com/badge/user/1573cfda-f106-4c1b-87c2-64cdbd982d7a/project/018d845a-3fdb-4929-a71f-c1dfc86f166b)
## Problem Description:
- There are n teams competing in a league. At each point during the season, each team has some number of wins, losses, and remaining games to play.

## Goal:
- To determine mathematically which teams have a chance of finishing the season in first place (with ties allowed).

## Solution:
**Implementation**: Uses the Ford Fulkerson algorithm to compute max flow.

`trivialSolution(int team)`: Determines if a team is eliminated (i.e. it is not possible for the team to finish in first place by the end of the season) through trivial elimination. 
- If the max number of games team x can win is less than the number of wins of some other team i, then team x is trivially eliminated.
  - If w[x] + r[x] < w[i], then team x is eliminated.

`nontrivialSolution(int team)`: Reduce the Baseball Elimination problem the the Max Flow Problem.
- I created a flow network where the source is a trivial vertex that is connected to all combinations of match-ups between teams i and j (as vertices). The edges from the source each have a weight of the games left between teams i and j. 
- Each match-up vertex (i, j) is then connected with team vertices i and j to ensure that one of the two teams earns of win, with edge weights set to positive infinity. 
- Finally all team vertices (x) are connected to a trivial sink. We give these edges a weight of w[x] + r[x] - w[i]. Since team i can win as many as w[i] + r[i], we prevent team x from winning more than that many games in total.
- If all edges that are pointing from s are full, then this is the same as assigning winners to all of the remaining games in such a way that no team wins more games than x. 
- If at least one edge pointing from s is not full, then there is no scenario in which team x can win in the season.

`certificateElim(String team)`: Returns the subset of teams that eliminates the given team.

## Dependencies:
- `algs4.jar`
- `stdlib.jar`
