package com.nbfc.validator;

import java.util.Arrays;

public class TestImpl implements TestIfc {
	   
	 public static void main(String args[])
	{
	    int arr1[]={-1,1,3,5,7,9};
	    int arr2[]={-2,2,3,4,5,6};
	    int arrResult[]=new int[arr1.length+arr2.length];
	    TestImpl test1= new TestImpl();
	    arrResult=test1.mergeArrays(arr1,arr2);
	    for(int i=0;i<arrResult.length;i++)
	    {
	    	System.out.print(arrResult[i]);
	    }
	   
	}
	public int[] mergeArrays(int[]arr1,int[]arr2){
	   int  arr3[]=new int[(arr1.length+arr2.length)-1];
	       int pointer=1;
	       for(int i=0;i<arr1.length;i++){
	          arr3[i]=arr1[i];
	         pointer=i;
	       }
	     
	       for(int i=0;i<arr2.length; i++){
	           arr3[pointer]=arr2[i];
	           pointer++;
	           }
	       
	       Arrays.sort(arr3);
//	        for(int i=0; i<arr3.length; i++){
//	            for(int j=1;j<arr3.length-i; j++)
//	            {
//	                if(arr3[j-1]>arr3[j]){
//	                    int temp=arr3[j-1];
//	                    arr3[j-1]=arr3[j];
//	                    arr3[j]=temp;
//	                }
//	            }
//	        }
	        return arr3;
	}
	}
