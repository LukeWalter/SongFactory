package songfactory;

public class Pair<A, B> {

    public A first;
    public B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;

    } // Pair

    @Override
    public String toString() {
        return "[" + first + ", " + second + "]";

    } // toString

    @Override
    public boolean equals(Object o) {

        if (!this.getClass().equals(o.getClass())) {
            return false;

        } // if

        Pair<Object, Object> other = (Pair<Object, Object>) o;

        if (this.first.equals(other.first) && this.second.equals(other.second)) {
            return true;

        } // if

        return false;

    } // compareTo

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode();

    } // hashcode

} // Pair
