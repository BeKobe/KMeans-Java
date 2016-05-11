package cn.edu.uestc.algorithm.kmeans.mykmeans;

import java.util.*;

/** Created by BurNI on 2016-04-28.*/
@SuppressWarnings("WeakerAccess")
public class Utils {

    /**
     * 辅助函数, 多维数据获得每一维的数据, 即矩阵每一列的元素放在一个列表中
     * */
    private static ArrayList<ArrayList<Double>> getEveryDimension(List<ArrayList<Double>> points)
    {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        int dimensionNum = points.get(0).size();
        for (int i=0; i<dimensionNum; i++)
        {
            result.add(new ArrayList<>());
        }
        for (ArrayList<Double> point : points)
        {
            for(double p : point)
            {
                result.get(point.indexOf(p)).add(p);
            }
        }
        return result;
    }

    /**
     * 从多维数据每一维度的最大最小值范围内(边界内)获取k个随机质心
     * */
    public static List<ArrayList<Double>> getRandCents(List<ArrayList<Double>> points,
                                                        int k)
    {
        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();
        ArrayList<ArrayList<Double>> temp = getEveryDimension(points);
        for (ArrayList<Double> tmp : temp)
        {
            Collections.sort(tmp, Double::compareTo);
        }
        for (ArrayList<Double> tmp : temp)
        {
            min.add(tmp.get(0));
            max.add(tmp.get(tmp.size()-1));
        }
        List<ArrayList<Double>> randCents = new ArrayList<>();
        int dimensionNum = points.get(0).size();
        for (int i = 0; i<k; i++) {
            ArrayList<Double> randCent = new ArrayList<>();
            for (int j = 0; j < dimensionNum; j++) {
                double rand = Math.random()*(max.get(j) - min.get(j)) + min.get(j);
                randCent.add(rand);
            }
            randCents.add(randCent);
        }
        return randCents;
    }

    /**
     * 计算两个向量的欧氏距离
     * */
    public static double calcEuclideanDistances(ArrayList<Double> cent, ArrayList<Double> point)
    {
        if (cent.size() != point.size())
        {
            throw new ArrayIndexOutOfBoundsException("长度不一致, 无法计算欧式距离!\n");
        }
        int size = cent.size();
        double distance = 0.0;
        for (int i=0; i<size; i++)
        {
            double temp = cent.get(i) - point.get(i);
            distance += Math.pow(temp, 2);
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    /**
     * 根据每一簇的质心得到这一簇的所有元素, 返回簇质心与其所有元素列表的对应关系
     * */
    public static
    Map<ArrayList<Double>, List<ArrayList<Double>>>
    getClusters(Map<ArrayList<Double>, ArrayList<Double>> data)
    {
        Map<ArrayList<Double>, List<ArrayList<Double>>> result = new HashMap<>();
        for (Map.Entry<ArrayList<Double>, ArrayList<Double>> entry : data.entrySet()) {
            result.put(entry.getValue(), result.getOrDefault(entry.getValue(), new ArrayList<>()));
            List<ArrayList<Double>> temp = result.get(entry.getValue());
            temp.add(entry.getKey());
            result.put(entry.getValue(), temp);
        }
        return result;
    }

    /**
     * 根据簇中所有元素的均值得到该簇的新质心
     * */
    public static ArrayList<Double> calcNewCent(List<ArrayList<Double>> clusteredPoints)
    {
        ArrayList<Double> newCent = new ArrayList<>();
        ArrayList<ArrayList<Double>> temp = getEveryDimension(clusteredPoints);
        for (ArrayList<Double> tmp : temp)
        {
            double sum = 0.0;
            for (double p : tmp)
            {
                sum += p;
            }
            newCent.add(sum / tmp.size());
        }
        return newCent;
    }
}
