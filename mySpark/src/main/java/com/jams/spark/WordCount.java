package com.jams.spark;

import java.util.Arrays;
import java.util.Iterator;
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
          
        //SparkConf conf = new SparkConf().setAppName("Spark WordCount by Java.").setMaster("local");  
    	SparkConf conf = new SparkConf().setAppName("Spark WordCount by Java."); 
    	
        JavaSparkContext sc = new JavaSparkContext(conf);  
          
        //JavaRDD<String> lines = sc.textFile("hdfs://192.168.3.3:9000/user/hadoop/input/01.txt");
        JavaRDD<String> lines = sc.textFile("/home/spark/run.sh");
        
/*
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {  

            public Iterator<String> call(String line) throws Exception { //  
                return Arrays.asList(line.split(" ")).iterator();  
            }  
        });  
         
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {  
            public Tuple2<String, Integer> call(String word) throws Exception {  
                return new Tuple2<String,Integer>(word,1) ;  
            }  
        });  
          
        JavaPairRDD<String,Integer> wordsCount =   
                pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {   
             public Integer call(Integer v1, Integer v2) throws Exception {  
                return v1 + v2;  
            }  
        });  
          
        wordsCount.foreach(new VoidFunction<Tuple2<String,Integer>>() {  
            public void call(Tuple2<String, Integer> pairs) throws Exception {  
                System.out.println(pairs._1 + ":" + pairs._2);  
            }  
        });
*/   
        JavaRDD<String> words = lines.flatMap(line->Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String,Integer> pairs = words.mapToPair(word->new Tuple2<String,Integer>(word,1));
        JavaPairRDD<String,Integer> wordsCount = pairs.reduceByKey((v1,v2)->v1+v2);
        wordsCount.foreach(tuple->System.out.println(tuple._1+":"+tuple._2));
        sc.close();  
    }  
}  
 