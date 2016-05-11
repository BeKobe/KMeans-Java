package cn.edu.uestc.algorithm.kmeans.mykmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/** Created by BurNI on 2016-04-28.*/
@SuppressWarnings({"WeakerAccess", "unused"})
public class KMeans {
    // 数据集
    private List<ArrayList<Double>> points = new ArrayList<>();
    // 质心集
    private List<ArrayList<Double>> cents;
    // 聚类结果, 每个数据集中的元素向量与其质心(簇)的对应关系
    private Map<ArrayList<Double>, ArrayList<Double>> clusterResult = new HashMap<>();
    // 误差, 即每个数据集中的元素向量与其和簇心(质心)的距离的对应关系
    private Map<ArrayList<Double>, Double> error = new HashMap<>();
    // 每个簇(质心)的误差平方和
    private Map<ArrayList<Double>, Double> SSE = new HashMap<>();
    // 总误差平方和
    private double totalSSE = 0.0;

    /**
     * 构造函数, 从文本文件中读取数据集并产生k个随机的初始质心
     * */
    KMeans(String filename, int k)
    {
        File file = new File(filename);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String str;
            while ((str = reader.readLine()) != null)
            {
                if (Objects.equals(str, ""))
                {
                    continue;
                }
                List<String> tmp = Arrays.asList(str.split("\\t| "));
                ArrayList<Double> p =
                        tmp.stream().map(Double::valueOf).collect(Collectors.toCollection(ArrayList::new));
                points.add(p);
            }
            reader.close();
            this.cents = Utils.getRandCents(points, k);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * K-Means算法流程
     * 对于每个数据集中的向量(这里抽象成点point), 计算其与所有质心的距离,
     * 然后将其分配给最近的质心, 完成分配后用新簇中元素向量的均值更新质心集,
     * 进行如此的反复迭代, 直到分配结果不再变化为止
     * */
    public void kMeansClustering()
    {
        for (ArrayList<Double> point : points)
        {
            Map<ArrayList<Double>, Double> distances = new HashMap<>();
            for (ArrayList<Double> cent : cents)
            {
                double distance = Utils.calcEuclideanDistances(cent, point);
                distances.put(cent, distance);
            }
            List<Map.Entry<ArrayList<Double>, Double>> sorted = new ArrayList<>(distances.entrySet());
            Collections.sort(sorted, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
            clusterResult.put(point, sorted.get(0).getKey());
            error.put(point, sorted.get(0).getValue());
        }
        Map<ArrayList<Double>, List<ArrayList<Double>>> clusteredPoints =
                Utils.getClusters(clusterResult);
        List<ArrayList<Double>> newCents = new ArrayList<>();
        newCents.addAll(cents);
        for (Map.Entry<ArrayList<Double>, List<ArrayList<Double>>> entry : clusteredPoints.entrySet())
        {
            newCents.set(cents.indexOf(entry.getKey()), Utils.calcNewCent(entry.getValue()));
        }
        if (!(Arrays.equals(newCents.toArray(), cents.toArray())))
        {
            cents = newCents;
            kMeansClustering();
        }
    }

    /**
     * 计算误差平方和SSE
     * */
    public void calcSSE()
    {
        Map<ArrayList<Double>, List<ArrayList<Double>>> clusteredPoints = Utils.getClusters(clusterResult);
        for (Map.Entry<ArrayList<Double>, List<ArrayList<Double>>> entry : clusteredPoints.entrySet())
        {
            double sse = 0.0;
            for (ArrayList<Double> each : entry.getValue())
            {
                double err = error.get(each);
                sse += Math.pow(err, 2);
            }
            SSE.put(entry.getKey(), sse);
            totalSSE += sse;
        }
    }

    public List<ArrayList<Double>> getPoints () {
        return points;
    }

    public void setPoints(List<ArrayList<Double>> points) {
        this.points = points;
    }

    public List<ArrayList<Double>> getCents() {
        return cents;
    }

    public void setCents(List<ArrayList<Double>> cents) {
        this.cents = cents;
    }

    public Map<ArrayList<Double>, Double> getError() {
        return error;
    }

    public void setError(Map<ArrayList<Double>, Double> error) {
        this.error = error;
    }

    public Map<ArrayList<Double>, ArrayList<Double>> getClusterResult() {
        return clusterResult;
    }

    public void setClusterResult(Map<ArrayList<Double>, ArrayList<Double>> clusterResult) {
        this.clusterResult = clusterResult;
    }

    public Map<ArrayList<Double>, Double> getSSE() {
        return SSE;
    }

    public void setSSE(Map<ArrayList<Double>, Double> SSE) {
        this.SSE = SSE;
    }

    public double getTotalSSE() {
        return totalSSE;
    }

    public void setTotalSSE(double totalSSE) {
        this.totalSSE = totalSSE;
    }
}
