import java.util.Scanner;

public class Run
{
	static Scanner in = new Scanner(System.in);
	static int Size=1000;

	public static void main(String[] args)
	{
		Run solver = new Run();
		 
		int tests = in.nextInt();
		
		for(int i=1 ; i<=tests ; i++)
			solver.solve(i);	
	}
	
	void solve(int casenum)
	{
		int n, S, w[], b[], chroms[][], mx=0, mxChrom[];
		double fitratio[];
		
		n = in.nextInt();
		S = in.nextInt();
		
		w = new int[n];
		b = new int[n];

		for(int i=0 ; i<n ; i++)
		{
			w[i] = in.nextInt();
			b[i] = in.nextInt();
		}

		chroms = GenInitPopulation(n, w, S);
		mxChrom = new int[n];
		
		for(int i=0 ; i<Size; i++)
		{
			mx = getMaxChrom(n,chroms,mxChrom, b, mx);
			fitratio = CalacFitRatio(n, chroms, b);
			chroms = GenNewPopulation(chroms, fitratio, n, w, S);
		}
		
		System.out.println("Case " + casenum + ": " + getMaxChrom(n,chroms,mxChrom, b, mx));
		Print(mxChrom, n, w, b);
		
	}
	
	int getMaxChrom(int n, int chroms[][], int mxChrom[], int b[], int mx)
	{
		int fit[] = CalFit(n, chroms, b);
		
		for(int i=0 ; i<Size ; i++)
			if(fit[i]>mx)
			{
				mx = fit[i];
				System.arraycopy(chroms[i], 0, mxChrom, 0, n);
			}
		
		return mx;
	}
	
	void Print(int chrom[], int n, int w[], int b[])
	{		
		int cnt=0;
		
		for(int i=0 ; i<n ; i++)
			if(chrom[i]==1)
				cnt++;
		
		System.out.println(cnt);
		
		for(int i=0 ; i<n ; i++)
			if(chrom[i]==1)
				System.out.println(w[i] + " " + b[i]);
	}
	
	int [][] GenNewPopulation(int chroms[][], double fitratio[], int n, int w[], int S)
	{
		int NewChroms[][] = new int [Size][n];
		int chrom1[] = new int[n], chrom2[] = new int [n];
		
		for(int i=0 ; i<Size ; )
		{
			int ind1=select(fitratio), ind2=select(fitratio);
			
			System.arraycopy(chroms[ind1], 0, chrom1, 0, n);
			System.arraycopy(chroms[ind2], 0, chrom2, 0, n);

			crossover(n, chrom1, chrom2);
			mutation(n,chrom1);
			mutation(n,chrom2);
			
			if(Check(chrom1, w, S))
			{
				System.arraycopy(chrom1, 0, NewChroms[i], 0, n);
				i++;
			}
			
			if(i<Size && Check(chrom2, w, S))
			{
				System.arraycopy(chrom2, 0, NewChroms[i], 0, n);
				i++;
			}
		}
		
		return NewChroms;
	}
	
	void crossover(int n, int chrom1[], int chrom2[])
	{
		double r = Math.random();

		if(r<0.7)
		{
			int ind = (int) (1+Math.floor(Math.random()*(Size-1)));
			
			for(int i=ind ; i<n ; i++)
			{
				chrom1[i] ^= chrom2[i];
				chrom2[i] ^= chrom1[i];
				chrom1[i] ^= chrom2[i];
			}			
		}
	}
	
	void mutation(int n, int chrom[])
	{
		double r = Math.random();
		
		if(r<0.01)
		{
			int ind = (int) (Math.floor(Math.random()*n));
			chrom[ind] = chrom[ind]==1 ? 0 : 1;
		}
	}
	
	int select(double fitratio[])
	{
		double r = Math.random();
		int ind=99;
		
		for(int i=0 ; i<Size ; i++)
			if(r<fitratio[i])
			{
				ind = i;
				break;
			}
		
		return ind;
	}
	
	int [][] GenInitPopulation(int n, int w[], int S)
	{
		int chroms[][] = new int[Size][n];
		
		for(int i=0 ; i<Size ; i++)
		{
			for(int j=0 ; j<n ; j++)
			{
				double r = Math.random();
				chroms[i][j] = (r<0.5 ? 0 : 1);
			}

			if(Check(chroms[i],w,S)==false)
				i--;
		}
		
		return chroms;
	}
	
	int [] CalFit(int n, int chroms[][], int b[])
	{
		int fit[] = new int[Size];
		
		for(int i=0 ; i<Size ; i++)
		{
			fit[i]=0;
			
			for(int j=0 ; j<n ; j++)
				fit[i]+=chroms[i][j]*b[j];			
		}
		
		return fit;
	}
	
	double [] CalacFitRatio (int n, int chroms[][], int b[])
	{
		int fit[] = CalFit(n,chroms,b), total=0;
		double fitratio[] = new double[Size];
		
		for(int i=0 ; i<Size ; i++)
			total += fit[i];
		
		for(int i=0 ; i<Size ; i++)
			fitratio[i] = fit[i]*1.0/total;
		
		for(int i=1 ; i<Size ; i++)
			fitratio[i] += fitratio[i-1];
		
		return fitratio;
	}
	
	boolean Check(int chrom[], int w[], int S)
	{
		int sum=0;
		
		for(int i=0 ; i<chrom.length ; i++)
			sum += chrom[i]*w[i];
		
		return (sum<=S);
	}

}
