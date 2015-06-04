package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class NashBalance {

	private ArrayList<double[][]> tables = new ArrayList<double[][]>();
	private double valueGiven;
	private double realValue;
	
	//valueGiven,initial value, number of bidders
	public NashBalance(double valueGiven, double initialValue, int numBider){
		this.valueGiven=valueGiven;
		this.realValue=initialValue/2;
		
		populate(numBider);
	}

	private void populate(int numBider) {
		for(int i=0;i<numBider;i++){
			Random r = new Random();
			double[][] temp = new double[11][22];
			
			double bidderNValue=r.nextGaussian()*(realValue*0.6)+realValue;
			double hedgeOther=0,chanceToWinOther=0.88;

			for(int j=0;j<11;j++){
				double hedgeMine=0,chanceToWinMine=0.8;
				double[] line = new double[22];
				for(int k=0;k<22;k+=2){
					
					temp[j][k] = (realValue-(bidderNValue-(bidderNValue*hedgeOther)))*chanceToWinOther;
					temp[j][k+1] = (realValue-(valueGiven-(valueGiven*hedgeMine)))*chanceToWinMine;
					hedgeMine+=0.05;
					chanceToWinMine-=0.07;
				}
				System.out.println(Arrays.toString(temp[j]));
				hedgeOther+=0.05;
				chanceToWinOther-=0.07;
			}
			tables.add(temp);
			
		}
	}
	
	public double solve(){
		int bestHedgeMine=0,bestHedgeOther=0;
		int bestHedgeMineOld=0,bestHedgeOtherOld=0;
		int bestHedgeMineFinal=Integer.MAX_VALUE;
		for(int i=0;i<tables.size();i++){
			do{
				bestHedgeMineOld=bestHedgeMine;
				bestHedgeOtherOld=bestHedgeOther;
				bestHedgeMine=bestCol(tables.get(i),bestHedgeOtherOld);
				bestHedgeOther=bestRow(tables.get(i)[bestHedgeMine]);
			}while(bestHedgeMine!=bestHedgeMineOld&&bestHedgeOther!=bestHedgeOtherOld);
			if(bestHedgeMine<bestHedgeMineFinal)
				bestHedgeMineFinal=bestHedgeMine;
		}
		return bestHedgeMine*0.05;
	}
	
	private int bestRow(double[] ds) {
		double max=Integer.MIN_VALUE;
		int	row=0;
		for(int i=0;i<ds.length;i++){
			if(ds[i]>max){
				max=ds[i];
				row=i;
			}
		}
		return row;
	}

	private int bestCol(double[][] ds, int bestHedgeOtherOld) {
		double max=Integer.MIN_VALUE;
		int	col=0;
		for(int i=0;i<11;i++){
			if(ds[i][bestHedgeOtherOld]>max){
				max=ds[i][bestHedgeOtherOld];
				col=i;
			}
		}
		return col;
	}

	public static void main(String args[]){
		NashBalance t = new NashBalance(8,10,9);
		System.out.println((double)t.solve()*0.05);
	}
}
