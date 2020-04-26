package com.example.a4322_term_project;

public class GameplayManager {

    private int numOfPlayers;
    private int index;
    private int[] playerIndex;

    private static GameplayManager instance = null;

    public GameplayManager(){
        instance = this;
    }

    public GameplayManager(int nplayers, int[] p, int i){
        numOfPlayers = nplayers;
        playerIndex = p;
        index = i;
        if(instance == null){
            instance = this;
        }
    }

    public void SetPlayerCount(int x){
        numOfPlayers = x;
    }

    public void SetIndex(int i){
        index = i;
    }

    public void SetPlayers(int[]p){
        playerIndex = p;
    }

    public int getIndex(){
        return index;
    }

    public int[] getPlayerIndex(){
        return playerIndex;
    }

    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    public int getCurrentPlayerIndex(){
        return playerIndex[index];
    }

    public void SetupGame(){
        if(numOfPlayers <= 0 ){
            numOfPlayers = 1;
        }
        if(playerIndex == null){
            playerIndex = new int[numOfPlayers];
        }
        for(int i = 0; i < playerIndex.length;i++){
            playerIndex[i] = i+1;
        }
        index = 0;
    }
}
