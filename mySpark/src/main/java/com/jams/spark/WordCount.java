package com.jams.spark;

import java.util.Arrays;  
import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;  
import org.apache.spark.api.java.JavaRDD;  
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.FlatMapFunction;  
import org.apache.spark.api.java.function.Function2;  
import org.apache.spark.api.java.function.PairFunction;  
import org.apache.spark.api.java.function.VoidFunction;  
  
import scala.Tuple2;  
  
/** 
 * @author hcz 
 */  
public class WordCount {  
  
    public static void main(String[] args) {  
          
        //设置应用程序的名称和运行模式(本地)  
        SparkConf conf = new SparkConf()  
                .setAppName("Spark WordCount by Java.").setMaster("local");  
          
        //创建Java SparkContext,  
        //通往天堂之门（去集群的唯一通道）  
        JavaSparkContext sc = new JavaSparkContext(conf);  
          
        //使用本地数据源来创建JavaRDD  
        JavaRDD<String> lines = sc.textFile("H://ScalaTraining//shell//README.md");  
          
        //对初始的JavaRDD进行Transformation级别的处理，例如Map、Filter高阶函数的编程   
        //对每行进行拆分，  
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {  
            @Override  
            public Iterable<String> call(String line) throws Exception { //  
                return Arrays.asList(line.split(" "));  
            }  
        });  
          
        //对单词实例进行计数为1  
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {  
            @Override  
            public Tuple2<String, Integer> call(String word) throws Exception {  
                return new Tuple2<String,Integer>(word,1) ;  
            }  
        });  
          
        // 统计每个单词在文件中出现的总次数  
        JavaPairRDD<String,Integer> wordsCount =   
                pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {   
                // 对相同的key，对value进行累加，可以local和reducer级别同时reduce，提高网络带宽利用率  
            @Override  
            public Integer call(Integer v1, Integer v2) throws Exception {  
                return v1 + v2;  
            }  
        });  
          
        wordsCount.foreach(new VoidFunction<Tuple2<String,Integer>>() {  
            @Override  
            public void call(Tuple2<String, Integer> pairs) throws Exception {  
                System.out.println(pairs._1 + ":" + pairs._2);  
            }  
        });   
        //关闭sc上下文  
        sc.close();  
    }  
}  
 