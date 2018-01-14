package lottoTools.helpers;

import java.util.ArrayList;

public interface ISubject 
{
	 ArrayList<Object> observers = new ArrayList<Object>();
	 
	 default void Notify(Object obj, String args) 
	 {
		 for(Object obs : observers) {
			 if(obs.equals(obj)) {
				 IObserver nObs = (IObserver) obs;
				 nObs.onNotify(this, args);
			 }
		 }
	 }
	 default void Notify(Object obj, String args, Object param) 
	 {
		 for(Object obs : observers) {
			 if(obs.equals(obj)) {
				 IObserver nObs = (IObserver) obs;
				 nObs.onNotify(this, args, param);
			 }
		 }
	 }
}
