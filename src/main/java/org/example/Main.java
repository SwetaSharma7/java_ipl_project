package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String matchPath = "/home/sweta/Desktop/data/matches.csv";
        String deliveryPath = "/home/sweta/Desktop/data/deliveries.csv";

        List<Matches> matches = readMatchesFromCSV(matchPath);
//        System.out.print(matches);
        List<Deliveries> deliveries = readDeliveriesFromCSV(deliveryPath);
//        System.out.println(deliveries);

        findMatchesTeamsPlayedPerYear(matches);
        findMatchesWonByTeams(matches);
        findExtraRunsConcededPerTeamIn2016(matches,deliveries,2016);
        findTopEconomicalBowlerIn2015(matches,deliveries,2015);

    }
    public static List<Matches>readMatchesFromCSV(String csvFilePath){
        List<Matches> matches = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] fields = line.split(",");

                Matches match = new Matches();

                match.setId(Integer.parseInt(fields[0].trim()));
                match.setSeason(Integer.parseInt(fields[1].trim()));
                match.setCity(fields[2].trim());
                match.setDate(fields[3].trim());
                match.setTeam1(fields[4].trim());
                match.setTeam2(fields[5].trim());
                match.setTossWinner(fields[6].trim());
                match.setTossDecision(fields[7].trim());
                match.setResult(fields[8].trim());
                match.setDlApplied(Integer.parseInt(fields[9].trim()));
                match.setWinner(fields[10].trim());
                match.setWinByRuns(Integer.parseInt(fields[11].trim()));
                match.setWinByWickets(Integer.parseInt(fields[12].trim()));
                match.setPlayerOfMatch(fields[13] .trim());
                match.setVenue(fields[14].trim());

                matches.add(match);
            }
        }catch(IOException e) {
                e.printStackTrace();
        }
        return matches;
    }
    public static List<Deliveries> readDeliveriesFromCSV(String csvFilePath) {
        List<Deliveries> deliveries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");

                Deliveries delivery = new Deliveries();

                delivery.setMatchId(Integer.parseInt(fields[0].trim()));
                delivery.setInning(Integer.parseInt(fields[1].trim()));
                delivery.setBattingTeam(fields[2].trim());
                delivery.setBowlingTeam(fields[3].trim());
                delivery.setOver(Integer.parseInt(fields[4].trim()));
                delivery.setBall(Integer.parseInt(fields[5].trim()));
                delivery.setBatsman(fields[6].trim());
                delivery.setNonStriker(fields[7].trim());
                delivery.setBowler(fields[8].trim());
                delivery.setIsSuperOver(Integer.parseInt(fields[9].trim()));
                delivery.setWideRuns(Integer.parseInt(fields[10].trim()));
                delivery.setByeRuns(Integer.parseInt(fields[11].trim()));
                delivery.setLegByeRuns(Integer.parseInt(fields[12].trim()));
                delivery.setNoBallRuns(Integer.parseInt(fields[13].trim()));
                delivery.setPenaltyRuns(Integer.parseInt(fields[14].trim()));
                delivery.setBatsmanRuns(Integer.parseInt(fields[15].trim()));
                delivery.setExtraRuns(Integer.parseInt(fields[16].trim()));
                delivery.setTotalRuns(Integer.parseInt(fields[17].trim()));

                deliveries.add(delivery);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deliveries;
    }

//    questons:
//    Number of matches played per year of all the years in IPL.

    public static void findMatchesTeamsPlayedPerYear(List<Matches> matches) {

        Map<Integer, Integer> noOfMatches = new TreeMap<>();

        for(Matches match: matches){
            int matchSeason = match.getSeason();
            int prevMatches = noOfMatches.getOrDefault(matchSeason,0);
            noOfMatches.put(matchSeason, prevMatches+1);
        }
        System.out.println("No of Matches played per year:");
        for(var entry : noOfMatches.entrySet()){
            System.out.println("season: " + entry.getKey() + ", No of matches played: " + entry.getValue());
        }
    }

