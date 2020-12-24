/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.amazons.players;

import edu.upc.epsevg.prop.amazons.CellType;
import edu.upc.epsevg.prop.amazons.GameStatus;
import edu.upc.epsevg.prop.amazons.IAuto;
import edu.upc.epsevg.prop.amazons.IPlayer;
import edu.upc.epsevg.prop.amazons.Move;
import edu.upc.epsevg.prop.amazons.SearchType;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Fatima i Aleena
 */
public class MinMaxPlayer implements IPlayer, IAuto{

    String name;
    private GameStatus gs;
    
    public MinMaxPlayer(String name) {
        this.name = name;
    }
    
     @Override
    public void timeout() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getName() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "MinMax(" + name + ")";
    }
    
    @Override
    public Move move(GameStatus gs) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        CellType color = gs.getCurrentPlayer();
        
        int num = gs.getNumberOfAmazonsForEachColor();
        double alfa = Integer.MIN_VALUE;
        Point millorMoviment = new Point();
        Point millorMovimentx = new Point();
        Point Origen = new Point();
        int profunditat = 3;
     
        for(int i = 0; i< num; i++)
        {
           Origen = gs.getAmazon(color, i);
           ArrayList<Point> listamoviments = gs.getAmazonMoves(Origen , false);
           for(int j = 0; j< listamoviments.size(); j++)
           {
                Point Destino=listamoviments.get(j);
                ArrayList<Point> listabuida = CaselasBuides(gs);
                GameStatus posax = new GameStatus(gs);
                for(int x = 0; x< listabuida.size(); x++)
                {
                    posax.moveAmazon(Origen, Destino);
                    listabuida.remove(Destino);
                    listabuida.add(Origen);
                    posax.placeArrow(listabuida.get(x));
                    listabuida.remove(x);
                    double valor = minValor(posax, profunditat, alfa, Integer.MAX_VALUE);
                    if(alfa< valor)
                    {
                        millorMoviment= new Point (listamoviments.get(j));
                        millorMovimentx= new Point(listabuida.get(x));
                        alfa = valor;
                    }
               }//getNumerOfNodesExplored, getMaxDepthReached, getSearchType
           }
        } 
        return new Move(Origen, millorMoviment, millorMovimentx, 0, 0 ,SearchType.MINIMAX );
    }
    
    public ArrayList<Point> CaselasBuides(GameStatus successor)
    {
        ArrayList<Point> llista = new ArrayList<>();
        
        for(int i=0; i<successor.getSize(); i++)
        {
            for(int j=0; j<successor.getSize(); j++)
            {
                if(successor.getPos(i, j) == CellType.EMPTY)
                {
                    llista.add(new Point(i, j));
                }
            }
        }
        return llista;
    }

    /**
      Funció minimitzadora
     * @param node Node de l'estat o node dins del estat actual del joc
     * @param color Color de la peça que minimitzara
     * @param profunditat Profunditat de l'arbre que genera minimax
     * @return valor de la funció minimitazadora
     */
    
    public double minValor(GameStatus node, int profunditat, double alfa, double beta) {
   
        int num = node.getNumberOfAmazonsForEachColor();
        CellType color = node.getCurrentPlayer();
        Point Origen = new Point();
        if(profunditat == 0 || node.isGameOver()) return getHeuristica(node, color);
        for (int i = 0; i < num; ++i)
        {
                Origen = node.getAmazon(color, i);
                ArrayList<Point> listamoviments = node.getAmazonMoves(Origen , false);
                for(int j = 0; j< listamoviments.size(); j++)
                {
                    Point Destino=listamoviments.get(j);
                    ArrayList<Point> listabuida = CaselasBuides(node);
                    GameStatus posax = new GameStatus(node);
                    for(int x = 0; x< listabuida.size(); x++)
                    {
                        posax.moveAmazon(Origen, Destino);
                        listabuida.remove(Destino);
                        listabuida.add(Origen);
                        posax.placeArrow(listabuida.get(x));
                        listabuida.remove(x);
                        double valor = maxValor(posax, profunditat-1, alfa, beta);
                        beta = Math.min(valor, beta);
                        if(beta <= alfa) return alfa;
                    }
                }
            }
        
        return beta;
    }
    
    /**
    Funció maximitzadora
     * @param node Node de l'estat o node dins del estat actual del joc
     * @param color Color de la peça que maximitzará
     * @param profunditat Profunditat de l'arbre que genera minimax
     * @return valor de la funció maximitzadora
     */
    public double maxValor(GameStatus node, int profunditat, double alfa, double beta) {
      
        int num = node.getNumberOfAmazonsForEachColor();
        CellType color = node.getCurrentPlayer();
        Point Origen = new Point();
        if(profunditat == 0 || node.isGameOver() )  return getHeuristica(node, color);
            for (int i = 0; i < num; ++i)
            {
               Origen = node.getAmazon(color, i);
               ArrayList<Point> listamoviments = node.getAmazonMoves(Origen , false);
               for(int j = 0; j< listamoviments.size(); j++)
               {
                    Point Destino=listamoviments.get(j);
                    ArrayList<Point> listabuida = CaselasBuides(node);
                    GameStatus posa = new GameStatus(node);
                    for(int x = 0; x< listabuida.size(); x++)
                    {
                        posa.moveAmazon(Origen, Destino);
                        listabuida.remove(Destino);
                        listabuida.add(Origen);
                        posa.placeArrow(listabuida.get(x));
                        listabuida.remove(x);
                        double valor =  minValor(posa, profunditat-1, alfa, beta);
                        alfa = Math.max(valor, alfa);
                        if(beta <= alfa) return beta;
                    }
                }
               
            }    
        return alfa;
    }

    public int getHeuristica(GameStatus node, CellType color)
    {
       int n = node.getNumberOfAmazonsForEachColor();
       int valor = 0;
       
       ArrayList<Point> moveposible = new ArrayList<>();
       ArrayList<Point> moveposiblerebal = new ArrayList<>();
       for(int i= 0; i < n; ++i)
       {
            moveposible.add(node.getAmazon(color, i));
            moveposiblerebal.add(node.getAmazon(color.opposite(node.getCurrentPlayer()), i));
           
       }
       for(int i= 0; i < moveposible.size(); ++i)
       {
           valor += node.getAmazonMoves(moveposible.get(i), false).size();
           valor -= node.getAmazonMoves(moveposiblerebal.get(i), false).size();
       }
        return valor;
    }
}

    