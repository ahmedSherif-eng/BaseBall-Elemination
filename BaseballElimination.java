/* *****************************************************************************
 *  Name: Ahmed Sherif
 *  Date: 08/02/202
 *  Description: Baseball Elimination Problem
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;


public class BaseballElimination {
    private final int[] win;
    private final int[] lose;
    private final int[] left;
    private final int[][] game;
    private final ArrayList<String> names;
    private final int size;
    private Queue<String> queue;
    private int toTCapacity = 0;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        try {
            In obj = new In(filename);
            size = obj.readInt();
            win = new int[size];
            lose = new int[size];
            left = new int[size];
            names = new ArrayList<String>(size);
            game = new int[size][size];
            queue = new Queue<>();
            for (int i = 0; i < size; i++) {
                names.add(i, obj.readString());
                win[i] = obj.readInt();
                lose[i] = obj.readInt();
                left[i] = obj.readInt();
                for (int j = 0; j < size; j++)
                    game[i][j] = obj.readInt();
            }
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return size;
    }

    // all teams
    public Iterable<String> teams() {
        return names;
    }

    // number of wins for given team
    public int wins(String team) {
        if (names.indexOf(team) == -1)
            throw new IllegalArgumentException();
        return win[names.indexOf(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (names.indexOf(team) == -1)
            throw new IllegalArgumentException();
        return lose[names.indexOf(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (names.indexOf(team) == -1)
            throw new IllegalArgumentException();
        return left[names.indexOf(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (names.indexOf(team1) == -1 || names.indexOf(team1) == -1)
            throw new IllegalArgumentException();
        return game[names.indexOf(team1)][names.indexOf(team2)];
    }

    // is given team eliminated?

    public boolean isEliminated(String team) {
        if (names.indexOf(team) == -1)
            throw new IllegalArgumentException();
        queue = new Queue<>();
        int index = names.indexOf(team);
        if (index != -1) {
            if (trivialSolution(index)) {
                return true;
            }
            else {
                FordFulkerson graph2 = nontrivialSolution(index);
                if (toTCapacity != graph2.value()) {
                    return true;
                }
                return false;
            }
        }
        else {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private boolean trivialSolution(int team) {
        if (team > size || team < 0)
            throw new IllegalArgumentException();
        boolean elim = false;
        for (int i = 0; i < size; i++) {
            if (team != i && win[team] + left[team] < win[i]) {
                elim = true;
                queue.enqueue(names.get(i));
                break;
            }
        }
        return elim;
    }

    private FordFulkerson nontrivialSolution(int team) {
        if (team > size || team < 0)
            throw new IllegalArgumentException();
        int games = ((size - 1) * size) / 2;
        FlowNetwork graph = new FlowNetwork(games + size + 2);
        int s = games + size;
        int t = games + size + 1;
        int vertex = 0;
        toTCapacity = 0;
        for (int i = 0; i < size; i++) {
            graph.addEdge(new FlowEdge(games + i, t, left[team] + win[team] - win[i]));
            for (int j = i + 1; j < size; j++) {
                graph.addEdge(new FlowEdge(vertex, games + i, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(vertex, games + j, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(s, vertex, game[i][j]));
                vertex++;
                toTCapacity += game[i][j];
            }
        }
        int index = 0;
        FordFulkerson graph2 = new FordFulkerson(graph, s, t);
        for (int i = games; i < games + size; i++) {
            if (graph2.inCut(i)) {
                queue.enqueue(names.get(index));
            }
            index++;
        }
        return new FordFulkerson(graph, s, t);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (names.indexOf(team) == -1)
            throw new IllegalArgumentException();
        queue = new Queue<String>();
        int index = names.indexOf(team);
        if (index != -1) {
            if (trivialSolution(index)) {
                System.out.println("queue : " + queue.toString());
                return queue;
            }
            else {
                nontrivialSolution(index);
                if (queue.size() == 0) {
                    return null;
                }
                return queue;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
