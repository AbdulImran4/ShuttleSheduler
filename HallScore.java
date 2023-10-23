public class HallScore implements Comparable<HallScore> {
    public String hallName;
    public int score;

    public HallScore(String hallName, int score) {
        this.hallName = hallName;
        this.score = score;
    }

    @Override
    public int compareTo(HallScore other) {
        return Integer.compare(other.score, this.score); // descending order of score
    }
}
