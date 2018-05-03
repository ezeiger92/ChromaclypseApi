package com.chromaclypse.api.placeholder;

public abstract class Fragment {
	public final Object execute(Context context) {
		try {
			return onExecute(context);
		}
		catch(ClassCastException e) {
			// Either an incorrect context was given or something in onExecute(Context) went wrong
			
			String[] parts = e.getMessage().split(" ");
			try {
				Class<?> requested = Class.forName(parts[1]);
				Class<?> actual = Class.forName(parts[6]);
				
				if(!Context.class.isAssignableFrom(requested)
						|| !Context.class.isAssignableFrom(actual)) {
					// Probably tried to cross-cast <? extends Context>
				}
				else {
					// Maybe GenericContext<T> to GenericContext<S>, or any other type failure
				}
			}
			catch(Exception e2) {
				// Somehow the classes that were used in calling this function now don't exist.
				// If this *ever* happens, HOW AND WHY
				
				// Print the original stack trace, this one just says "I failed to load a class"
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	protected abstract Object onExecute(Context context);
}
