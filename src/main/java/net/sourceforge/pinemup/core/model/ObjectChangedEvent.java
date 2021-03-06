package net.sourceforge.pinemup.core.model;

import java.util.EventObject;

public abstract class ObjectChangedEvent<T> extends EventObject {
   private static final long serialVersionUID = -1662988083960754627L;

   protected ObjectChangedEvent(T source) {
      super(source);
   }

   @Override
   @SuppressWarnings("unchecked")
   public T getSource() {
      return (T)source;
   }
}
