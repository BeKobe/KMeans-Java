package cn.edu.uestc.algorithm.kmeans.mykmeans;

/** Created by BurNI on 2016-04-28.*/
public class TestMain {
    public static void main(String[] args)
    {
        KMeans kmeans = new KMeans("test3.txt", 2);
        kmeans.kMeansClustering();
        System.out.println(kmeans.getCents());
        kmeans.calcSSE();
        System.out.println("SSE: " + kmeans.getSSE());
        System.out.println("Total SSE: " + kmeans.getTotalSSE());
    }
}
