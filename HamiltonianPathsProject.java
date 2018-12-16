/*
 * GNU GPLv3
 */
package graphsproject;

import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * PAPADOPOULOS THEODOROS 2785
 * ARISTOTLE UNIVERSITY OF THESSALONIKI
 * DEPARTMENT OF INFORMATICS
 * 6TH SEMESTER
 * GRAPH THEORY AND ALGORITHMS 2018
 * @author PAPADOPOULOS THEODOROS 2785
 */
public class HamiltonianPathsProject {

    /**
     * @param args the command line arguments
     * argument should be a file name .txt format
     * 
     */

    private static String A_FILE=null;    
    private static String[][] graph;
    private static String[][] m_table;
    private static String[][] m1_table;
    private static String[][] matrix_hadamard;    
    
    /**
     * Main function, initilize the parametres and call suitable functions
     * @param args arguments from command line,should enter a file name .txt type which contains
     * an adjacency matrix of a graph
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        //if there are not command line arguments use a default example file
        if(args.length<=0){
            //System.out.printf("Using the default example graph...");
            System.out.println("Give the filename which contains an adjacency matrix: (.txt format) \n"
                    + " or press '0' to use a default example.");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if(!answer.equals("0")){
                A_FILE=answer;
            }else{
                A_FILE="ex1.txt";
            }
        }else{
            A_FILE=args[0];
        }
        //Generating adjatency matrix
        graph = create_graph(A_FILE); // file_manager(args[0]);
        //System.out.println();
        //System.out.println("--------- Adjacency matrix------");
        //arrayPrinter(graph);
        //System.out.println();
        
        //Generating m1 matrix and show
        m1_table = create_M1_table(graph);
        //System.out.println();
        //System.out.println("------------- M1 matrix ------");
        //arrayPrinter(m1_table);
        //System.out.println();
        
        //Generating M table and show
        m_table = create_M_table(m1_table);
        //System.out.println();
        //System.out.println("------------- M matrix ------");
        //arrayPrinter(m_table);
        //System.out.println();

        //Hadamard Calls
        matrix_hadamard = hadamard(m_table,m1_table);
        //matrix_hadamard = hadamard(m_table,matrix_hadamard);
        //matrix_hadamard = hadamard(m_table,matrix_hadamard);
        
        //System.out.println();
        //System.out.println("------ HADAMARD-----");
        //arrayPrinter(matrix_hadamard);
        //System.out.println();
        
        //Multiply for number of edges
        for(int i=0;i<graph.length-3;i++){
            matrix_hadamard=hadamard(m_table,matrix_hadamard); // <-- check for proper tables!
        }
        //arrayPrinter(matrix_hadamard);
        showResults(matrix_hadamard);
        
        
        
    }  
    /**
     * Function that reads the adjacency matrix from file
     * @param file File name in string type
     * @return A 2-D arrays which contains the graph adjacency matrix
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static String[][] create_graph(String file) throws FileNotFoundException, IOException{
        
        //Reading array from file
        String file_name = file;
        Scanner scanner = new Scanner(new BufferedReader (new FileReader(file_name)));
        //int graph[][];
        Path path = Paths.get(file_name);
        int lines = (int) Files.lines(path).count() ;
        //System.out.println("!DEBUGGING!Number of rows and columns:" +lines);
        String[][] graph_temp = new String[lines][lines];
        for(int i=0;i<lines;i++){
            for(int j=0;j<lines;j++){
                int value = scanner.nextInt();
                graph_temp[i][j]= Integer.toString(value);
            }    
        }
        
        //System.out.println("!DEBUGGING! length:"+graph.length);
        //System.out.println("------------- Graph Matrix ------");
        //arrayPrinter(graph_temp);
        //System.out.println();
        return graph_temp;
        
         
    }
    /**
     * Implements a 2-D Array Printing
     * @param array A 2-D array for printing
     */
    public static void arrayPrinter(String[][] array){
        
        int limit = array.length;
        for(int i=0;i<limit;i++){
            System.out.println();
            for (int j=0;j<limit;j++){
                System.out.printf("%s   ",array[i][j]);
            }
        }
        System.out.println();
        
    }
    
    /**
     * Creates the M1 table of a graph
     * @param array
     * @return the M1 table
     */
    public static String[][] create_M1_table(String[][] array){
        int n = array.length;
        String matrix[][] = new String[n][n];
        String i_temp=null;
        String j_temp=null;
        
        for(int i=0;i<n;i++){ // rows
            for(int j=0;j<n;j++){ //columns
                if(array[i][j].equals("1")){
                    i_temp = Integer.toString(i+1);
                    j_temp = Integer.toString(j+1);
                    //System.out.println(i_temp);
                    matrix[i][j]= i_temp + j_temp;
                }else{
                    matrix[i][j]="0";
                }
            }
        }              
        return matrix;
        
    } 
    
