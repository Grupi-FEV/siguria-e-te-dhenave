public class Pair<K,V> {
    public K first;
    public V second;

    public Pair(K first, V second){
        this.first = first;
        this.second = second;
    }

    public String toString(){
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}