//    Number of matches won of all teams over all the years of IPL.
public static void findMatchesWonByTeams(List<Matches> matches) {
    Map<String, Integer> matchesWonByTeam = new TreeMap<>();

    for ( Matches match : matches) {
        String teamName = match.getWinner();
        int noOfWin = matchesWonByTeam.getOrDefault(teamName, 0);
        matchesWonByTeam.put(teamName, noOfWin+1);

    }

    System.out.println("Matches won by per team is :-");

    for(Map.Entry<String,Integer> elem : matchesWonByTeam.entrySet()){
        System.out.println("Team Name : " + elem.getKey() + ", Matches won : " + elem.getValue());
    }

}

//     finding id of particular year.
    public static Set<Integer> getMatchYear(List<Matches> matches, int year){
        Set<Integer> matchYearId = new HashSet<>();
        for(Matches match : matches){
            if(match.getSeason() == year){
                matchYearId.add(match.getId());
            }
        }
        return matchYearId;
    }


//    For the year 2016 get the extra runs conceded per team.
    public static void findExtraRunsConcededPerTeamIn2016(List<Matches> matches, List<Deliveries> deliveries, int year) {
        Set<Integer> matchId = getMatchYear(matches, year);
        Map<String, Integer> extraRunByTeams = new TreeMap<>();

        for(Deliveries delivery : deliveries){
            int id = delivery.getMatchId();
            if(matchId.contains(id)){
                int extraRuns = delivery.getExtraRuns();
                String teamName = delivery.getBowlingTeam();
                int presentRun = extraRunByTeams.getOrDefault(teamName, 0);
                extraRunByTeams.put(teamName, presentRun + extraRuns);
            }

            }
        System.out.println("Extra run conceeded by team at the year of " + year);

        for(var entry : extraRunByTeams.entrySet()){
            System.out.println("Team Name: " + entry.getKey() + " => Extra run conceded: " + entry.getValue());
        }

    }

//    For the year 2015 get the top economical bowlers.
public static void findTopEconomicalBowlerIn2015(List<Matches> matches,List<Deliveries> deliveries,int year) {
    Set<Integer> matchYearId = getMatchYear(matches, year);
    Map<String, Integer> bowlerRuns = new HashMap<>();
    Map<String, Integer> bowlerBalls = new HashMap<>();
    Map<String, Float> bowlersEconomy = new HashMap<>();

    float lowestEconomy = Float.MAX_VALUE;

    for (Deliveries delivery : deliveries) {
        int id = delivery.getMatchId();

        if (matchYearId.contains(id)) {
            String bowler = delivery.getBowler();

            int currRuns = delivery.getTotalRuns();
            int prevRuns = bowlerRuns.getOrDefault(bowler, 0);
            int prevBalls = bowlerBalls.getOrDefault(bowler, 0);

            bowlerRuns.put(bowler, prevRuns + currRuns);
            bowlerBalls.put(bowler, prevBalls + 1);
        }
    }

    bowlerRuns.forEach((bowler, runs) -> {
        int balls = bowlerBalls.get(bowler);
        int overs = balls / 6;
        float economy = ((float) runs / (float) overs);

        bowlersEconomy.put(bowler, economy);
    });

    List<Map.Entry<String, Float>> sortEconomyList = new ArrayList<>(bowlersEconomy.entrySet());
    sortEconomyList.sort(Map.Entry.comparingByValue());
    Map<String, Float> sortedBowlersEconomy = new LinkedHashMap<>();

    for (Map.Entry<String, Float> bowlerEconomy : sortEconomyList) {
        sortedBowlersEconomy.put(bowlerEconomy.getKey(), bowlerEconomy.getValue());
    }

    System.out.println("Top economical bowlers of 2015 : ");
    for(Map.Entry<String,Float> bowlerEconomy: sortedBowlersEconomy.entrySet()){
        if(lowestEconomy == Float.MAX_VALUE || lowestEconomy == bowlerEconomy.getValue()) {
            System.out.println("Bowler: " + bowlerEconomy.getKey() + ", Economy: " + bowlerEconomy.getValue());

            lowestEconomy = bowlerEconomy.getValue();
        }
        else{
            break;
        }
    }

}

}
