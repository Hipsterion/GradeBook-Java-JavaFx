package main.java.utils.guiUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HideableItem<T>
{
    public final ObjectProperty<T> object = new SimpleObjectProperty<>();
    public final BooleanProperty hidden = new SimpleBooleanProperty();

    public HideableItem(T object)
    {
        setObject(object);
    }

    public ObjectProperty<T> objectProperty(){return this.object;}
    public T getObject(){return this.objectProperty().get();}
    public void setObject(T object){this.objectProperty().set(object);}

    public BooleanProperty hiddenProperty(){return this.hidden;}
    public boolean isHidden(){return this.hiddenProperty().get();}
    public void setHidden(boolean hidden){this.hiddenProperty().set(hidden);}

    @Override
    public String toString()
    {
        return getObject() == null ? null : getObject().toString();
    }
}