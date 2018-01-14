package lottoTools.helpers;

import java.util.ArrayList;

import lottoTools.helpers.ISubject;
public interface IObserver 
{
	default void addSubject(ISubject _subject) {
	  _subject.observers.add((Object)this);

	}
	void onNotify(Object obj, String args);
	default void onNotify(Object obj, String args, Object param) {
		
	}
}
