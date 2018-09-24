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
          
        //����Ӧ�ó�������ƺ�����ģʽ(����)  
        SparkConf conf = new SparkConf()  
                .setAppName("Spark WordCount by Java.").setMaster("local");  
          
        //����Java SparkContext,  
        //ͨ������֮�ţ�ȥ��Ⱥ��Ψһͨ����  
        JavaSparkContext sc = new JavaSparkContext(conf);  
          
        //ʹ�ñ�������Դ������JavaRDD  
        JavaRDD<String> lines = sc.textFile("H://ScalaTraining//shell//README.md");  
          
        //�Գ�ʼ��JavaRDD����Transformation����Ĵ�������Map��Filter�߽׺����ı��   
        //��ÿ�н��в�֣�  
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {  
            @Override  
            public Iterable<String> call(String line) throws Exception { //  
                return Arrays.asList(line.split(" "));  
            }  
        });  
          
        //�Ե���ʵ�����м���Ϊ1  
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {  
            @Override  
            public Tuple2<String, Integer> call(String word) throws Exception {  
                return new Tuple2<String,Integer>(word,1) ;  
            }  
        });  
          
        // ͳ��ÿ���������ļ��г��ֵ��ܴ���  
        JavaPairRDD<String,Integer> wordsCount =   
                pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {   
                // ����ͬ��key����value�����ۼӣ�����local��reducer����ͬʱreduce������������������  
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
        //�ر�sc������  
        sc.close();  
    }  
}  
 