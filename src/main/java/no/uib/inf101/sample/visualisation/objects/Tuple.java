package no.uib.inf101.sample.visualisation.objects;

public class Tuple<A, B> {
    private final A first;
    private final B second;

    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public A setFirst(A first) {
        return this.first;
    }

    public B setSecond(B second) {
        return this.second;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
