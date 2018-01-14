
package lottoTools.helpers;

import java.util.ArrayList;
import java.util.Collections;

import lottoTools.Player;
public final class Generic 
{
	private Generic(){}
	public static final class IListTools{
		//Interfaces
		@FunctionalInterface
		private static interface IListItemGetValue{
		   abstract int get(ArrayList<?> _list, int i);
		}	  
		public static final IListItemGetValue IListGetDefaultInt =(ArrayList<?> _list, int i) ->(int)_list.get(i);
		public static final IListItemGetValue IListGetPlayerTotal =(ArrayList<?> _list, int i) ->((Player)_list.get(i)).getTotal();	
		public static final IListItemGetValue IListTest =(ArrayList<?> _list, int i) ->((int[])_list.get(i))[1];	
	}
	
	
	/**
	 * Ordinal: returns the String ordinal of a number
	 * @param i: The number to be converted
	 * @return
	 */
	public static String Ordinal(int i) {
	    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + sufixes[i % 10];
	    }
	}
	/**
	 * SortList: sorts list with the bubble sort algorithm
	 * @param _list
	 * @param ascending
	 * @param getItemValue
	 */
		public static void SortList(ArrayList<?> _list, boolean ascending, IListTools.IListItemGetValue getItemValue) {
			//sort with the bubble sort algorithm
			
			int n=_list.size();
			for(int i=0; i<n-1; i++)
			{
				for(int j=0; j<n-i-1;j++)
				{
					int _this =getItemValue.get(_list, j);
					int _next =getItemValue.get(_list, j+1);
					boolean swapCondition = (ascending) ? (_this>_next) : (_this<_next);
					if(swapCondition)
					{
						Collections.swap(_list, j, j+1);
					}
				
				}
				
			}
		}
		
		public static void SortList(ArrayList<?> _list, boolean ascending) {
			SortList(_list, ascending, IListTools.IListTest);
		}
		
		
}