    /**
     * Creates the M matrix 
     * @param array a 2-D array of M1 type 
    */
    public static String[][] create_M_table(String[][] array){
        
        // Because there is a problem with call-by-reference and call-by-value
        int n = array.length;
        String[][] tempo = new String[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                tempo[i][j]=array[i][j];
            }
        }
        //Seperate the last digit
        for(int i=0;i<n;i++){ //for each row
            for(int j=0;j<n;j++){ //for each column
                if(!tempo[i][j].equals("0")){
                    String stt=tempo[i][j];
                    char[] charArray = stt.toCharArray();
                    tempo[i][j]= String.valueOf(charArray[1]);
                }
            }
            
        }
        return tempo;
    }
    
    
    /**
     * Implements the Hadamart multiplication of an m and m1 array
     * @param m_array The M array 
     * @param mj_array The Mj array
     * @return the result of hadamard multiplication
    */
    public static String[][] hadamard(String[][] m_array , String[][] mj_array){
    
        String path1,path2;
         
        //Initilization of result matrix with # character
        String[][] matrix= new String[m_array.length][mj_array[0].length];
        for (int i=0;i<matrix.length;i++){
            for (int j = 0; j<matrix.length; j++) {
                matrix[i][j] = "#";
            }
        }
        String[][] temp_matrix = new String[matrix.length][matrix.length];
        
        //String temp=null;
        for(int i=0;i<m_array.length;i++){
            for(int j=0;j<mj_array[0].length;j++){
                for(int k=0;k<m_array[0].length;k++){
                    temp_matrix[i][j]=matrix[i][k];
                    if(!m_array[k][j].equals("0") && !mj_array[i][k].equals("0") && !mj_array[i][k].contains(m_array[k][j])){
                        //Keep a copy of previous cell-value
                        
                        matrix[i][j] = mj_array[i][k]+m_array[k][j];
                        
                        if(matrix[i][j].contains("|")){
                        String[] parts = matrix[i][j].split("\\|");
                        path1=parts[0];
                        path2=parts[1];
                        path1+= m_array[i][k];
                        path2+= m_array[i][k];
                        matrix[i][j]=path1+" | " + path2;  
                        }
                        
                        if(matrix[i][j].equals(temp_matrix[i][j])){
                            matrix[i][j]=matrix[i][j]+" | "+temp_matrix[i][j]+m_array[k][j];
                        }
                        
                        
                        
                        
                    }
                    
                    
                

                }
                if(matrix[i][j].equals("#")){
                    matrix[i][j]="0";
                }
            }
        }
        return matrix;
    } 
    
    /**
     * Shows the Hamiltonian circles or paths with proper messages
     * @param matrix a 2-d array 
     */
    public static void showResults(String[][] matrix){
        int n = matrix.length;
        int numberOfpaths =0;
        System.out.println();
        System.out.println("RESULTS ( sequence of edges) ");
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(!matrix[i][j].equals("0")){
                    numberOfpaths++;
                    if(checkCircle(matrix[i][j])){
                        System.out.println("Hamiltonian Circle: "+ matrix[i][j]);
                    }else{
                        System.out.println("Hamiltonian Path: " + matrix[i][j]);
                    }
                }
            }
        } 
        //For debugging use
        //System.out.println("Total number of result paths or circles: "+numberOfpaths);
    }
    
    
    /**
     * Function that checks if there is a circle in a hamiltonian path
     * @param path A string as a path of edges
     * @return true if the path is circle ,else false
     */
    public static boolean checkCircle(String path){
        
        boolean circle =false; //the returning result
        String[] path_array=new String[path.length()]; // array of character for each edge
        char[] charArray = path.toCharArray();
        for(int i=0;i<path.length();i++){
            path_array[i]=String.valueOf(charArray[i]);
        }
        String v0=path_array[0];
        String v1=path_array[path_array.length-1];
        //Converting edges to integers in order to use in adjacency matrix
        int k1 = Integer.parseInt(v0)-1; //value -1 because the edge '2' is represented by '1' index
        int k2 = Integer.parseInt(v1)-1;
        
        //Check if there is an '1' in the adjacency matrix on proper cells
        if(graph[k1][k2].equals("1") || graph[k2][k1].equals("1")){
            circle=true;
        }else{
          circle=false;
        }
        return circle;
        
    }

}

