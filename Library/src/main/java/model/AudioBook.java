package model;

public class AudioBook extends Book{
    private int runTime;

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return String.format("AudioBook author: %s | title: %s | published date: %s | run time: %s.", author, title, publishedDate, runTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AudioBook audioBook = (AudioBook) o;
        return runTime == audioBook.runTime;
    }
}
