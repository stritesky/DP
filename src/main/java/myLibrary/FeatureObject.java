package myLibrary;

/**
 * Created by radek on 5.4.17.
 */
public class FeatureObject {

    private int count = 1;
    private String name;

    FeatureObject(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof FeatureObject)
        {
            FeatureObject c = (FeatureObject) o;
            if ( this.name.equals(c.getName()) ) //whatever here
                return true;
        }
        return false;
    }
}
